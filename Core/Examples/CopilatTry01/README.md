# Job Executor Framework

A Java application framework for executing jobs with controlled concurrency using a queue-based system.

## Overview

This project provides a robust job execution framework with the following key components:

- **JobExecutor**: Manages job execution with configurable concurrency limits
- **JobHandler**: Interface for implementing custom job retrieval and processing logic
- **Job**: Represents a unit of work with status tracking
- **Pull-based Architecture**: JobHandler is responsible for providing jobs when available

## Features

- ✅ **Configurable concurrency** (default: 2 concurrent jobs)
- ✅ **Main thread job retrieval** - No extra threads for job retrieval
- ✅ **Smart polling** - Only retrieves jobs when workers are available
- ✅ **JobStatus-based execution** - Returns OK, FAILED, or WARNING status
- ✅ **Configurable wait time** - 2-second wait when no jobs available
- ✅ **Job lifecycle hooks** (onJobStart, onJobEnd)
- ✅ **External API integration** for job details retrieval
- ✅ **Comprehensive logging and monitoring**
- ✅ **Thread-safe implementation**
- ✅ **Graceful shutdown support**
- ✅ **Resource-efficient architecture**

## Architecture

### Improved Efficient Design

The JobExecutor uses an **optimized architecture** that prevents unnecessary calls to external services:

#### 🎯 **Job Retrieval Strategy:**
- **Main Thread Retrieval**: Calling thread handles all job retrieval
- **Smart Polling**: Only calls `retrieve()` when workers are available
- **Queue Management**: Retrieved jobs are queued for available workers
- **Configurable Backoff**: 2-second wait when no jobs are available

#### ⚡ **JobStatus System:**
- **OK**: Job completed successfully
- **WARNING**: Job completed but with warnings/issues
- **FAILED**: Job failed during execution
- **Intelligent Handling**: Different statuses trigger appropriate responses

#### ⚡ **Benefits:**
- **Prevents API flooding** - No redundant calls when no jobs available
- **Resource efficient** - Minimal external service load
- **Optimal throughput** - Workers stay busy processing, not polling
- **Graceful handling** - Intelligent backoff during low job periods

### Core Components

1. **Job**: Represents a work unit with:
   - Unique ID
   - Type and description
   - Status tracking (PENDING, RUNNING, COMPLETED, FAILED)
   - Timestamps for lifecycle events

2. **JobHandler Interface**: Defines the contract for job processing:
   - `retrieve()`: Fetch a job if one is available (returns null if no job available)
   - `onJobStart(Job)`: Called when job execution begins
   - `execute(Job)`: Main job execution logic, returns JobStatus (OK, FAILED, WARNING)
   - `onJobEnd(Job, boolean, JobStatus, Exception)`: Called when job completes

3. **JobExecutor**: Manages the execution pipeline:
   - **Main thread retrieval** - Uses calling thread for job retrieval
   - **Smart polling** - Only retrieves when workers are available
   - **Worker threads** - Process jobs from internal queue
   - **JobStatus handling** - Processes OK, FAILED, WARNING statuses
   - **Real-time monitoring** - Track queue size and active workers

## Usage

### Basic Usage

```java
// Create a job handler that provides jobs
JobHandler handler = new ExampleJobHandler();

// Create job executor with 2 concurrent workers
JobExecutor executor = new JobExecutor(2, handler);

// Start the executor - it will automatically retrieve and process jobs
executor.start();

// The executor will continuously poll the handler for jobs
// Add jobs to your handler as needed

// Shutdown when done
executor.stop();
```

### Custom Job Handler Implementation

```java
public class MyJobHandler implements JobHandler {
    
    @Override
    public Job retrieve() {
        // Return a job if available, null otherwise
        // This could fetch from a database, API, queue, etc.
        return getNextJobFromYourSource();
    }
    
    @Override
    public void onJobStart(Job job) {
        // Initialize resources, log start, etc.
    }
    
    @Override
    public JobStatus execute(Job job) throws Exception {
        // Your custom job logic here
        
        if (someCondition) {
            return JobStatus.OK;
        } else if (partialSuccess) {
            return JobStatus.WARNING;
        } else {
            return JobStatus.FAILED;
        }
    }
    
    @Override
    public void onJobEnd(Job job, boolean success, JobStatus executionStatus, Exception error) {
        // Handle different statuses
        if (executionStatus == JobStatus.OK) {
            // Success handling
        } else if (executionStatus == JobStatus.WARNING) {
            // Warning handling
        } else {
            // Failure handling
        }
    }
}
```

## Project Structure

```
src/main/java/com/example/jobexecutor/
├── JobExecutorApp.java                 # Main application
├── executor/
│   └── JobExecutor.java               # Core job execution engine
├── handler/
│   ├── JobHandler.java                # Job handler interface
│   └── impl/
│       ├── ExampleJobHandler.java     # Full-featured example
│       └── SimpleJobHandler.java      # Simple example
└── model/
    ├── Job.java                       # Job data model
    └── JobStatus.java                 # Job status enumeration
```

## Running the Application

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Build and Run

```bash
# Compile the project
mvn clean compile

# Run the main application
mvn exec:java -Dexec.mainClass="com.example.jobexecutor.JobExecutorApp"

# Or package and run
mvn clean package
java -jar target/job-executor-1.0-SNAPSHOT.jar
```

## Configuration

### Concurrency
Adjust the number of concurrent workers when creating the JobExecutor:

```java
JobExecutor executor = new JobExecutor(5, handler); // 5 concurrent jobs
```

### Logging
Logging is configured via `logback.xml`. Logs are written to:
- Console (INFO level)
- File: `logs/job-executor.log`

## Example Job Types

The framework includes examples for different job types:

- **data_processing**: Data processing tasks
- **report_generation**: Report generation jobs
- **file_cleanup**: File maintenance tasks  
- **notification**: Sending notifications
- **custom_task**: Generic custom jobs

## Dependencies

- **Jackson**: JSON processing for API responses
- **Apache HttpClient**: HTTP client for external API calls
- **SLF4J + Logback**: Logging framework

## Thread Safety

The JobExecutor is designed to be thread-safe:
- Uses `BlockingQueue` for job queuing
- Employs `ExecutorService` for thread management
- Uses `AtomicBoolean` for state management

## Error Handling

- Failed jobs are marked with `FAILED` status
- Exceptions are logged and passed to `onJobEnd` handler
- Graceful degradation when external APIs are unavailable
- Proper resource cleanup on shutdown

## Extending the Framework

To create your own job handler:

1. Implement the `JobHandler` interface
2. Override the four required methods
3. Add your custom business logic
4. Handle errors appropriately
5. Use the handler with JobExecutor

## Best Practices

1. **Resource Management**: Always close resources in `onJobEnd`
2. **Error Handling**: Implement proper exception handling in `execute`
3. **Logging**: Use structured logging for better observability
4. **Timeouts**: Implement timeouts for external API calls
5. **Monitoring**: Track job metrics and queue sizes
6. **Testing**: Unit test your JobHandler implementations

## License

This is a sample project for demonstration purposes.
