package co.in.nnj.learn;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutor<JR, JS> implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class.getName());

    public static class Builder<JR, JS> {
        JobProcessor<JR, JS> processor;
        int jobs;
        final List<Runnable> handlers = new ArrayList<>();

        Builder<JR, JS> withProcessor(final JobProcessor<JR, JS> processor) {
            this.processor = processor;
            return this;
        }

        public Builder<JR, JS> withConcurentJobs(final int jobs) {
            this.jobs = jobs;
            return this;
        }

        public Builder<JR, JS> withConcurentHandler(final Runnable handler) {
            handlers.add(handler);
            return this;
        }

        public JobExecutor<JR, JS> build() throws InvalidAttributesException {
            if (jobs <= 0 || processor == null) {
                throw new InvalidAttributesException("Missing mandetory attributes - processor or jobs.");
            }
            return new JobExecutor<JR, JS>(this);
        }
    }

    public static <JR, JS> Builder<JR, JS> builder() {
        return new Builder<>();
    }

    public static interface JobProcessor<JR, JS> {
        boolean init();

        JR retrieveJob();

        JS processJob(JR jobReq);

        boolean isJobFailed(JS status);

        boolean isRetryRequired(JS status);

        boolean isInitRequired(JS status);
    }

    final Semaphore reqRetrievalLock;
    final JobProcessor<JR, JS> jobProcessor;
    final Queue<JR> jobQueue;
    final Queue<JR> retryQueue;
    final List<Runnable> handlers;
    final ExecutorService service;
    boolean init_req = false;
    boolean execJobs = true;

    private JobExecutor(final Builder<JR, JS> builder) {
        final int jobs = builder.jobs + builder.handlers.size();

        this.jobProcessor = builder.processor;
        this.reqRetrievalLock = new Semaphore(jobs);
        this.service = Executors.newFixedThreadPool(jobs);
        this.jobQueue = new ArrayDeque<>();
        this.retryQueue = new ArrayDeque<>();
        this.handlers = builder.handlers;
    }

    private boolean init() {
        if (!init_req) {
            init_req = jobProcessor.init();
        }
        return init_req;
    }

    private void requestInit() {
        init_req = false;
    }

    private void requestRetry(final JR jobReq) {
        synchronized (retryQueue) {
            LOGGER.info("Added job to the retry queue " + jobReq);
            retryQueue.add(jobReq);
        }
    }

    private boolean retrieveJob() throws InterruptedException {
        reqRetrievalLock.acquire();
        JR jobReq = null;
        if (retryQueue.size() > 0) {
            synchronized (retryQueue) {
                jobReq = retryQueue.poll();
            }
        }

        if (jobReq == null) {
            jobReq = jobProcessor.retrieveJob();
        }

        if (jobReq != null) {
            LOGGER.info("Added job to the queue " + jobReq);
            jobQueue.add(jobReq);
            return true;
        }

        TimeUnit.SECONDS.sleep(1);
        return false;
    }

    private void processJob() {
        final JR jobReq = jobQueue.poll();
        if (jobReq == null) {
            return;
        }
        service.execute(new Runnable() {
            @Override
            public void run() {
                JS status;
                try {
                    if (execJobs == false) {
                        requestRetry(jobReq);
                        return;
                    }
                    status = jobProcessor.processJob(jobReq);
                    if (jobProcessor.isJobFailed(status)) {
                        if (jobProcessor.isRetryRequired(status)) {
                            requestRetry(jobReq);
                        }

                        if (jobProcessor.isInitRequired(status)) {
                            requestInit();
                        }
                    }
                } finally {
                    reqRetrievalLock.release();
                }
            }
        });
    }

    public int stop() {
        execJobs = false;
        try {
            service.shutdown();
            if (service.awaitTermination(3, TimeUnit.SECONDS)) {
                service.shutdownNow();
                service.awaitTermination(3, TimeUnit.SECONDS);
            }
        } catch (final InterruptedException e) {
            service.shutdownNow();
            Thread.currentThread().interrupt();
        }
        final List<Runnable> runningJobs = service.shutdownNow();
        return runningJobs.size();
    }

    @Override
    public void run() {
        for (final Runnable runnable : handlers) {
            service.execute(runnable);
        }

        while (execJobs) {
            try {
                if (!init_req) {
                    init();
                    continue;
                }
                if (!retrieveJob()) {
                    continue;
                }
                processJob();
            } catch (final InterruptedException e) {
                break;
            } catch (final Exception e) {
                System.out.println("Error - " + e.getMessage());
            }
        }
    }
}
