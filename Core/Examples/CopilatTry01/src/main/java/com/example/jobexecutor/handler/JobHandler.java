package com.example.jobexecutor.handler;

import com.example.jobexecutor.model.Job;
import com.example.jobexecutor.model.JobStatus;

/**
 * Interface for handling job execution.
 * This interface should be implemented by users to define custom job processing logic.
 */
public interface JobHandler {
    
    /**
     * Retrieves a job from an external source if one is available.
     * This method is called to fetch a job that is ready for execution.
     * It should return null if no job is currently available.
     * 
     * @return A job that is ready for execution, or null if no job is available
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
}
