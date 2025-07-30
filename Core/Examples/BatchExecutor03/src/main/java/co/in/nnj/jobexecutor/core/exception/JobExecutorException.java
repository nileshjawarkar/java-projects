package co.in.nnj.jobexecutor.core.exception;

/**
 * Base exception for JobExecutor related errors
 */
public class JobExecutorException extends Exception {
    
    public JobExecutorException(String message) {
        super(message);
    }
    
    public JobExecutorException(String message, Throwable cause) {
        super(message, cause);
    }
}
