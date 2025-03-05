package co.in.nnj.learn.executor;

public class QueuedJob<JR> {
    public final JR jobReq;
    public final int retryNumber;
    public QueuedJob(final JR jobReq) {
        this.jobReq = jobReq;
        this.retryNumber = 0;
    }

    public QueuedJob(final JR jobReq, final int retryCount) {
        this.jobReq = jobReq;
        this.retryNumber = retryCount;
    }
}
