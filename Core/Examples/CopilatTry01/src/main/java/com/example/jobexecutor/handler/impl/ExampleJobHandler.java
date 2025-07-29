package com.example.jobexecutor.handler.impl;

import com.example.jobexecutor.handler.JobHandler;
import com.example.jobexecutor.model.Job;
import com.example.jobexecutor.model.JobStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Example implementation of JobHandler that demonstrates how to:
 * - Retrieve jobs from an external source or internal queue
 * - Handle job lifecycle events
 * - Execute job-specific logic
 */
public class ExampleJobHandler implements JobHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExampleJobHandler.class);
    
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;
    private final BlockingQueue<Job> availableJobs;
    
    public ExampleJobHandler() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
        this.availableJobs = new LinkedBlockingQueue<>();
        
        // Initialize with some sample jobs
        initializeSampleJobs();
    }
    
    /**
     * Initialize the handler with some sample jobs for demonstration
     */
    private void initializeSampleJobs() {
        availableJobs.offer(new Job("data_processing", "Process daily sales data"));
        availableJobs.offer(new Job("report_generation", "Generate monthly financial report"));
        availableJobs.offer(new Job("file_cleanup", "Clean up temporary files older than 7 days"));
        availableJobs.offer(new Job("notification", "Send status notifications to administrators"));
        availableJobs.offer(new Job("data_processing", "Process customer feedback data"));
        availableJobs.offer(new Job("custom_task", "Execute custom business logic"));
    }
    
    /**
     * Add a job to the available jobs queue
     */
    public void addJob(Job job) {
        availableJobs.offer(job);
        logger.info("Added job {} to available jobs queue", job.getId());
    }
    
    @Override
    public Job retrieve() {
        logger.debug("Checking for available jobs...");
        
        Job job = availableJobs.poll();
        if (job != null) {
            logger.info("Retrieved job {} from queue", job.getId());
            
            // Optionally fetch additional job details from external API
            try {
                fetchJobDetailsFromAPI(job);
            } catch (Exception e) {
                logger.warn("Failed to fetch additional job details for {}: {}", job.getId(), e.getMessage());
                // Continue with the job even if external details fetch fails
            }
        } else {
            logger.debug("No jobs available");
        }
        
        return job;
    }
    
    /**
     * Fetch additional job details from external API
     */
    private void fetchJobDetailsFromAPI(Job job) throws IOException {
        // Simulate calling an external API to get job details
        String apiUrl = "https://jsonplaceholder.typicode.com/posts/1";
        
        HttpGet request = new HttpGet(apiUrl);
        request.setHeader("Accept", "application/json");
        
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.debug("Fetched additional job details from API: {}", responseBody);
                
                // Parse the response and potentially update job details
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String title = jsonNode.get("title").asText();
                
                logger.info("Job {} has additional context: {}", job.getId(), title);
            } else {
                logger.warn("Failed to fetch job details from API, status: {}", 
                           response.getStatusLine().getStatusCode());
            }
        }
    }
    
    @Override
    public void onJobStart(Job job) {
        logger.info("Job {} is starting execution - Type: {}, Description: {}", 
                   job.getId(), job.getType(), job.getDescription());
        
        // Here you could perform initialization tasks such as:
        // - Setting up resources
        // - Sending notifications
        // - Updating external systems
        // - Logging to audit trail
    }
    
    @Override
    public JobStatus execute(Job job) throws Exception {
        logger.info("Executing job {} of type {}", job.getId(), job.getType());
        
        // Perform different actions based on job type
        switch (job.getType().toLowerCase()) {
            case "data_processing":
                return executeDataProcessingJob(job);
            case "report_generation":
                return executeReportGenerationJob(job);
            case "file_cleanup":
                return executeFileCleanupJob(job);
            case "notification":
                return executeNotificationJob(job);
            default:
                return executeDefaultJob(job);
        }
    }
    
    @Override
    public void onJobEnd(Job job, boolean success, JobStatus executionStatus, Exception error) {
        if (success) {
            if (executionStatus == JobStatus.OK) {
                logger.info("Job {} completed successfully", job.getId());
            } else if (executionStatus == JobStatus.WARNING) {
                logger.warn("Job {} completed with warnings", job.getId());
            }
            
            // Handle successful completion:
            // - Send success notifications
            // - Update external systems
            // - Trigger dependent jobs
            // - Clean up resources
            
        } else {
            logger.error("Job {} failed with error: {}", job.getId(), 
                        error != null ? error.getMessage() : "Unknown error");
            
            // Handle failure:
            // - Send failure notifications
            // - Log errors for analysis
            // - Trigger retry logic if applicable
            // - Clean up partial results
        }
        
        // Common cleanup tasks regardless of success/failure
        performCommonCleanup(job);
    }
    
    /**
     * Executes a data processing job
     */
    private JobStatus executeDataProcessingJob(Job job) throws Exception {
        logger.info("Processing data for job {}", job.getId());
        
        // Simulate data processing work
        Thread.sleep(2000); // Simulate work
        
        // Simulate different outcomes based on job ID for demonstration
        if (job.getId().hashCode() % 10 == 0) {
            logger.warn("Data processing completed with warnings for job {}", job.getId());
            return JobStatus.WARNING;
        }
        
        logger.info("Data processing completed successfully - processed 1000 records");
        return JobStatus.OK;
    }
    
    /**
     * Executes a report generation job
     */
    private JobStatus executeReportGenerationJob(Job job) throws Exception {
        logger.info("Generating report for job {}", job.getId());
        
        // Simulate report generation
        Thread.sleep(3000); // Simulate work
        
        logger.info("Report generated successfully - report_{}.pdf", job.getId());
        return JobStatus.OK;
    }
    
    /**
     * Executes a file cleanup job
     */
    private JobStatus executeFileCleanupJob(Job job) throws Exception {
        logger.info("Cleaning up files for job {}", job.getId());
        
        // Simulate file cleanup
        Thread.sleep(1000); // Simulate work
        
        // Simulate partial cleanup scenario
        if (job.getId().hashCode() % 7 == 0) {
            logger.warn("Some files could not be cleaned up for job {}", job.getId());
            return JobStatus.WARNING;
        }
        
        logger.info("Cleaned up 25 temporary files");
        return JobStatus.OK;
    }
    
    /**
     * Executes a notification job
     */
    private JobStatus executeNotificationJob(Job job) throws Exception {
        logger.info("Sending notifications for job {}", job.getId());
        
        // Simulate sending notifications
        Thread.sleep(500); // Simulate work
        
        logger.info("Notifications sent to 10 recipients");
        return JobStatus.OK;
    }
    
    /**
     * Executes a default job when type is not recognized
     */
    private JobStatus executeDefaultJob(Job job) throws Exception {
        logger.info("Executing default job logic for {}", job.getId());
        
        // Simulate generic work
        Thread.sleep(1500); // Simulate work
        
        logger.info("Generic job completed - {}", job.getDescription());
        return JobStatus.OK;
    }
    
    /**
     * Performs common cleanup tasks
     */
    private void performCommonCleanup(Job job) {
        logger.debug("Performing cleanup for job {}", job.getId());
        
        // Common cleanup tasks:
        // - Release resources
        // - Update metrics
        // - Archive job data
    }
    
    /**
     * Cleanup method to close HTTP client
     */
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            logger.error("Error closing HTTP client", e);
        }
    }
}
