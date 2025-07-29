package com.example.jobexecutor.model;

/**
 * Interface for jobs that can be executed by the JobExecutor.
 * This interface defines the contract for all job implementations.
 */
public interface Job {
    
    /**
     * Get the unique identifier for this job.
     * 
     * @return String representing the unique job ID
     */
    String getId();
    
    /**
     * Execute the job and return the execution status.
     * 
     * @return JobStatus indicating the result of job execution (OK, WARNING, or FAILED)
     * @throws Exception if job execution fails
     */
    JobStatus execute() throws Exception;
}
