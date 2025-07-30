package co.in.nnj.jobexecutor.core.config;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for JobExecutorConfig and its Builder
 */
class JobExecutorConfigTest {

    @Test
    void testDefaultConfig() {
        JobExecutorConfig config = JobExecutorConfig.defaultConfig();
        
        assertEquals(JobExecutorConfig.DEFAULT_MAX_CONCURRENT_JOBS, config.getMaxConcurrentJobs());
        assertEquals(JobExecutorConfig.DEFAULT_NO_JOB_WAIT_TIME, config.getNoJobWaitTime());
        assertEquals(JobExecutorConfig.DEFAULT_SHUTDOWN_TIMEOUT, config.getShutdownTimeout());
        assertEquals(JobExecutorConfig.DEFAULT_MAX_QUEUE_SIZE, config.getMaxQueueSize());
        assertEquals(JobExecutorConfig.DEFAULT_MAX_RETRY_ATTEMPTS, config.getMaxRetryAttempts());
        assertEquals(JobExecutorConfig.DEFAULT_RETRY_DELAY, config.getRetryDelay());
        assertEquals(JobExecutorConfig.DEFAULT_JOB_TIMEOUT, config.getJobTimeout());
        assertTrue(config.isRetryEnabled());
        assertTrue(config.isTimeoutEnabled());
    }

    @Test
    void testDefaultConstants() {
        assertEquals(2, JobExecutorConfig.DEFAULT_MAX_CONCURRENT_JOBS);
        assertEquals(Duration.ofSeconds(2), JobExecutorConfig.DEFAULT_NO_JOB_WAIT_TIME);
        assertEquals(Duration.ofSeconds(30), JobExecutorConfig.DEFAULT_SHUTDOWN_TIMEOUT);
        assertEquals(100, JobExecutorConfig.DEFAULT_MAX_QUEUE_SIZE);
        assertEquals(3, JobExecutorConfig.DEFAULT_MAX_RETRY_ATTEMPTS);
        assertEquals(Duration.ofSeconds(5), JobExecutorConfig.DEFAULT_RETRY_DELAY);
        assertEquals(Duration.ofMinutes(10), JobExecutorConfig.DEFAULT_JOB_TIMEOUT);
    }

    @Test
    void testBuilderDefaultValues() {
        JobExecutorConfig config = JobExecutorConfig.builder().build();
        
        assertEquals(JobExecutorConfig.DEFAULT_MAX_CONCURRENT_JOBS, config.getMaxConcurrentJobs());
        assertEquals(JobExecutorConfig.DEFAULT_NO_JOB_WAIT_TIME, config.getNoJobWaitTime());
        assertEquals(JobExecutorConfig.DEFAULT_SHUTDOWN_TIMEOUT, config.getShutdownTimeout());
        assertEquals(JobExecutorConfig.DEFAULT_MAX_QUEUE_SIZE, config.getMaxQueueSize());
        assertEquals(JobExecutorConfig.DEFAULT_MAX_RETRY_ATTEMPTS, config.getMaxRetryAttempts());
        assertEquals(JobExecutorConfig.DEFAULT_RETRY_DELAY, config.getRetryDelay());
        assertEquals(JobExecutorConfig.DEFAULT_JOB_TIMEOUT, config.getJobTimeout());
        assertTrue(config.isRetryEnabled());
        assertTrue(config.isTimeoutEnabled());
    }

    @Test
    void testBuilderCustomValues() {
        JobExecutorConfig config = JobExecutorConfig.builder()
                .maxConcurrentJobs(5)
                .noJobWaitTime(Duration.ofSeconds(1))
                .shutdownTimeout(Duration.ofMinutes(1))
                .maxQueueSize(200)
                .maxRetryAttempts(5)
                .retryDelay(Duration.ofSeconds(10))
                .jobTimeout(Duration.ofMinutes(20))
                .enableRetry(false)
                .enableTimeout(false)
                .build();
        
        assertEquals(5, config.getMaxConcurrentJobs());
        assertEquals(Duration.ofSeconds(1), config.getNoJobWaitTime());
        assertEquals(Duration.ofMinutes(1), config.getShutdownTimeout());
        assertEquals(200, config.getMaxQueueSize());
        assertEquals(5, config.getMaxRetryAttempts());
        assertEquals(Duration.ofSeconds(10), config.getRetryDelay());
        assertEquals(Duration.ofMinutes(20), config.getJobTimeout());
        assertFalse(config.isRetryEnabled());
        assertFalse(config.isTimeoutEnabled());
    }

