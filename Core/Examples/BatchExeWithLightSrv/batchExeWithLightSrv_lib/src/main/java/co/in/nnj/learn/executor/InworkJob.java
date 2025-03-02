package co.in.nnj.learn.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* Class to manage inwork jobs */
class InworkJob<JR> {
    public long startTime;
    public JR jobReq;
    public Future<JobStatus> future;

    public InworkJob(final JR jobReq, final Future<JobStatus> future) {
        this.jobReq = jobReq;
        this.future = future;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isDone() {
        return future.isDone();
    }

    public JobStatus getStatus() throws InterruptedException, ExecutionException {
        return future.get();
    }

    public boolean isTimeOutReached(final int timeInSec) {
        final long timeInMS = (System.currentTimeMillis() - startTime);
        return (TimeUnit.MILLISECONDS.toSeconds(timeInMS) > timeInSec);
    }
}
