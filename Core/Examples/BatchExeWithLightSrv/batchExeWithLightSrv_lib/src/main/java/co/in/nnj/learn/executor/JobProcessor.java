package co.in.nnj.learn.executor;

public interface JobProcessor<JR> {
    boolean init();

    JR retrieveJob();

    JobStatus processJob(JR jobReq);
}
