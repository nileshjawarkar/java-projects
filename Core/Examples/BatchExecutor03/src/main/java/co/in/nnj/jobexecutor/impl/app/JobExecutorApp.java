package co.in.nnj.jobexecutor.impl.app;

import co.in.nnj.jobexecutor.core.executor.JobExecutor;
import co.in.nnj.jobexecutor.core.config.JobExecutorConfig;
import co.in.nnj.jobexecutor.impl.handler.ExampleJobHandler;
import co.in.nnj.jobexecutor.impl.job.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class demonstrating the usage of JobExecutor
 */
public class JobExecutorApp {
    private static final Logger logger = LoggerFactory.getLogger(JobExecutorApp.class);
    
    public static void main(String[] args) {
        logger.info("Starting JobExecutor Demo Application");
        
        // Create job handler
        ExampleJobHandler jobHandler = new ExampleJobHandler();
        
        // Add some additional jobs to the handler if needed
        addMoreJobsToHandler(jobHandler);
        
        // Create job executor with maximum 2 concurrent jobs
        JobExecutor jobExecutor = new JobExecutor(2, jobHandler);
        
        // Set up shutdown hook for graceful termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown hook triggered, stopping JobExecutor...");
            jobExecutor.stop();
        }));
        
        // Create a separate thread to monitor and eventually stop the executor
        Thread monitorThread = new Thread(() -> {
            try {
                // Monitor for 15 seconds
                for (int i = 0; i < 15; i++) {
                    Thread.sleep(1000);
                    logger.info("Status - Queue size: {}, Active workers: {}", 
                               jobExecutor.getQueueSize(), jobExecutor.getActiveWorkers());
                }
                
                logger.info("Stopping job processing...");
                jobExecutor.stop();
                
            } catch (InterruptedException e) {
                logger.info("Monitor thread interrupted");
                Thread.currentThread().interrupt();
            }
        });
        
        try {
            // Start monitoring in background
            monitorThread.start();
            
            // Start the job executor - this will block and run job retrieval in main thread
            logger.info("JobExecutor starting. Main thread will handle job retrieval...");
            jobExecutor.start();
            
        } finally {
            // Ensure resources are cleaned up
            logger.info("Application shutdown complete");
        }
    }
    
    private static void addMoreJobsToHandler(ExampleJobHandler jobHandler) {
        // Add some additional jobs to demonstrate continuous processing
        jobHandler.addJob(new SimpleJob("data_processing", "Process evening batch data"));
        jobHandler.addJob(new SimpleJob("notification", "Send daily summary notifications"));
        jobHandler.addJob(new SimpleJob("report_generation", "Generate weekly performance report"));
        
        logger.info("Added additional jobs to the handler");
    }
}
