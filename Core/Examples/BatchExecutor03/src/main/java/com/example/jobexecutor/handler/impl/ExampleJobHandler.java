package com.example.jobexecutor.handler.impl;

import com.example.jobexecutor.handler.JobHandler;
import com.example.jobexecutor.model.Job;
import com.example.jobexecutor.model.JobStatus;
import com.example.jobexecutor.model.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Example implementation of JobHandler for demonstration purposes.
 * This handler manages a queue of jobs and provides basic job processing logic.
 */
public class ExampleJobHandler implements JobHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExampleJobHandler.class);
    
    private final BlockingQueue<Job> availableJobs;
    
    public ExampleJobHandler() {
        this.availableJobs = new LinkedBlockingQueue<>();
        initializeSampleJobs();
        logger.info("ExampleJobHandler initialized with {} jobs", availableJobs.size());
    }
    
    /**
     * Initialize the handler with some sample jobs for demonstration
     */
    private void initializeSampleJobs() {
        availableJobs.offer(new SimpleJob("data_processing", "Process daily sales data"));
        availableJobs.offer(new SimpleJob("report_generation", "Generate monthly financial report"));
        availableJobs.offer(new SimpleJob("file_cleanup", "Clean up temporary files older than 7 days"));
        availableJobs.offer(new SimpleJob("notification", "Send status notifications to administrators"));
        availableJobs.offer(new SimpleJob("data_processing", "Process customer feedback data"));
        availableJobs.offer(new SimpleJob("custom_task", "Execute custom business logic"));
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
        } else {
            logger.debug("No jobs available");
        }
        
        return job;
    }
    
    @Override
    public void onJobStart(Job job) {
        logger.info("Job {} is starting execution", job.getId());
    }
    
    // The execute method is now a default method in JobHandler that calls job.execute()
    // So we don't need to override it unless we want custom logic
    
    @Override
    public void onJobEnd(Job job, boolean success, JobStatus executionStatus, Exception error) {
        if (success) {
            if (executionStatus == JobStatus.WARNING) {
                logger.warn("Job {} completed with warnings", job.getId());
            } else {
                logger.info("Job {} completed successfully", job.getId());
            }
        } else {
            logger.error("Job {} failed", job.getId(), error);
        }
    }
}
