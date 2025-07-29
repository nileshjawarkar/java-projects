package com.example.jobexecutor.executor;

import com.example.jobexecutor.config.JobExecutorConfig;
import com.example.jobexecutor.handler.JobHandler;
import com.example.jobexecutor.metrics.JobExecutorMetrics;
import com.example.jobexecutor.model.Job;
import com.example.jobexecutor.model.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Enhanced JobExecutor with improved fault tolerance, metrics, and configuration support.
 * 
 * Features:
 * - Configurable retry mechanism
 * - Job timeout support  
 * - Comprehensive metrics
 * - Graceful error handling
 * - Resource management
 */
public class JobExecutor implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);
    
    private final JobExecutorConfig config;
    private final JobHandler jobHandler;
    private final JobExecutorMetrics metrics;
    
    // Thread management
    private final ExecutorService executorService;
    private final AtomicBoolean isRunning;
    private final AtomicInteger activeWorkers;
    
    // Job queues
    private final BlockingQueue<Job> retrievedJobsQueue;
    private final BlockingQueue<Job> retryQueue;
    private final ConcurrentHashMap<String, Long> retryTimestamps;
    
    /**
     * Creates a JobExecutor with default configuration
     */
    public JobExecutor(JobHandler jobHandler) {
        this(JobExecutorConfig.defaultConfig(), jobHandler);
    }
    
    /**
     * Creates a JobExecutor with the specified configuration
     */
    public JobExecutor(JobExecutorConfig config, JobHandler jobHandler) {
        this.config = Objects.requireNonNull(config, "Config cannot be null");
        this.jobHandler = Objects.requireNonNull(jobHandler, "JobHandler cannot be null");
        this.metrics = new JobExecutorMetrics();
        
        // Initialize thread management
        this.executorService = Executors.newFixedThreadPool(
            config.getMaxConcurrentJobs(),
            r -> {
                Thread t = new Thread(r, "JobExecutor-Worker");
                t.setDaemon(true);
                return t;
            }
        );
        
        this.isRunning = new AtomicBoolean(false);
        this.activeWorkers = new AtomicInteger(0);
        
        // Initialize queues with bounds checking
        this.retrievedJobsQueue = new ArrayBlockingQueue<>(config.getMaxQueueSize());
        this.retryQueue = new LinkedBlockingQueue<>();
        this.retryTimestamps = new ConcurrentHashMap<>();
        
        logger.info("JobExecutor initialized with config: {}", config);
    }
    
    /**
     * Legacy constructor for backward compatibility
     */
    public JobExecutor(int maxConcurrentJobs, JobHandler jobHandler) {
        this(JobExecutorConfig.builder()
            .maxConcurrentJobs(maxConcurrentJobs)
            .build(), jobHandler);
    }
    
    /**
     * Starts the job executor and begins job processing.
     * Main thread handles job retrieval, executor service handles job processing.
     */
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            logger.info("Starting JobExecutor with {} concurrent workers", config.getMaxConcurrentJobs());
            
            logger.info("Starting job retrieval and processing in main thread");
            
            // Main thread handles job retrieval and delegates processing to executor service
            while (isRunning.get()) {
                try {
                    // Retrieve job (checks queue first, then retry queue, then JobHandler.retrieve())
                    Optional<Job> jobOptional = retrieveJob();
                    
                    if (jobOptional.isPresent()) {
                        Job job = jobOptional.get();
                        // Submit job for processing using executor service
                        executorService.submit(() -> {
                            activeWorkers.incrementAndGet();
                            try {
                                executeJob(job);
                            } finally {
                                activeWorkers.decrementAndGet();
                            }
                        });
                    } else {
                        // No job available, wait before trying again
                        Thread.sleep(config.getNoJobWaitTime().toMillis());
                    }
                    
                } catch (InterruptedException e) {
                    logger.info("Main thread job retrieval interrupted");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("Error in main thread job retrieval", e);
                    try {
                        Thread.sleep(config.getNoJobWaitTime().toMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            
            logger.info("Job retrieval stopped");
        }
    }
    
    /**
     * Stops the job executor and waits for current jobs to complete.
     */
    public void stop() {
        if (isRunning.compareAndSet(true, false)) {
            logger.info("Stopping JobExecutor...");
            
            shutdownExecutorService(executorService, "executor service", config.getShutdownTimeout());
            
            logger.info("JobExecutor stopped. Final metrics: {}", metrics);
        }
    }
    
    /**
     * Closes the JobExecutor and releases all resources
     */
    @Override
    public void close() {
        stop();
    }
    
    private void shutdownExecutorService(ExecutorService executor, String name, Duration timeout) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
                logger.warn("{} did not terminate gracefully, forcing shutdown...", name);
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.error("{} did not terminate after forced shutdown", name);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for {} to terminate", name, e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    // Getters for monitoring
    public boolean isRunning() { return isRunning.get(); }
    public int getQueueSize() { return retrievedJobsQueue.size(); }
    public int getActiveWorkers() { return activeWorkers.get(); }
    public JobExecutorMetrics getMetrics() { return metrics; }
    public JobExecutorConfig getConfig() { return config; }
    
    /**
     * Retrieve a job by checking multiple sources in priority order:
     * 1. retrievedJobsQueue (already retrieved jobs)
     * 2. retryQueue (failed jobs ready for retry)
     * 3. JobHandler.retrieve() (new jobs)
     * This method runs in the main thread and handles all job retrieval logic.
     * 
     * @return An Optional containing a job to process, or Optional.empty() if no job is available
     */
    private Optional<Job> retrieveJob() {
        // Priority 1: Check retrieved jobs queue first (non-blocking)
        Job job = retrievedJobsQueue.poll();
        
        if (job != null) {
            logger.debug("Retrieved job {} from retrieved jobs queue", job.getId());
            return Optional.of(job);
        }
        
        // Priority 2: Check retry queue for jobs ready to retry
        job = retryQueue.peek(); // Peek first to check timing
        if (job != null && job.canRetry()) {
            Long retryTime = retryTimestamps.get(job.getId());
            
            // Ensure timestamp exists - if missing, create one now
            if (retryTime == null) {
                retryTime = System.currentTimeMillis() + config.getRetryDelay().toMillis();
                retryTimestamps.put(job.getId(), retryTime);
                logger.warn("Missing retry timestamp for job {}, created new timestamp", job.getId());
            }
            
            if (System.currentTimeMillis() >= retryTime) {
                // Ready for retry - remove from queue and timestamps
                job = retryQueue.poll();
                retryTimestamps.remove(job.getId());
                job.prepareForRetry();
                metrics.recordJobRetried();
                logger.info("Retrying job {} (attempt {}/3)", 
                           job.getId(), job.getRetryCount());
                return Optional.of(job);
            }
            // Not ready yet, continue to next priority
        } else if (job != null) {
            // Job cannot retry anymore, discard it
            retryQueue.poll(); // Remove it from queue
            retryTimestamps.remove(job.getId()); // Clean up timestamp
            logger.debug("Discarding job {} - no more retries available", job.getId());
        }
        
        // Priority 3: Get new job from JobHandler
        try {
            job = jobHandler.retrieve();
            if (job != null) {
                logger.debug("Retrieved new job {} from JobHandler", job.getId());
                return Optional.of(job);
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error retrieving job from JobHandler", e);
            return Optional.empty();
        }
    }
    
    /**
     * Execute a single job with error handling (no timeout wrapper).
     */
    private void executeJob(Job job) {
        logger.info("Starting execution of job: {}", job.getId());
        long startTime = System.currentTimeMillis();
        
        try {
            // Set job status to running
            job.setStatus(JobStatus.RUNNING);
            job.setStartedAt(LocalDateTime.now());
            
            // Call job start handler
            jobHandler.onJobStart(job);
            
            // Execute job directly (no timeout wrapper)
            JobStatus result = jobHandler.execute(job);
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Update job status based on result
            switch (result) {
                case OK:
                    job.setStatus(JobStatus.COMPLETED);
                    job.setResult("Job completed successfully");
                    metrics.recordJobCompleted(executionTime, true, false, false);
                    logger.info("Job {} completed successfully", job.getId());
                    break;
                case WARNING:
                    job.setStatus(JobStatus.COMPLETED);
                    job.setResult("Job completed with warnings");
                    metrics.recordJobCompleted(executionTime, true, true, false);
                    logger.warn("Job {} completed with warnings", job.getId());
                    break;
                case FAILED:
                    handleJobFailure(job, executionTime);
                    break;
                default:
                    logger.warn("Unexpected job result: {}", result);
                    handleJobFailure(job, executionTime);
                    break;
            }
            
            job.setCompletedAt(LocalDateTime.now());
            
            // Call job end handler with proper signature
            boolean success = (result == JobStatus.OK || result == JobStatus.WARNING);
            jobHandler.onJobEnd(job, success, result, null);
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("Error executing job {}", job.getId(), e);
            handleJobFailure(job, executionTime, e);
        }
    }
    
    /**
     * Handle job failure with retry logic.
     */
    private void handleJobFailure(Job job, long executionTime) {
        handleJobFailure(job, executionTime, null);
    }
    
    /**
     * Handle job failure with retry logic and exception.
     */
    private void handleJobFailure(Job job, long executionTime, Exception error) {
        job.setStatus(JobStatus.FAILED);
        job.setErrorMessage(error != null ? error.getMessage() : "Job execution failed");
        job.setCompletedAt(LocalDateTime.now());
        metrics.recordJobCompleted(executionTime, false, false, false);
        
        // Call job end handler
        jobHandler.onJobEnd(job, false, JobStatus.FAILED, error);
        
        if (job.canRetry()) {
            logger.info("Scheduling job {} for retry (attempt {}/3)", 
                       job.getId(), job.getRetryCount() + 1);
            
            // Calculate retry time and store it atomically with queue addition
            long retryTime = System.currentTimeMillis() + config.getRetryDelay().toMillis();
            
            // Atomic operation: timestamp first, then queue
            retryTimestamps.put(job.getId(), retryTime);
            boolean queued = retryQueue.offer(job);
            
            if (!queued) {
                // If queueing failed, clean up timestamp
                retryTimestamps.remove(job.getId());
                logger.error("Failed to queue job {} for retry - queue may be full", job.getId());
            } else {
                logger.debug("Job {} scheduled for retry at {}", job.getId(), retryTime);
            }
        } else {
            logger.error("Job {} failed permanently (no retries available)", job.getId());
        }
    }
}
