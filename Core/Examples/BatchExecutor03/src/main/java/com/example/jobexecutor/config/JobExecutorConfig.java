package com.example.jobexecutor.config;

import java.time.Duration;

/**
 * Configuration class for JobExecutor with sensible defaults and validation.
 * Provides centralized configuration management with builder pattern.
 */
public class JobExecutorConfig {
    
    // Default values
    public static final int DEFAULT_MAX_CONCURRENT_JOBS = 2;
    public static final Duration DEFAULT_NO_JOB_WAIT_TIME = Duration.ofSeconds(2);
    public static final Duration DEFAULT_SHUTDOWN_TIMEOUT = Duration.ofSeconds(30);
    public static final int DEFAULT_MAX_QUEUE_SIZE = 100;
    public static final int DEFAULT_MAX_RETRY_ATTEMPTS = 3;
    public static final Duration DEFAULT_RETRY_DELAY = Duration.ofSeconds(5);
    public static final Duration DEFAULT_JOB_TIMEOUT = Duration.ofMinutes(10);
    
    private final int maxConcurrentJobs;
    private final Duration noJobWaitTime;
    private final Duration shutdownTimeout;
    private final int maxQueueSize;
    private final int maxRetryAttempts;
    private final Duration retryDelay;
    private final Duration jobTimeout;
    private final boolean enableRetry;
    private final boolean enableTimeout;
    
    private JobExecutorConfig(Builder builder) {
        this.maxConcurrentJobs = builder.maxConcurrentJobs;
        this.noJobWaitTime = builder.noJobWaitTime;
        this.shutdownTimeout = builder.shutdownTimeout;
        this.maxQueueSize = builder.maxQueueSize;
        this.maxRetryAttempts = builder.maxRetryAttempts;
        this.retryDelay = builder.retryDelay;
        this.jobTimeout = builder.jobTimeout;
        this.enableRetry = builder.enableRetry;
        this.enableTimeout = builder.enableTimeout;
    }
    
    // Getters
    public int getMaxConcurrentJobs() { return maxConcurrentJobs; }
    public Duration getNoJobWaitTime() { return noJobWaitTime; }
    public Duration getShutdownTimeout() { return shutdownTimeout; }
    public int getMaxQueueSize() { return maxQueueSize; }
    public int getMaxRetryAttempts() { return maxRetryAttempts; }
    public Duration getRetryDelay() { return retryDelay; }
    public Duration getJobTimeout() { return jobTimeout; }
    public boolean isRetryEnabled() { return enableRetry; }
    public boolean isTimeoutEnabled() { return enableTimeout; }
    
    /**
     * Creates a default configuration
     */
    public static JobExecutorConfig defaultConfig() {
        return new Builder().build();
    }
    
    /**
     * Creates a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder for JobExecutorConfig
     */
    public static class Builder {
        private int maxConcurrentJobs = DEFAULT_MAX_CONCURRENT_JOBS;
        private Duration noJobWaitTime = DEFAULT_NO_JOB_WAIT_TIME;
        private Duration shutdownTimeout = DEFAULT_SHUTDOWN_TIMEOUT;
        private int maxQueueSize = DEFAULT_MAX_QUEUE_SIZE;
        private int maxRetryAttempts = DEFAULT_MAX_RETRY_ATTEMPTS;
        private Duration retryDelay = DEFAULT_RETRY_DELAY;
        private Duration jobTimeout = DEFAULT_JOB_TIMEOUT;
        private boolean enableRetry = true;
        private boolean enableTimeout = true;
        
        public Builder maxConcurrentJobs(int maxConcurrentJobs) {
            if (maxConcurrentJobs <= 0) {
                throw new IllegalArgumentException("maxConcurrentJobs must be positive");
            }
            this.maxConcurrentJobs = maxConcurrentJobs;
            return this;
        }
        
        public Builder noJobWaitTime(Duration noJobWaitTime) {
            if (noJobWaitTime == null || noJobWaitTime.isNegative()) {
                throw new IllegalArgumentException("noJobWaitTime must be positive");
            }
            this.noJobWaitTime = noJobWaitTime;
            return this;
        }
        
        public Builder shutdownTimeout(Duration shutdownTimeout) {
            if (shutdownTimeout == null || shutdownTimeout.isNegative()) {
                throw new IllegalArgumentException("shutdownTimeout must be positive");
            }
            this.shutdownTimeout = shutdownTimeout;
            return this;
        }
        
        public Builder maxQueueSize(int maxQueueSize) {
            if (maxQueueSize <= 0) {
                throw new IllegalArgumentException("maxQueueSize must be positive");
            }
            this.maxQueueSize = maxQueueSize;
            return this;
        }
        
        public Builder maxRetryAttempts(int maxRetryAttempts) {
            if (maxRetryAttempts < 0) {
                throw new IllegalArgumentException("maxRetryAttempts must be non-negative");
            }
            this.maxRetryAttempts = maxRetryAttempts;
            return this;
        }
        
        public Builder retryDelay(Duration retryDelay) {
            if (retryDelay == null || retryDelay.isNegative()) {
                throw new IllegalArgumentException("retryDelay must be positive");
            }
            this.retryDelay = retryDelay;
            return this;
        }
        
        public Builder jobTimeout(Duration jobTimeout) {
            if (jobTimeout == null || jobTimeout.isNegative()) {
                throw new IllegalArgumentException("jobTimeout must be positive");
            }
            this.jobTimeout = jobTimeout;
            return this;
        }
        
        public Builder enableRetry(boolean enableRetry) {
            this.enableRetry = enableRetry;
            return this;
        }
        
        public Builder enableTimeout(boolean enableTimeout) {
            this.enableTimeout = enableTimeout;
            return this;
        }
        
        public JobExecutorConfig build() {
            return new JobExecutorConfig(this);
        }
    }
    
    @Override
    public String toString() {
        return "JobExecutorConfig{" +
                "maxConcurrentJobs=" + maxConcurrentJobs +
                ", noJobWaitTime=" + noJobWaitTime +
                ", shutdownTimeout=" + shutdownTimeout +
                ", maxQueueSize=" + maxQueueSize +
                ", maxRetryAttempts=" + maxRetryAttempts +
                ", retryDelay=" + retryDelay +
                ", jobTimeout=" + jobTimeout +
                ", enableRetry=" + enableRetry +
                ", enableTimeout=" + enableTimeout +
                '}';
    }
}
