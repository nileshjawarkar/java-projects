package co.in.nnj.learn.core;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.executor.JobHandler;
import co.in.nnj.learn.executor.JobStatus;

public class JobProcessor implements JobHandler<JobRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobProcessor.class.getName());

    private final AppStatus appStatus;
    public JobProcessor(final AppStatus appStatus) {
        this.appStatus = appStatus;
    }

    static List<JobRequest> jobs = List.<JobRequest>of(
            new JobRequest(20, 0),
            new JobRequest(), new JobRequest(),
            new JobRequest(), new JobRequest(),
            new JobRequest(), new JobRequest(),
            new JobRequest(), new JobRequest(),
            new JobRequest(), new JobRequest(),
            new JobRequest(), new JobRequest(),
            new JobRequest(), new JobRequest(),
            new JobRequest(), new JobRequest(),
            new JobRequest(), new JobRequest(),
            new JobRequest(2, 1), new JobRequest(1, 2),
            new JobRequest(2, 3), new JobRequest(1, 4),
            new JobRequest(), new JobRequest());

    @Override
    public boolean init() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (final InterruptedException e) {
        }
        return true;
    }

    boolean reset = false;
    int i = 0;

    @Override
    public JobRequest retrieveJob() {
        final JobRequest req = jobs.get(i);
        i++;
        if (i >= jobs.size()) {
            i = 1;
        }
        return req;
    }

    @Override
    public JobStatus processJob(final JobRequest jobReq) {
        LOGGER.info("Job started [" + jobReq.id + "]");
        try {
            TimeUnit.SECONDS.sleep(jobReq.jobTime);
        } catch (final InterruptedException e) {
        } finally {
            LOGGER.info("Job ended [" + jobReq.id + "]");
        }

        JobStatus status = JobStatus.FAILED;
        final int s = jobReq.result;
        if (s == 0) {
            status = JobStatus.SUCCEEDED;
        } else if (s == 2) {
            status = JobStatus.FAILED_RETRY_REQ;
        } else if (s == 3) {
            status = JobStatus.FAILED_INIT_RETRY_REQ;
        }
        return status;
    }
}
