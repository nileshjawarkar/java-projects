package co.in.nnj.learn.executor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutor<JR> implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class.getName());

    public static class Builder<JR> {
        JobHandler<JR> handler;
        int jobs;
        final List<Runnable> handlers = new ArrayList<>();
        public int maxJobTime = 600;
        public int maxJobRetry = 3;
        private List<JR> existingJobs = null;
        private Builder() {
        }

        public Builder<JR> withProcessor(final JobHandler<JR> handler) {
            this.handler = handler;
            return this;
        }

        public Builder<JR> withConcurentJobs(final int jobs) {
            this.jobs = jobs;
            return this;
        }

        public Builder<JR> withMaxRetry(final int maxJobRetry) {
            this.maxJobRetry = maxJobRetry;
            return this;
        }

        public Builder<JR> withMaxJobTime(final int timeInSec) {
            this.maxJobTime = timeInSec;
            return this;
        }

        public Builder<JR> withConcurentHandler(final Runnable handler) {
            handlers.add(handler);
            return this;
        }

        public Builder<JR> withPreviousJobs(final List<JR> jobs) {
            this.existingJobs = jobs;
            return this;
        }

        public JobExecutor<JR> build() throws InvalidAttributesException {
            if (jobs <= 0 || handler == null) {
                throw new InvalidAttributesException("Missing mandatory attributes - processor or jobs.");
            }
            return new JobExecutor<>(this);
        }
    }

    public static <JR> Builder<JR> builder() {
        return new Builder<JR>();
    }

    private final JobHandler<JR> jobHandler;
    private final Queue<QueuedJob<JR>> jobQueue;
    private final Queue<QueuedJob<JR>> retryQueue;
    private final List<Runnable> handlers;
    private final ExecutorService service;
    private final int concurrentJobsAllowed;
    private final int MAX_JOBTIME_IN_SEC;
    private final int MAX_JOBRETRY_COUNT;

    boolean init_req = false;
    boolean execJobs = true;
    long completedJobs = 0;

    private JobExecutor(final Builder<JR> builder) {
        final int jobs = builder.jobs + builder.handlers.size();
        this.MAX_JOBRETRY_COUNT = builder.maxJobRetry;
        this.MAX_JOBTIME_IN_SEC = builder.maxJobTime;
        this.concurrentJobsAllowed = builder.jobs;
        this.jobHandler = builder.handler;
        this.service = Executors.newFixedThreadPool(jobs);
        this.jobQueue = new ArrayDeque<>();
        this.retryQueue = new ArrayDeque<>();

        if(builder.existingJobs != null && builder.existingJobs.size() > 0) {
            for (final JR job : builder.existingJobs) {
               retryQueue.add(new QueuedJob<JR>(job));
            }
        }
        this.handlers = builder.handlers;
    }

    private boolean init() {
        boolean init_statues = false;
        if (init_req) {
            init_statues = jobHandler.init();
            init_req = !init_statues;
        }
        return init_statues;
    }

    private void requestInit() {
        init_req = true;
    }

    private void requestRetry(final QueuedJob<JR> qJob) {
        synchronized (retryQueue) {
            LOGGER.info("Added job to the retry queue " + qJob.jobReq);
            retryQueue.add(new QueuedJob<JR>(qJob.jobReq, qJob.retryNumber + 1));
        }
    }

    public List<JR> getJobs() throws Exception {
        if (execJobs) {
            throw new Exception("Invalid state - This api must be called after stopping the executor.");
        }
        final List<JR> jobs = new ArrayList<>();
        for (final QueuedJob<JR> qJob : jobQueue) {
            jobs.add(qJob.jobReq);
        }
        for (final QueuedJob<JR> qJob : retryQueue) {
            jobs.add(qJob.jobReq);
        }
        return jobs;
    }

    private boolean retrieveJob() throws InterruptedException {
        QueuedJob<JR> qJob = null;
        if (retryQueue.size() > 0) {
            synchronized (retryQueue) {
                qJob = retryQueue.poll();
            }
        }

        if (qJob == null) {
            final JR jobReq = jobHandler.retrieveJob();
            if (jobReq != null) {
                qJob = new QueuedJob<JR>(jobReq);
            }
        }

        if (qJob != null) {
            LOGGER.info("Added job to the queue " + qJob.jobReq);
            jobQueue.add(qJob);
            return true;
        }
        TimeUnit.SECONDS.sleep(1);
        return false;
    }

    /* List of inwork jobs */
    private final List<InworkJob<JR>> inworkJobs = new ArrayList<>();
    private boolean stopped = false;

    /* Add job to inworkJobs queue */
    private void addJobToInworkQueue(final QueuedJob<JR> qJob, final Future<JobStatus> future) {
        synchronized (inworkJobs) {
            inworkJobs.add(new InworkJob<>(qJob.jobReq, future, qJob.retryNumber + 1));
        }
    }

    private void cancleInworkJobs() {
        final Iterator<InworkJob<JR>> iterator = inworkJobs.iterator();
        while (iterator.hasNext()) {
            final InworkJob<JR> job = iterator.next();
            try {
                job.future.cancel(true);
            } catch (final CancellationException e) {
                System.out.println("Job canceled [" + job.jobReq + "], added to retry queue.");
                requestRetry(job);
            }
        }
    }

    /*
     * checkJobStatus checks for completion of jobs.
     * If completed,
     * 1) Check its status and if required add it to retry queue, or request init
     * 2) remove it from queue.
     * If time-out limit reached, cancel the job and remove it from queue.
     * Returns no of job in inwork queue.
     */
    private int checkJobStatus() throws InterruptedException, ExecutionException {
        synchronized (inworkJobs) {
            final Iterator<InworkJob<JR>> iterator = inworkJobs.iterator();
            while (iterator.hasNext()) {
                final InworkJob<JR> job = iterator.next();
                if (job.isDone()) {
                    try {
                        final JobStatus status = job.getStatus();
                        if (status != JobStatus.SUCCEEDED) {
                            if (status == JobStatus.CANCELED) {
                                System.out.println("Job already canceled [" + job.jobReq + "]");
                            } else if (job.retryNumber < MAX_JOBRETRY_COUNT) {
                                if (status == JobStatus.FAILED_INIT_RETRY_REQ) {
                                    requestInit();
                                    requestRetry(job);
                                } else if (status == JobStatus.FAILED_RETRY_REQ) {
                                    requestRetry(job);
                                }
                            }
                        } else {
                            completedJobs++;
                        }
                    } finally {
                        iterator.remove();
                    }
                } else if (job.isTimeOutReached(MAX_JOBTIME_IN_SEC)) {
                    System.out.println("Timeout for job [" + job.jobReq + "]");
                    iterator.remove();
                    try {
                        job.future.cancel(true);
                    } catch (final CancellationException e) {
                    }
                }
            }
        }
        return inworkJobs.size();
    }

    /*
     * Execute job using thread-pool
     * This API uses Callable implementation to submit the job and
     * uses the future return to monitor its status. This is specifically done,
     * to check if some job taking time more than it should. In that case, after
     * time-out limit
     * we can cancel that job.
     */
    private void processJob() {
        if (execJobs == false) {
            return;
        }

        final QueuedJob<JR> qJob = jobQueue.poll();
        if (qJob == null) {
            return;
        }
        try {
            final Future<JobStatus> future = service.submit(new Callable<JobStatus>() {
                @Override
                public JobStatus call() throws Exception {
                    if (execJobs == false) {
                        return JobStatus.FAILED_RETRY_REQ;
                    }
                    return jobHandler.processJob(qJob.jobReq);
                }
            });
            addJobToInworkQueue(qJob, future);
        } catch (final Exception e) {
            requestRetry(qJob);
        }
    }

    public void stop() {
        execJobs = false;
        try {
            service.shutdown();
            cancleInworkJobs();
            if (service.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
                service.awaitTermination(500, TimeUnit.MILLISECONDS);
            }
        } catch (final InterruptedException e) {
            service.shutdownNow();
            Thread.currentThread().interrupt();
        }
        try {
            while (!stopped) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (final InterruptedException e) {
        }
    }

    @Override
    public void run() {
        // -- Schedule execution of all the handlers.
        for (final Runnable runnable : handlers) {
            service.execute(runnable);
        }

        // -- Start job execution.
        // -- Job execution do following things -
        // -- 1) Check if init status is OK. If not, execute the init implementation
        // -- provided by user and continue to next step if init is OK.
        // -- 2) Check inwork job status and remove completed jobs.
        // -- 3) If execution slot is available (return value of step-2), then
        // -- retrieve the job.
        // -- 4) Start execution of retrieved job.
        while (execJobs) {
            try {
                if (init_req && !init()) {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    continue;
                }

                if (checkJobStatus() == concurrentJobsAllowed) {
                    TimeUnit.MILLISECONDS.sleep(250);
                    continue;
                }

                if (!retrieveJob()) {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    continue;
                }
                processJob();
            } catch (final InterruptedException e) {
                break;
            } catch (final Exception e) {
                System.out.println("Error - " + e.getMessage());
                e.printStackTrace();
            }
        }
        stopped = true;
        System.out.println("completedJobs [" + completedJobs + "]");
    }
}
