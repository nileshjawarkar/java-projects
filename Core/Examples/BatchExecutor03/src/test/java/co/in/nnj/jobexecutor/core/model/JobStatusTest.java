package co.in.nnj.jobexecutor.core.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for JobStatus enum
 */
class JobStatusTest {

    @Test
    void testJobStatusValues() {
        // Test all enum values exist
        JobStatus[] statuses = JobStatus.values();
        assertEquals(7, statuses.length);
        
        // Test specific values
        assertEquals(JobStatus.PENDING, JobStatus.valueOf("PENDING"));
        assertEquals(JobStatus.RUNNING, JobStatus.valueOf("RUNNING"));
        assertEquals(JobStatus.COMPLETED, JobStatus.valueOf("COMPLETED"));
        assertEquals(JobStatus.FAILED, JobStatus.valueOf("FAILED"));
        assertEquals(JobStatus.OK, JobStatus.valueOf("OK"));
        assertEquals(JobStatus.WARNING, JobStatus.valueOf("WARNING"));
        assertEquals(JobStatus.INIT_REQUIRED, JobStatus.valueOf("INIT_REQUIRED"));
    }

    @Test
    void testJobStatusOrdinal() {
        // Test ordinal values for consistency
        assertEquals(0, JobStatus.PENDING.ordinal());
        assertEquals(1, JobStatus.RUNNING.ordinal());
        assertEquals(2, JobStatus.COMPLETED.ordinal());
        assertEquals(3, JobStatus.FAILED.ordinal());
        assertEquals(4, JobStatus.OK.ordinal());
        assertEquals(5, JobStatus.WARNING.ordinal());
        assertEquals(6, JobStatus.INIT_REQUIRED.ordinal());
    }

    @Test
    void testJobStatusToString() {
        // Test string representation
        assertEquals("PENDING", JobStatus.PENDING.toString());
        assertEquals("RUNNING", JobStatus.RUNNING.toString());
        assertEquals("COMPLETED", JobStatus.COMPLETED.toString());
        assertEquals("FAILED", JobStatus.FAILED.toString());
        assertEquals("OK", JobStatus.OK.toString());
        assertEquals("WARNING", JobStatus.WARNING.toString());
        assertEquals("INIT_REQUIRED", JobStatus.INIT_REQUIRED.toString());
    }

    @Test
    void testJobStatusEquality() {
        // Test enum equality
        assertEquals(JobStatus.PENDING, JobStatus.PENDING);
        assertNotEquals(JobStatus.PENDING, JobStatus.RUNNING);
        assertNotEquals(JobStatus.OK, JobStatus.WARNING);
        assertEquals(JobStatus.INIT_REQUIRED, JobStatus.INIT_REQUIRED);
    }

    @Test
    void testInvalidJobStatus() {
        // Test invalid enum value throws exception
        assertThrows(IllegalArgumentException.class, () -> JobStatus.valueOf("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> JobStatus.valueOf(""));
        assertThrows(NullPointerException.class, () -> JobStatus.valueOf(null));
    }
}