    @Test
    void testBuilderMethodChaining() {
        JobExecutorConfig.Builder builder = JobExecutorConfig.builder();
        
        JobExecutorConfig.Builder returnedBuilder = builder
                .maxConcurrentJobs(3)
                .noJobWaitTime(Duration.ofSeconds(3))
                .shutdownTimeout(Duration.ofSeconds(45))
                .maxQueueSize(150)
                .maxRetryAttempts(2)
                .retryDelay(Duration.ofSeconds(8))
                .jobTimeout(Duration.ofMinutes(15))
                .enableRetry(true)
                .enableTimeout(true);
        
        assertSame(builder, returnedBuilder);
        
        JobExecutorConfig config = builder.build();
        assertEquals(3, config.getMaxConcurrentJobs());
        assertEquals(Duration.ofSeconds(3), config.getNoJobWaitTime());
        assertEquals(Duration.ofSeconds(45), config.getShutdownTimeout());
        assertEquals(150, config.getMaxQueueSize());
        assertEquals(2, config.getMaxRetryAttempts());
        assertEquals(Duration.ofSeconds(8), config.getRetryDelay());
        assertEquals(Duration.ofMinutes(15), config.getJobTimeout());
        assertTrue(config.isRetryEnabled());
        assertTrue(config.isTimeoutEnabled());
    }

    @Test
    void testBuilderValidation_MaxConcurrentJobs() {
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().maxConcurrentJobs(0));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().maxConcurrentJobs(-1));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().maxConcurrentJobs(-10));
    }

    @Test
    void testBuilderValidation_NoJobWaitTime() {
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().noJobWaitTime(null));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().noJobWaitTime(Duration.ofSeconds(-1)));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().noJobWaitTime(Duration.ofMillis(-100)));
    }

    @Test
    void testBuilderValidation_ShutdownTimeout() {
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().shutdownTimeout(null));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().shutdownTimeout(Duration.ofSeconds(-1)));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().shutdownTimeout(Duration.ofMinutes(-1)));
    }

    @Test
    void testBuilderValidation_MaxQueueSize() {
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().maxQueueSize(0));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().maxQueueSize(-1));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().maxQueueSize(-100));
    }

    @Test
    void testBuilderValidation_MaxRetryAttempts() {
        // Zero is allowed (no retries)
        assertDoesNotThrow(() -> JobExecutorConfig.builder().maxRetryAttempts(0));
        
        // Negative values are not allowed
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().maxRetryAttempts(-1));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().maxRetryAttempts(-5));
    }

    @Test
    void testBuilderValidation_RetryDelay() {
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().retryDelay(null));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().retryDelay(Duration.ofSeconds(-1)));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().retryDelay(Duration.ofMillis(-500)));
    }

    @Test
    void testBuilderValidation_JobTimeout() {
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().jobTimeout(null));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().jobTimeout(Duration.ofSeconds(-1)));
        assertThrows(IllegalArgumentException.class, () -> 
            JobExecutorConfig.builder().jobTimeout(Duration.ofMinutes(-5)));
    }

    @Test
    void testBuilderValidation_ZeroDurations() {
        // Zero durations should be allowed
        assertDoesNotThrow(() -> JobExecutorConfig.builder()
            .noJobWaitTime(Duration.ZERO)
            .shutdownTimeout(Duration.ZERO)
            .retryDelay(Duration.ZERO)
            .jobTimeout(Duration.ZERO)
            .build());
    }

    @Test
    void testConfigImmutability() {
        JobExecutorConfig config = JobExecutorConfig.builder()
                .maxConcurrentJobs(5)
                .build();
        
        // Verify that getters return the same values
        assertEquals(5, config.getMaxConcurrentJobs());
        assertEquals(5, config.getMaxConcurrentJobs());
        
        // Config object should be immutable (no setters available)
        // This is enforced by the class design
    }

    @Test
    void testToString() {
        JobExecutorConfig config = JobExecutorConfig.builder()
                .maxConcurrentJobs(3)
                .maxQueueSize(50)
                .enableRetry(false)
                .build();
        
        String toString = config.toString();
        
        assertTrue(toString.contains("JobExecutorConfig"));
        assertTrue(toString.contains("maxConcurrentJobs=3"));
        assertTrue(toString.contains("maxQueueSize=50"));
        assertTrue(toString.contains("enableRetry=false"));
    }

    @Test
    void testMultipleBuilds() {
        JobExecutorConfig.Builder builder = JobExecutorConfig.builder()
                .maxConcurrentJobs(4);
        
        JobExecutorConfig config1 = builder.build();
        JobExecutorConfig config2 = builder.build();
        
        // Both configs should have the same values
        assertEquals(4, config1.getMaxConcurrentJobs());
        assertEquals(4, config2.getMaxConcurrentJobs());
        
        // But they should be different instances
        assertNotSame(config1, config2);
    }

    @Test
    void testBuilderModificationAfterBuild() {
        JobExecutorConfig.Builder builder = JobExecutorConfig.builder()
                .maxConcurrentJobs(2);
        
        JobExecutorConfig config1 = builder.build();
        
        // Modify builder after first build
        builder.maxConcurrentJobs(6);
        JobExecutorConfig config2 = builder.build();
        
        // First config should not be affected
        assertEquals(2, config1.getMaxConcurrentJobs());
        assertEquals(6, config2.getMaxConcurrentJobs());
    }
}
