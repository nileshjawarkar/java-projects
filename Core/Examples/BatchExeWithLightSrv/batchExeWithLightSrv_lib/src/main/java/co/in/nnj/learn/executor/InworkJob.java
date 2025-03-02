package co.in.nnj.learn.executor;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* Class to manage inwork jobs */
class InworkJob<JR> extends QueuedJob<JR> {
    public long startTime;
    public Future<JobStatus> future;

    public InworkJob(final JR jobReq, final Future<JobStatus> future, final int retryNumber) {
        super(jobReq, retryNumber);
        this.future = future;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isDone() {
        return future.isDone();
    }

    public JobStatus getStatus() throws InterruptedException, ExecutionException {
        try {
            return future.get();
        } catch (final CancellationException e) {
            return JobStatus.CANCELED;
        }
    }

    public boolean isTimeOutReached(final int timeInSec) {
        final long timeInMS = (System.currentTimeMillis() - startTime);
        return (TimeUnit.MILLISECONDS.toSeconds(timeInMS) > timeInSec);
    }
}
