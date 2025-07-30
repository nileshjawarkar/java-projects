package co.in.nnj.jobexecutor.core.model;

/**
 * Represents the status of a job
 */
public enum JobStatus {
    PENDING,    // Job is waiting to be executed
    RUNNING,    // Job is currently being executed
    COMPLETED,  // Job completed successfully
    FAILED,     // Job failed during execution
    
    // Execution result statuses
    OK,         // Job executed successfully
    WARNING,    // Job executed with warnings
    INIT_REQUIRED  // New status to trigger re-initialization
}
