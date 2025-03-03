package co.in.nnj.learn.core;

import java.util.Random;
import java.util.UUID;

public class JobRequest {
    private static Random random = new Random();
    public final String id;
    public final long jobTime;
    public int result;

    public JobRequest(final long jobTime, final int result) {
        this.id = UUID.randomUUID().toString();
        this.jobTime = jobTime;
        this.result = result;
    }

    public JobRequest() {
        this.id = UUID.randomUUID().toString();
        jobTime = random.nextLong(1, 3);
        this.result = 0;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", time=" + jobTime + "}";
    }
}
