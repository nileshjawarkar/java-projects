package co.in.nnj.jobexecutor.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for QueuedJob class
 */
class QueuedJobTest {

    @Mock
    private Job mockJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockJob.getId()).thenReturn("test-job-123");
    }

    @Test
    void testQueuedJobCreation() {
        // Test default constructor
        QueuedJob queuedJob = new QueuedJob(mockJob);
        
        assertEquals("test-job-123", queuedJob.getId());
        assertEquals(JobStatus.PENDING, queuedJob.getStatus());
        assertEquals(0, queuedJob.getRetryCount());
        assertEquals(3, queuedJob.getMaxRetries());
        assertNull(queuedJob.getResult());
        assertNull(queuedJob.getErrorMessage());
        assertNull(queuedJob.getStartedAt());
        assertNull(queuedJob.getCompletedAt());
    }

    @Test
    void testQueuedJobCreationWithMaxRetries() {
        // Test constructor with custom max retries
        QueuedJob queuedJob = new QueuedJob(mockJob, 5);
        
        assertEquals("test-job-123", queuedJob.getId());
        assertEquals(JobStatus.PENDING, queuedJob.getStatus());
        assertEquals(0, queuedJob.getRetryCount());
        assertEquals(5, queuedJob.getMaxRetries());
    }

    @Test
    void testQueuedJobCreationWithNullJob() {
        // Test null job throws exception
        assertThrows(NullPointerException.class, () -> new QueuedJob(null));
        assertThrows(NullPointerException.class, () -> new QueuedJob(null, 3));
    }

    @Test
    void testQueuedJobCreationWithNegativeMaxRetries() {
        // Test negative max retries throws exception
        assertThrows(IllegalArgumentException.class, () -> new QueuedJob(mockJob, -1));
        assertThrows(IllegalArgumentException.class, () -> new QueuedJob(mockJob, -5));
    }

    @Test
    void testQueuedJobCreationWithZeroMaxRetries() {
        // Test zero max retries is allowed
        QueuedJob queuedJob = new QueuedJob(mockJob, 0);
        assertEquals(0, queuedJob.getMaxRetries());
    }

    @Test
    void testStatusManagement() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        
        // Test status changes
        queuedJob.setStatus(JobStatus.RUNNING);
        assertEquals(JobStatus.RUNNING, queuedJob.getStatus());
        
        queuedJob.setStatus(JobStatus.COMPLETED);
        assertEquals(JobStatus.COMPLETED, queuedJob.getStatus());
        
        queuedJob.setStatus(JobStatus.FAILED);
        assertEquals(JobStatus.FAILED, queuedJob.getStatus());
    }

    @Test
    void testStatusManagementWithNull() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        
        // Test null status throws exception
        assertThrows(NullPointerException.class, () -> queuedJob.setStatus(null));
    }

    @Test
    void testResultManagement() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        
        // Test result setting
        queuedJob.setResult("Success");
        assertEquals("Success", queuedJob.getResult());
        
        queuedJob.setResult("Failed with error");
        assertEquals("Failed with error", queuedJob.getResult());
        
        // Test null result is allowed
        queuedJob.setResult(null);
        assertNull(queuedJob.getResult());
    }

    @Test
    void testErrorMessageManagement() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        
        // Test error message setting
        queuedJob.setErrorMessage("Test error");
        assertEquals("Test error", queuedJob.getErrorMessage());
        
        // Test null error message is allowed
        queuedJob.setErrorMessage(null);
        assertNull(queuedJob.getErrorMessage());
    }

    @Test
    void testTimestampManagement() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime completedTime = startTime.plusMinutes(5);
        
        // Test timestamp setting
        queuedJob.setStartedAt(startTime);
        assertEquals(startTime, queuedJob.getStartedAt());
        
        queuedJob.setCompletedAt(completedTime);
        assertEquals(completedTime, queuedJob.getCompletedAt());
        
        // Test null timestamps are allowed
        queuedJob.setStartedAt(null);
        queuedJob.setCompletedAt(null);
        assertNull(queuedJob.getStartedAt());
        assertNull(queuedJob.getCompletedAt());
    }

    @Test
    void testRetryManagement() {
        QueuedJob queuedJob = new QueuedJob(mockJob, 3);
        
        // Test initial retry state
        assertEquals(0, queuedJob.getRetryCount());
        assertEquals(3, queuedJob.getMaxRetries());
        
        // Initially PENDING, so cannot retry
        assertEquals(JobStatus.PENDING, queuedJob.getStatus());
        assertFalse(queuedJob.canRetry());
        
        // Set to FAILED to enable retry
        queuedJob.setStatus(JobStatus.FAILED);
        assertTrue(queuedJob.canRetry());
        
        // Test retry count increment through prepareForRetry
        queuedJob.prepareForRetry();
        assertEquals(1, queuedJob.getRetryCount());
        assertEquals(JobStatus.PENDING, queuedJob.getStatus()); // prepareForRetry sets to PENDING
        
        // Set to FAILED again to test next retry
        queuedJob.setStatus(JobStatus.FAILED);
        assertTrue(queuedJob.canRetry());
        
        queuedJob.prepareForRetry();
        assertEquals(2, queuedJob.getRetryCount());
        assertEquals(JobStatus.PENDING, queuedJob.getStatus());
        
        // Set to FAILED again to test final retry
        queuedJob.setStatus(JobStatus.FAILED);
        assertTrue(queuedJob.canRetry());
        
        queuedJob.prepareForRetry();
        assertEquals(3, queuedJob.getRetryCount());
        assertEquals(JobStatus.PENDING, queuedJob.getStatus());
        
        // Now at max retries, so even if FAILED, cannot retry
        queuedJob.setStatus(JobStatus.FAILED);
        assertFalse(queuedJob.canRetry());
    }

    @Test
    void testRetryWithZeroMaxRetries() {
        QueuedJob queuedJob = new QueuedJob(mockJob, 0);
        
        // Test retry with zero max retries
        assertEquals(0, queuedJob.getRetryCount());
        assertEquals(0, queuedJob.getMaxRetries());
        assertFalse(queuedJob.canRetry());
        
        // Attempting to prepare for retry should throw exception
        assertThrows(IllegalStateException.class, () -> queuedJob.prepareForRetry());
    }

    @Test
    void testRetryTimestampManagement() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        long currentTime = System.currentTimeMillis();
        
        // Initially PENDING, so not ready for retry regardless of timestamp  
        assertFalse(queuedJob.isReadyForRetry(currentTime));
        
        // Set to FAILED to enable retry checks
        queuedJob.setStatus(JobStatus.FAILED);
        
        // Test initial retry timestamp (should be 0, so ready)  
        assertTrue(queuedJob.isReadyForRetry(currentTime));
        
        // Test setting retry timestamp in future
        long futureTime = currentTime + 5000; // 5 seconds in future
        queuedJob.setRetryTimestamp(futureTime);
        assertFalse(queuedJob.isReadyForRetry(currentTime));
        assertTrue(queuedJob.isReadyForRetry(futureTime + 1000)); // Past the retry time
        
        // Test past timestamp
        long pastTime = currentTime - 1000; // 1 second ago
        queuedJob.setRetryTimestamp(pastTime);
        assertTrue(queuedJob.isReadyForRetry(currentTime));
        
        // Test zero timestamp
        queuedJob.setRetryTimestamp(0);
        assertTrue(queuedJob.isReadyForRetry(currentTime));
    }

    @Test
    void testPrepareForRetry() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        long currentTime = System.currentTimeMillis();
        
        // Set some state
        queuedJob.setStatus(JobStatus.FAILED);
        queuedJob.setResult("Failed");
        queuedJob.setErrorMessage("Error occurred");
        queuedJob.setRetryTimestamp(currentTime + 5000);
        
        // Verify ready for retry before prepare (job is FAILED and retryTimestamp is future)
        assertTrue(queuedJob.canRetry());
        assertFalse(queuedJob.isReadyForRetry(currentTime)); // not ready yet due to future timestamp
        
        // Prepare for retry
        queuedJob.prepareForRetry();
        
        // Verify state is reset
        assertEquals(JobStatus.PENDING, queuedJob.getStatus());
        assertNull(queuedJob.getResult());
        assertNull(queuedJob.getErrorMessage());
        assertEquals(0, queuedJob.getRetryTimestamp()); // Should be reset to 0
        assertEquals(1, queuedJob.getRetryCount()); // Should be incremented
        
        // After prepareForRetry, job is PENDING so not ready for retry
        // (it's ready for execution, not retry)
        assertFalse(queuedJob.canRetry());
        assertFalse(queuedJob.isReadyForRetry(currentTime));
    }

    @Test
    void testWrappedJobAccess() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        
        // Test wrapped job access
        assertEquals(mockJob, queuedJob.getWrappedJob());
        
        // Test that getId() delegates to wrapped job
        String jobId = queuedJob.getId();
        assertEquals("test-job-123", jobId);
        
        // Verify getId() was called on the wrapped job
        verify(mockJob, atLeastOnce()).getId();
    }

    @Test
    void testToString() {
        QueuedJob queuedJob = new QueuedJob(mockJob);
        String toString = queuedJob.toString();
        
        // Test toString contains job ID
        assertTrue(toString.contains("test-job-123"));
        assertTrue(toString.contains("QueuedJob"));
    }

    @Test
    void testEqualsAndHashCode() {
        QueuedJob queuedJob1 = new QueuedJob(mockJob);
        QueuedJob queuedJob2 = new QueuedJob(mockJob);
        
        // Create another mock job with different ID
        Job mockJob2 = mock(Job.class);
        when(mockJob2.getId()).thenReturn("different-job-456");
        QueuedJob queuedJob3 = new QueuedJob(mockJob2);
        
        // Test equality based on job ID
        assertEquals(queuedJob1, queuedJob2);
        assertNotEquals(queuedJob1, queuedJob3);
        
        // Test hash code consistency
        assertEquals(queuedJob1.hashCode(), queuedJob2.hashCode());
        assertNotEquals(queuedJob1.hashCode(), queuedJob3.hashCode());
        
        // Test null and different class
        assertNotEquals(queuedJob1, null);
        assertNotEquals(queuedJob1, "string");
    }
}
