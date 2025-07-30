package co.in.nnj.jobexecutor.core.metrics;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Thread-safe metrics collector for JobExecutor
 */
public class JobExecutorMetrics {
    
    private final LongAdder totalJobsProcessed = new LongAdder();
    private final LongAdder totalJobsSucceeded = new LongAdder();
    private final LongAdder totalJobsFailed = new LongAdder();
    private final LongAdder totalJobsWithWarnings = new LongAdder();
    private final LongAdder totalJobsRetried = new LongAdder();
    private final LongAdder totalJobsTimedOut = new LongAdder();
    
    private final AtomicLong totalExecutionTimeMs = new AtomicLong(0);
    private final AtomicLong maxExecutionTimeMs = new AtomicLong(0);
    private final AtomicLong minExecutionTimeMs = new AtomicLong(Long.MAX_VALUE);
    
    private volatile LocalDateTime startTime;
    private volatile LocalDateTime lastJobCompletedAt;
    
    public void recordJobStarted() {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
    }
    
    public void recordJobCompleted(long executionTimeMs, boolean success, boolean warning, boolean timeout) {
        totalJobsProcessed.increment();
        lastJobCompletedAt = LocalDateTime.now();
        
        if (timeout) {
            totalJobsTimedOut.increment();
        } else if (success) {
            if (warning) {
                totalJobsWithWarnings.increment();
            } else {
                totalJobsSucceeded.increment();
            }
        } else {
            totalJobsFailed.increment();
        }
        
        // Update execution time metrics
        totalExecutionTimeMs.addAndGet(executionTimeMs);
        updateMaxExecutionTime(executionTimeMs);
        updateMinExecutionTime(executionTimeMs);
    }
    
    public void recordJobRetried() {
        totalJobsRetried.increment();
    }
    
    private void updateMaxExecutionTime(long executionTimeMs) {
        long current = maxExecutionTimeMs.get();
        while (executionTimeMs > current) {
            if (maxExecutionTimeMs.compareAndSet(current, executionTimeMs)) {
                break;
            }
            current = maxExecutionTimeMs.get();
        }
    }
    
    private void updateMinExecutionTime(long executionTimeMs) {
        long current = minExecutionTimeMs.get();
        while (executionTimeMs < current) {
            if (minExecutionTimeMs.compareAndSet(current, executionTimeMs)) {
                break;
            }
            current = minExecutionTimeMs.get();
        }
    }
    
    // Getters for metrics
    public long getTotalJobsProcessed() { return totalJobsProcessed.sum(); }
    public long getTotalJobsSucceeded() { return totalJobsSucceeded.sum(); }
    public long getTotalJobsFailed() { return totalJobsFailed.sum(); }
    public long getTotalJobsWithWarnings() { return totalJobsWithWarnings.sum(); }
    public long getTotalJobsRetried() { return totalJobsRetried.sum(); }
    public long getTotalJobsTimedOut() { return totalJobsTimedOut.sum(); }
    
    public long getTotalExecutionTimeMs() { return totalExecutionTimeMs.get(); }
    public long getMaxExecutionTimeMs() { 
        long max = maxExecutionTimeMs.get();
        return max == 0 ? 0 : max;
    }
    public long getMinExecutionTimeMs() { 
        long min = minExecutionTimeMs.get();
        return min == Long.MAX_VALUE ? 0 : min;
    }
    
    public double getAverageExecutionTimeMs() {
        long total = getTotalJobsProcessed();
        return total == 0 ? 0.0 : (double) getTotalExecutionTimeMs() / total;
    }
    
    public double getSuccessRate() {
        long total = getTotalJobsProcessed();
        return total == 0 ? 0.0 : (double) getTotalJobsSucceeded() / total * 100;
    }
    
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getLastJobCompletedAt() { return lastJobCompletedAt; }
    
    public void reset() {
        totalJobsProcessed.reset();
        totalJobsSucceeded.reset();
        totalJobsFailed.reset();
        totalJobsWithWarnings.reset();
        totalJobsRetried.reset();
        totalJobsTimedOut.reset();
        
        totalExecutionTimeMs.set(0);
        maxExecutionTimeMs.set(0);
        minExecutionTimeMs.set(Long.MAX_VALUE);
        
        startTime = null;
        lastJobCompletedAt = null;
    }
    
    @Override
    public String toString() {
        return "JobExecutorMetrics{" +
                "totalProcessed=" + getTotalJobsProcessed() +
                ", succeeded=" + getTotalJobsSucceeded() +
                ", failed=" + getTotalJobsFailed() +
                ", warnings=" + getTotalJobsWithWarnings() +
                ", retried=" + getTotalJobsRetried() +
                ", timedOut=" + getTotalJobsTimedOut() +
                ", successRate=" + String.format("%.2f%%", getSuccessRate()) +
                ", avgExecutionTime=" + String.format("%.2fms", getAverageExecutionTimeMs()) +
                '}';
    }
}
