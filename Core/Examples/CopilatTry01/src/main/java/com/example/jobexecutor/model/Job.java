package com.example.jobexecutor.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a job that can be executed by the JobExecutor.
 * Immutable job properties (id, type, description) with mutable execution state.
 */
public class Job {
    private final String id;
    private final String type;
    private final String description;
    private final LocalDateTime createdAt;
    
    // Mutable execution state
    private volatile JobStatus status;
    private volatile String result;
    private volatile String errorMessage;
    private volatile LocalDateTime startedAt;
    private volatile LocalDateTime completedAt;
    private volatile int retryCount;
    private volatile int maxRetries;
    
    public Job(String type, String description) {
        this(type, description, 0);
    }
    
    public Job(String type, String description, int maxRetries) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Job type cannot be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Job description cannot be null or empty");
        }
        if (maxRetries < 0) {
            throw new IllegalArgumentException("Max retries cannot be negative");
        }
        
        this.id = UUID.randomUUID().toString();
        this.type = type.trim();
        this.description = description.trim();
        this.createdAt = LocalDateTime.now();
        this.status = JobStatus.PENDING;
        this.retryCount = 0;
        this.maxRetries = maxRetries;
    }

    // Immutable getters
    public String getId() { return id; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Mutable state getters and setters
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { 
        this.status = Objects.requireNonNull(status, "Status cannot be null"); 
    }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public int getRetryCount() { return retryCount; }
    public void incrementRetryCount() { this.retryCount++; }
    public void setRetryCount(int retryCount) { 
        if (retryCount < 0) {
            throw new IllegalArgumentException("Retry count cannot be negative");
        }
        this.retryCount = retryCount; 
    }
    
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) {
        if (maxRetries < 0) {
            throw new IllegalArgumentException("Max retries cannot be negative");
        }
        this.maxRetries = maxRetries;
    }
    
    /**
     * Checks if this job can be retried
     */
    public boolean canRetry() {
        return retryCount < maxRetries;
    }
    
    /**
     * Checks if this job is in a final state (completed or permanently failed)
     */
    public boolean isCompleted() {
        return status == JobStatus.COMPLETED || 
               (status == JobStatus.FAILED && !canRetry());
    }
    
    /**
     * Resets job for retry attempt
     */
    public void prepareForRetry() {
        if (!canRetry()) {
            throw new IllegalStateException("Job cannot be retried");
        }
        this.status = JobStatus.PENDING;
        this.result = null;
        this.errorMessage = null;
        this.startedAt = null;
        this.completedAt = null;
        incrementRetryCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", retryCount=" + retryCount +
                ", maxRetries=" + maxRetries +
                ", createdAt=" + createdAt +
                '}';
    }
}
