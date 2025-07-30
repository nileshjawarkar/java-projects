package co.in.nnj.jobexecutor.impl.handler;

import co.in.nnj.jobexecutor.core.handler.JobHandler;
import co.in.nnj.jobexecutor.core.model.Job;
import co.in.nnj.jobexecutor.core.model.JobStatus;
import co.in.nnj.jobexecutor.impl.job.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Example implementation of JobHandler for demonstration purposes.
 * This handler manages a queue of jobs and provides basic job processing logic.
 * It also demonstrates internal metrics collection.
 */
public class ExampleJobHandler implements JobHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExampleJobHandler.class);
    
    private final BlockingQueue<Job> availableJobs;
    
    // Internal metrics tracking
    private final AtomicLong totalJobsExecuted = new AtomicLong(0);
    private final AtomicLong successfulJobs = new AtomicLong(0);
    private final AtomicLong failedJobs = new AtomicLong(0);
    private final AtomicLong warningJobs = new AtomicLong(0);
    private final AtomicLong retriedJobs = new AtomicLong(0);
    private final AtomicLong initRequiredJobs = new AtomicLong(0);
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    
    public ExampleJobHandler() {
        this.availableJobs = new LinkedBlockingQueue<>();
        initializeSampleJobs();
        logger.info("ExampleJobHandler initialized with {} jobs", availableJobs.size());
    }
    
    /**
     * Initialize the handler with some sample jobs for demonstration
     */
    private void initializeSampleJobs() {
        availableJobs.offer(new SimpleJob("data_processing", "Process daily sales data"));
        availableJobs.offer(new SimpleJob("report_generation", "Generate monthly financial report"));
        availableJobs.offer(new SimpleJob("file_cleanup", "Clean up temporary files older than 7 days"));
        availableJobs.offer(new SimpleJob("notification", "Send status notifications to administrators"));
        availableJobs.offer(new SimpleJob("data_processing", "Process customer feedback data"));
        availableJobs.offer(new SimpleJob("custom_task", "Execute custom business logic"));
    }
    
    /**
     * Add a job to the available jobs queue
     */
    public void addJob(Job job) {
        availableJobs.offer(job);
        logger.info("Added job {} to available jobs queue", job.getId());
    }
    
    @Override
    public boolean init() {
        // For this example, always return true (always initialized)
        // In a real implementation, this would check database connections,
        // external services, configuration files, etc.
        logger.debug("ExampleJobHandler initialization check - always ready");
        return true;
    }
    
    @Override
    public Job retrieve() {
        logger.debug("Checking for available jobs...");
        
        Job job = availableJobs.poll();
        if (job != null) {
            logger.info("Retrieved job {} from queue", job.getId());
        } else {
            logger.debug("No jobs available");
        }
        
        return job;
    }
    
    @Override
    public void onJobStart(Job job) {
        logger.info("Job {} is starting execution", job.getId());
    }
    
    // The execute method is now a default method in JobHandler that calls job.execute()
    // So we don't need to override it unless we want custom logic
    
    @Override
    public void onJobEnd(Job job, boolean success, JobStatus executionStatus, Exception error) {
        if (success) {
            if (executionStatus == JobStatus.WARNING) {
                logger.warn("Job {} completed with warnings", job.getId());
            } else {
                logger.info("Job {} completed successfully", job.getId());
            }
        } else {
            logger.error("Job {} failed", job.getId(), error);
        }
    }
    
    @Override
    public void onJobRetry(Job job, int retryCount, int maxRetries, String reason) {
        retriedJobs.incrementAndGet();
        logger.info("Job {} is being retried (attempt {}/{}) due to: {}", 
                   job.getId(), retryCount, maxRetries, reason);
    }
    
    @Override
    public void recordJobMetrics(Job job, long executionTimeMs, boolean success, boolean hasWarnings, boolean requiresInit) {
        totalJobsExecuted.incrementAndGet();
        totalExecutionTime.addAndGet(executionTimeMs);
        
        if (success) {
            if (hasWarnings) {
                warningJobs.incrementAndGet();
            } else {
                successfulJobs.incrementAndGet();
            }
        } else {
            failedJobs.incrementAndGet();
        }
        
        if (requiresInit) {
            initRequiredJobs.incrementAndGet();
        }
        
        // Log metrics periodically (every 10 jobs)
        long totalJobs = totalJobsExecuted.get();
        if (totalJobs % 10 == 0) {
            logMetrics();
        }
    }
    
    /**
     * Log current metrics summary
     */
    private void logMetrics() {
        long total = totalJobsExecuted.get();
        long successful = successfulJobs.get();
        long failed = failedJobs.get();
        long warnings = warningJobs.get();
        long retried = retriedJobs.get();
        long initRequired = initRequiredJobs.get();
        long avgExecutionTime = total > 0 ? totalExecutionTime.get() / total : 0;
        
        logger.info("=== Job Metrics Summary ===");
        logger.info("Total Jobs: {}, Successful: {}, Failed: {}, Warnings: {}", 
                   total, successful, failed, warnings);
        logger.info("Retries: {}, Init Required: {}, Avg Execution Time: {}ms", 
                   retried, initRequired, avgExecutionTime);
        logger.info("==========================");
    }
    
    /**
     * Get current metrics for monitoring
     */
    public String getMetricsReport() {
        long total = totalJobsExecuted.get();
        long successful = successfulJobs.get();
        long failed = failedJobs.get();
        long warnings = warningJobs.get();
        long retried = retriedJobs.get();
        long initRequired = initRequiredJobs.get();
        long avgExecutionTime = total > 0 ? totalExecutionTime.get() / total : 0;
        
        return String.format("JobHandler Metrics - Total: %d, Success: %d, Failed: %d, Warnings: %d, Retries: %d, InitRequired: %d, AvgTime: %dms",
                           total, successful, failed, warnings, retried, initRequired, avgExecutionTime);
    }
}
