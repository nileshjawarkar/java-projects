package co.in.nnj.jobexecutor.core.handler;

import co.in.nnj.jobexecutor.core.model.Job;
import co.in.nnj.jobexecutor.core.model.JobStatus;

/**
 * Interface for handling job execution.
 * This interface should be implemented by users to define custom job processing logic.
 */
public interface JobHandler {
    
    /**
     * Check initialization requirements before job retrieval and execution.
     * This method verifies that all prerequisites are satisfied for job processing.
     * JobExecutor will not retrieve or schedule jobs until this method returns true.
     * 
     * @return true if all prerequisites are satisfied and job processing can proceed,
     *         false if initialization requirements are not met
     */
    boolean init();
    
    /**
     * Retrieve a job from the external source.
     * This method is only called after init() returns true.
     * 
     * @return Job to be processed, or null if no job is available
     */
    Job retrieve();
    
    /**
     * Called when a job starts execution.
     * This method can be used for initialization, logging, or setup tasks.
     * 
     * @param job The job that is starting
     */
    void onJobStart(Job job);
    
    /**
     * Executes the actual job logic by calling the job's execute method.
     * This is the main method where the job's work is performed.
     * 
     * @param job The job to execute
     * @return The status of job execution (OK, FAILED, or WARNING)
     * @throws Exception if job execution fails
     */
    default JobStatus execute(Job job) throws Exception {
        return job.execute();
    }
    
    /**
     * Called when a job completes execution (either successfully or with failure).
     * This method can be used for cleanup, logging, or notification tasks.
     * 
     * @param job The job that completed
     * @param success Whether the job completed successfully
     * @param executionStatus The execution status returned by execute method
     * @param error The error that occurred (null if successful)
     */
    void onJobEnd(Job job, boolean success, JobStatus executionStatus, Exception error);
    
    /**
     * Called when a job is being retried.
     * This allows the handler to track retry metrics and perform any retry-specific logic.
     * 
     * @param job The job being retried
     * @param retryCount The current retry attempt number (1-based)
     * @param maxRetries The maximum number of retries allowed
     * @param reason The reason for the retry
     */
    default void onJobRetry(Job job, int retryCount, int maxRetries, String reason) {
        // Default implementation does nothing - handlers can override if needed
    }
    
    /**
     * Called to record job completion metrics.
     * This allows the handler to manage its own metrics collection.
     * 
     * @param job The job that was executed
     * @param executionTimeMs Execution time in milliseconds
     * @param success Whether the job completed successfully
     * @param hasWarnings Whether the job completed with warnings
     * @param requiresInit Whether the job required re-initialization
     */
    default void recordJobMetrics(Job job, long executionTimeMs, boolean success, boolean hasWarnings, boolean requiresInit) {
        // Default implementation does nothing - handlers can override if needed
    }
}
