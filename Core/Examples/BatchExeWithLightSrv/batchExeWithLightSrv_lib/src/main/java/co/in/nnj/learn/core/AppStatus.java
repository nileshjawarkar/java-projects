package co.in.nnj.learn.core;

import java.io.Serializable;

public class AppStatus implements Serializable {
    private long jobReceived;
    private long jobCompleted;
    private long lastHeartBit;

    public AppStatus() {
        jobReceived = 0;
        jobCompleted = 0;
        lastHeartBit = System.currentTimeMillis();
    }

    public long getJobReceived() {
        return jobReceived;
    }
    public void setJobReceived(final long jobReceived) {
        this.lastHeartBit = System.currentTimeMillis();
        this.jobReceived = jobReceived;
    }
    public long getJobCompleted() {
        return jobCompleted;
    }
    public void setJobCompleted(final long jobCompleted) {
        this.lastHeartBit = System.currentTimeMillis();
        this.jobCompleted = jobCompleted;
    }
    public long getLastHeartBit() {
        return lastHeartBit;
    }
    public void setLastHeartBit(final long lastHeartBit) {
        this.lastHeartBit = lastHeartBit;
    }

    @Override
    public String toString() {
        return "AppStatus {jobReceived=" + jobReceived + ", jobCompleted=" + jobCompleted + ", lastHeartBit="
                + lastHeartBit + "}";
    }

}
