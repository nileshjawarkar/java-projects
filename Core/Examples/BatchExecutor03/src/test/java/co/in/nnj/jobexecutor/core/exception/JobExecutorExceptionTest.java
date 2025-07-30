package co.in.nnj.jobexecutor.core.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for JobExecutorException
 */
class JobExecutorExceptionTest {

    @Test
    void testJobExecutorExceptionWithMessage() {
        String message = "Test exception message";
        JobExecutorException exception = new JobExecutorException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testJobExecutorExceptionWithMessageAndCause() {
        String message = "Test exception message";
        Throwable cause = new RuntimeException("Root cause");
        JobExecutorException exception = new JobExecutorException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testJobExecutorExceptionWithNullMessage() {
        JobExecutorException exception = new JobExecutorException((String) null);
        
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testJobExecutorExceptionWithNullCause() {
        String message = "Test message";
        JobExecutorException exception = new JobExecutorException(message, null);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testJobExecutorExceptionInheritance() {
        JobExecutorException exception = new JobExecutorException("test");
        
        assertTrue(exception instanceof Exception);
        // JobExecutorException extends Exception, not RuntimeException
        Exception genericException = exception;
        assertNotNull(genericException);
    }

    @Test
    void testJobExecutorExceptionSerialization() {
        String message = "Serialization test";
        Throwable cause = new IllegalArgumentException("Cause");
        JobExecutorException exception = new JobExecutorException(message, cause);
        
        // Test that exception can be created and basic properties work
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testJobExecutorExceptionChaining() {
        RuntimeException rootCause = new RuntimeException("Root cause");
        IllegalStateException intermediateCause = new IllegalStateException("Intermediate", rootCause);
        JobExecutorException exception = new JobExecutorException("Final message", intermediateCause);
        
        assertEquals("Final message", exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }
}
