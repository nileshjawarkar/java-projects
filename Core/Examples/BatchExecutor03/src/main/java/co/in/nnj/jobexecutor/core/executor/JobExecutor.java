package co.in.nnj.jobexecutor.core.executor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.jobexecutor.core.config.JobExecutorConfig;
import co.in.nnj.jobexecutor.core.handler.JobHandler;
import co.in.nnj.jobexecutor.core.model.Job;
import co.in.nnj.jobexecutor.core.model.JobStatus;
import co.in.nnj.jobexecutor.core.model.QueuedJob;

/**
 * Enhanced JobExecutor with improved fault tolerance, metrics, and
 * configuration support.
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

    // Thread management
    private final ExecutorService executorService;
    private final AtomicBoolean isRunning;
    private final AtomicInteger activeWorkers;

    // Job queues
    private final BlockingQueue<QueuedJob> retrievedJobsQueue;
    private final BlockingQueue<QueuedJob> retryQueue;

    // Add initialization state tracking
    private final AtomicBoolean isInitialized;
    private volatile long lastInitCheckTime = 0;
    private static final long INIT_CHECK_INTERVAL_MS = 5000; // Check init every 5 seconds when not initialized

    /**
     * Creates a JobExecutor with default configuration
     */
    public JobExecutor(final JobHandler jobHandler) {
        this(JobExecutorConfig.defaultConfig(), jobHandler);
    }

    /**
     * Creates a JobExecutor with the specified configuration
     */
    public JobExecutor(final JobExecutorConfig config, final JobHandler jobHandler) {
        this.config = Objects.requireNonNull(config, "Config cannot be null");
        this.jobHandler = Objects.requireNonNull(jobHandler, "JobHandler cannot be null");

        // Initialize thread management
        this.executorService = Executors.newFixedThreadPool(
                config.getMaxConcurrentJobs(),
                r -> {
                    final Thread t = new Thread(r, "JobExecutor-Worker");
                    t.setDaemon(true);
                    return t;
                });

        this.isRunning = new AtomicBoolean(false);
        this.activeWorkers = new AtomicInteger(0);

        // Initialize queues with bounds checking
        this.retrievedJobsQueue = new ArrayBlockingQueue<>(config.getMaxQueueSize());
        this.retryQueue = new LinkedBlockingQueue<>();

        this.isInitialized = new AtomicBoolean(false);

        logger.info("JobExecutor initialized with config: {}", config);
    }

    /**
     * Legacy constructor for backward compatibility
     */
    public JobExecutor(final int maxConcurrentJobs, final JobHandler jobHandler) {
        this(JobExecutorConfig.builder()
                .maxConcurrentJobs(maxConcurrentJobs)
                .build(), jobHandler);
    }

    /**
     * Starts the job executor and begins job processing.
     * Main thread handles initialization, job retrieval, executor service handles
     * job processing.
     */
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            logger.info("Starting JobExecutor with {} concurrent workers", config.getMaxConcurrentJobs());

            logger.info("Starting initialization check and job processing in main thread");

            // Cache current time to reduce System.currentTimeMillis() calls
            long lastTimeUpdate = System.currentTimeMillis();
            long cachedCurrentTime = lastTimeUpdate;
            final long TIME_CACHE_DURATION_MS = 100; // Update cached time every 100ms

            // Main thread handles initialization check, job retrieval and delegates
            // processing to executor service
            while (isRunning.get()) {
                try {
                    // Update cached time periodically to reduce system calls
                    if (cachedCurrentTime - lastTimeUpdate > TIME_CACHE_DURATION_MS) {
                        cachedCurrentTime = System.currentTimeMillis();
                        lastTimeUpdate = cachedCurrentTime;
                    }

                    // Check initialization status first
                    if (!checkInitialization(cachedCurrentTime)) {
                        // Not initialized, wait before checking again
                        Thread.sleep(Math.min(INIT_CHECK_INTERVAL_MS, config.getNoJobWaitTime().toMillis()));
                        cachedCurrentTime = System.currentTimeMillis();
                        lastTimeUpdate = cachedCurrentTime;
                        continue;
                    }

                    // Retrieve job (checks queue first, then retry queue, then
                    // JobHandler.retrieve())
                    final Optional<QueuedJob> jobOptional = retrieveJob(cachedCurrentTime);

                    if (jobOptional.isPresent()) {
                        final QueuedJob job = jobOptional.get();
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
                        // Update time after sleep
                        cachedCurrentTime = System.currentTimeMillis();
                        lastTimeUpdate = cachedCurrentTime;
                    }

                } catch (final InterruptedException e) {
                    logger.info("Main thread interrupted");
                    Thread.currentThread().interrupt();
                    break;
                } catch (final Exception e) {
                    logger.error("Error in main thread", e);
                    try {
                        Thread.sleep(config.getNoJobWaitTime().toMillis());
                        // Update time after sleep
                        cachedCurrentTime = System.currentTimeMillis();
                        lastTimeUpdate = cachedCurrentTime;
                    } catch (final InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            logger.info("Job processing stopped");
        }
    }

    /**
     * Check if JobHandler is properly initialized.
     * Uses caching to avoid frequent init() calls.
     * 
     * @param currentTime Current time in milliseconds
     * @return true if initialized and ready for job processing
     */
    private boolean checkInitialization(final long currentTime) {
        // If already initialized, return true (until INIT_REQUIRED is encountered)
        if (isInitialized.get()) {
            return true;
        }

        // Throttle init checks to avoid overwhelming the JobHandler
        if (currentTime - lastInitCheckTime < INIT_CHECK_INTERVAL_MS) {
            return false;
        }

        try {
            final boolean initResult = jobHandler.init();
            lastInitCheckTime = currentTime;

            if (initResult) {
                isInitialized.set(true);
                logger.info("JobHandler initialization successful - job processing enabled");
                return true;
            } else {
                logger.debug("JobHandler initialization not ready - job processing disabled");
                return false;
            }
        } catch (final Exception e) {
            logger.error("Error during JobHandler initialization", e);
            lastInitCheckTime = currentTime;
            return false;
        }
    }

    /**
     * Force re-initialization check (called when job returns INIT_REQUIRED).
     */
    private void forceInitializationCheck() {
        isInitialized.set(false);
        lastInitCheckTime = 0; // Reset to force immediate check
        logger.warn("Forcing re-initialization due to INIT_REQUIRED status");
    }

    /**
     * Retrieve a job by checking multiple sources in priority order.
     * Only called when JobHandler is initialized.
     */
    private Optional<QueuedJob> retrieveJob(final long currentTime) {
        // Priority 1: Check retrieved jobs queue first (non-blocking)
        QueuedJob job = retrievedJobsQueue.poll();

        if (job != null) {
            logger.debug("Retrieved job {} from retrieved jobs queue", job.getId());
            return Optional.of(job);
        }

        // Priority 2: Check retry queue for jobs ready to retry
        job = retryQueue.peek(); // Peek first to check timing
        if (job != null && job.canRetry()) {
            // Ensure timestamp exists - if missing, create one now
            if (job.getRetryTimestamp() == 0) {
                final long retryTime = currentTime + config.getRetryDelay().toMillis();
                job.setRetryTimestamp(retryTime);
                logger.warn("Missing retry timestamp for job {}, created new timestamp", job.getId());
            }

            if (job.isReadyForRetry(currentTime)) {
                // Ready for retry - remove from queue
                job = retryQueue.poll();
                job.prepareForRetry();
                jobHandler.onJobRetry(job.getWrappedJob(), job.getRetryCount() + 1, job.getMaxRetries(),
                        "Retry attempt");
                logger.info("Retrying job {} (attempt {}/3)",
                        job.getId(), job.getRetryCount());
                return Optional.of(job);
            }
            // Not ready yet, continue to next priority
        } else if (job != null) {
            // Job cannot retry anymore, discard it
            retryQueue.poll(); // Remove it from queue
            logger.debug("Discarding job {} - no more retries available", job.getId());
        }

        // Priority 3: Get new job from JobHandler (only if initialized)
        try {
            final Job newJob = jobHandler.retrieve();
            if (newJob != null) {
                final QueuedJob queuedJob = new QueuedJob(newJob);
                logger.debug("Retrieved new job {} from JobHandler", queuedJob.getId());
                return Optional.of(queuedJob);
            }
            return Optional.empty();
        } catch (final Exception e) {
            logger.error("Error retrieving job from JobHandler", e);
            return Optional.empty();
        }
    }

    /**
     * Execute a single job with error handling and initialization status handling.
     */
    private void executeJob(final QueuedJob job) {
        logger.info("Starting execution of job: {}", job.getId());
        final long startTimeNanos = System.nanoTime(); // Use nanoTime for precise duration measurement

        try {
            // Set job status to running
            job.setStatus(JobStatus.RUNNING);
            job.setStartedAt(LocalDateTime.now());

            // Call job start handler
            jobHandler.onJobStart(job.getWrappedJob());

            // Execute job directly
            final JobStatus result = jobHandler.execute(job.getWrappedJob());
            final long executionTimeMs = (System.nanoTime() - startTimeNanos) / 1_000_000; // Convert to milliseconds

            // Update job status based on result
            switch (result) {
                case OK:
                    job.setStatus(JobStatus.COMPLETED);
                    job.setResult("Job completed successfully");
                    jobHandler.recordJobMetrics(job.getWrappedJob(), executionTimeMs, true, false, false);
                    logger.info("Job {} completed successfully", job.getId());
                    break;
                case WARNING:
                    job.setStatus(JobStatus.COMPLETED);
                    job.setResult("Job completed with warnings");
                    jobHandler.recordJobMetrics(job.getWrappedJob(), executionTimeMs, true, true, false);
                    logger.warn("Job {} completed with warnings", job.getId());
                    break;
                case INIT_REQUIRED:
                    job.setStatus(JobStatus.FAILED);
                    job.setResult("Job requires re-initialization");
                    jobHandler.recordJobMetrics(job.getWrappedJob(), executionTimeMs, false, false, true);
                    logger.warn("Job {} requires re-initialization", job.getId());

                    // Force re-initialization check
                    forceInitializationCheck();

                    // Put job back for retry if possible
                    if (job.canRetry()) {
                        handleJobRetry(job, "Re-initialization required");
                    }
                    break;
                case FAILED:
                    handleJobFailure(job, executionTimeMs);
                    break;
                default:
                    logger.warn("Unexpected job result: {}", result);
                    handleJobFailure(job, executionTimeMs);
                    break;
            }

            job.setCompletedAt(LocalDateTime.now());

            // Call job end handler with proper signature
            final boolean success = (result == JobStatus.OK || result == JobStatus.WARNING);
            jobHandler.onJobEnd(job.getWrappedJob(), success, result, null);

        } catch (final Exception e) {
            final long executionTimeMs = (System.nanoTime() - startTimeNanos) / 1_000_000; // Convert to milliseconds
            logger.error("Error executing job {}", job.getId(), e);
            handleJobFailure(job, executionTimeMs, e);
        }
    }

    /**
     * Handle job retry (for INIT_REQUIRED case).
     */
    private void handleJobRetry(final QueuedJob job, final String reason) {
        logger.info("Scheduling job {} for retry due to: {}", job.getId(), reason);

        // Calculate retry time - get current time once for efficiency
        final long currentTime = System.currentTimeMillis();
        final long retryTime = currentTime + config.getRetryDelay().toMillis();

        // Set retry timestamp and queue the job
        job.setRetryTimestamp(retryTime);
        final boolean queued = retryQueue.offer(job);

        if (!queued) {
            logger.error("Failed to queue job {} for retry - queue may be full", job.getId());
        } else {
            logger.debug("Job {} scheduled for retry at {}", job.getId(), retryTime);
        }
    }

    // Getters for monitoring
    public boolean isRunning() {
        return isRunning.get();
    }

    public int getQueueSize() {
        return retrievedJobsQueue.size();
    }

    public int getActiveWorkers() {
        return activeWorkers.get();
    }

    public JobExecutorConfig getConfig() {
        return config;
    }

    public boolean isInitialized() {
        return isInitialized.get();
    }

    /**
     * Handle job failure with retry logic.
     */
    private void handleJobFailure(final QueuedJob job, final long executionTime) {
        handleJobFailure(job, executionTime, null);
    }

    /**
     * Handle job failure with retry logic and exception.
     */
    private void handleJobFailure(final QueuedJob job, final long executionTime, final Exception error) {
        job.setStatus(JobStatus.FAILED);
        job.setErrorMessage(error != null ? error.getMessage() : "Job execution failed");
        job.setCompletedAt(LocalDateTime.now());
        jobHandler.recordJobMetrics(job.getWrappedJob(), executionTime, false, false, false);

        // Call job end handler
        jobHandler.onJobEnd(job.getWrappedJob(), false, JobStatus.FAILED, error);

        if (job.canRetry()) {
            logger.info("Scheduling job {} for retry (attempt {}/3)",
                    job.getId(), job.getRetryCount() + 1);

            // Calculate retry time - get current time once for efficiency
            final long currentTime = System.currentTimeMillis();
            final long retryTime = currentTime + config.getRetryDelay().toMillis();

            // Set retry timestamp and queue the job
            job.setRetryTimestamp(retryTime);
            final boolean queued = retryQueue.offer(job);

            if (!queued) {
                logger.error("Failed to queue job {} for retry - queue may be full", job.getId());
            } else {
                logger.debug("Job {} scheduled for retry at {}", job.getId(), retryTime);
            }
        } else {
            logger.error("Job {} failed permanently (no retries available)", job.getId());
        }
    }

    /**
     * Stops the job executor and waits for running jobs to complete.
     */
    public void stop() {
        if (isRunning.compareAndSet(true, false)) {
            logger.info("Stopping JobExecutor...");

            // Shutdown executor service gracefully
            executorService.shutdown();
            try {
                // Wait for running jobs to complete (up to 30 seconds)
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    logger.warn("Some jobs did not complete within 30 seconds, forcing shutdown");
                    executorService.shutdownNow();
                    // Wait a bit more for forced shutdown
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        logger.error("Executor did not terminate gracefully");
                    }
                }
                logger.info("JobExecutor stopped successfully");
            } catch (final InterruptedException e) {
                logger.warn("Interrupted while waiting for jobs to complete");
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Implementation of AutoCloseable interface.
     * Calls stop() to gracefully shutdown the executor.
     */
    @Override
    public void close() {
        stop();
    }
}
