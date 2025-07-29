package com.example.jobexecutor.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Final wrapper that holds a Job instance and adds execution state management.
 * This class contains only the details required by JobExecutor for job processing.
 * 
 * Thread Safety: Each QueuedJob instance is owned by a single thread at any given time
 * (main thread for retrieval, worker thread for execution, retry thread for retries).
 * No concurrent access occurs, so volatile modifiers are not needed.
 * 
 * The class is marked final to prevent inheritance and ensure controlled behavior.
 */
public final class QueuedJob {
    private final Job job;
    
    // Execution state fields - no volatile needed since each instance is owned by single thread
    private JobStatus status;
    private String result;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private int retryCount;
    private final int maxRetries;
    
    public QueuedJob(Job job) {
        this(job, 3); // Default max retries
    }
    
    public QueuedJob(Job job, int maxRetries) {
        this.job = Objects.requireNonNull(job, "Job cannot be null");
        if (maxRetries < 0) {
            throw new IllegalArgumentException("Max retries cannot be negative");
        }
        this.status = JobStatus.PENDING;
        this.retryCount = 0;
        this.maxRetries = maxRetries;
    }

    // Required by JobExecutor for identification and logging
    public String getId() { 
        return job.getId(); 
    }
    
    // Required by JobExecutor for status management
    public void setStatus(JobStatus status) { 
        this.status = Objects.requireNonNull(status, "Status cannot be null"); 
    }

    // Required by JobExecutor to set completion results
    public void setResult(String result) { 
        this.result = result; 
    }

    // Required by JobExecutor to set error details
    public void setErrorMessage(String errorMessage) { 
        this.errorMessage = errorMessage; 
    }

    // Required by JobExecutor to track execution timing
    public void setStartedAt(LocalDateTime startedAt) { 
        this.startedAt = startedAt; 
    }

    // Required by JobExecutor to track completion timing
    public void setCompletedAt(LocalDateTime completedAt) { 
        this.completedAt = completedAt; 
    }

    // Required by JobExecutor for retry logic
    public int getRetryCount() { 
        return retryCount; 
    }
    
    // Required by JobExecutor for retry eligibility check
    public boolean canRetry() {
        return status == JobStatus.FAILED && retryCount < maxRetries;
    }
    
    // Required by JobExecutor to prepare job for retry
    public void prepareForRetry() {
        if (!canRetry()) {
            throw new IllegalStateException("Job cannot be retried");
        }
        this.status = JobStatus.PENDING;
        this.result = null;
        this.errorMessage = null;
        this.startedAt = null;
        this.completedAt = null;
        this.retryCount++;
    }

    // Required by JobExecutor to pass to JobHandler
    public Job getWrappedJob() {
        return job;
    }
    
    // Getter methods for accessing state
    public JobStatus getStatus() {
        return status;
    }
    
    public String getResult() {
        return result;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public int getMaxRetries() {
        return maxRetries;
    }

    // Required for queue equality and identification
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueuedJob queuedJob = (QueuedJob) o;
        return Objects.equals(getId(), queuedJob.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    // Basic toString for debugging (includes only essential info)
    @Override
    public String toString() {
        return "QueuedJob{" +
                "id='" + getId() + '\'' +
                ", status=" + status +
                ", retryCount=" + retryCount +
                ", maxRetries=" + maxRetries +
                '}';
    }
}
