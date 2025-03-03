package co.in.nnj.learn.executor;

public interface JobHandler<JR> {
    boolean init();

    JR retrieveJob();

    JobStatus processJob(JR jobReq);
}
