package co.in.nnj.jobexecutor.impl.job;

import co.in.nnj.jobexecutor.core.model.Job;
import co.in.nnj.jobexecutor.core.model.JobStatus;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Simple implementation of Job interface.
 * This is a basic job with id, type, and description.
 */
public class SimpleJob implements Job {
    private final String id;
    private final String type;
    private final String description;
    private final LocalDateTime createdAt;
    private volatile JobStatus status;
    private volatile String result;
    private volatile String errorMessage;
    private volatile LocalDateTime startedAt;
    private volatile LocalDateTime completedAt;
    private volatile int retryCount;
    private volatile int maxRetries;
    
    public SimpleJob(String type, String description) {
        this(type, description, 3);
    }
    
    public SimpleJob(String type, String description, int maxRetries) {
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

    // Job interface implementation
    @Override
    public String getId() { return id; }
    
    @Override
    public JobStatus execute() throws Exception {
        // Simulate some work
        Thread.sleep(100 + (int)(Math.random() * 500)); // 100-600ms
        
        // Simulate different outcomes based on job type
        switch (type.toLowerCase()) {
            case "data_processing":
                return Math.random() < 0.5 ? JobStatus.OK : JobStatus.WARNING;
            case "notification":
                return JobStatus.OK;
            case "file_cleanup":
                return Math.random() < 0.8 ? JobStatus.OK : JobStatus.WARNING;
            case "report_generation":
                return Math.random() < 0.9 ? JobStatus.OK : JobStatus.FAILED;
            case "custom_task":
                return JobStatus.OK;
            default:
                double rand = Math.random();
                return rand < 0.7 ? JobStatus.OK : 
                      (rand < 0.85 ? JobStatus.WARNING : JobStatus.FAILED);
        }
    }
    
    // Additional getters for identification (not part of Job interface)
    public String getType() { return type; }
    
    public String getDescription() { return description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }

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
    
    public int getMaxRetries() { return maxRetries; }
    
    public void setMaxRetries(int maxRetries) {
        if (maxRetries < 0) {
            throw new IllegalArgumentException("Max retries cannot be negative");
        }
        this.maxRetries = maxRetries;
    }

    private void incrementRetryCount() { 
        this.retryCount++; 
    }

    public boolean canRetry() {
        return status == JobStatus.FAILED && retryCount < maxRetries;
    }

    public boolean isCompleted() {
        return status == JobStatus.COMPLETED || 
               (status == JobStatus.FAILED && !canRetry());
    }
    
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
        SimpleJob simpleJob = (SimpleJob) o;
        return Objects.equals(id, simpleJob.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SimpleJob{" +
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
