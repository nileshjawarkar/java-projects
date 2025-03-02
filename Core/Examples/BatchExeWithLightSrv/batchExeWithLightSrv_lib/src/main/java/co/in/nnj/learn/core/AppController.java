package co.in.nnj.learn.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import co.in.nnj.learn.executor.JobExecutor;
import co.in.nnj.learn.server.Server;

public class AppController {

    private final AppStatus appStatus;
    private JobExecutor<JobRequest> executor;

    public AppController() {
        appStatus = new AppStatus();
    }

    public AppStatus getAppStatus() {
        return appStatus;
    }

    public byte[] getAppStatusAsByteArray() throws IOException {
        final ByteArrayOutputStream baStream = new ByteArrayOutputStream();
        final ObjectOutputStream ooStream = new ObjectOutputStream(baStream);
        ooStream.writeObject(appStatus);
        ooStream.flush();
        return baStream.toByteArray();
    }

    public void start() {
        try {
            executor.run();
            /*
             * final Thread t = new Thread(executor);
             * t.start();
             * t.join(25 * 1000);
             * executor.stop();
             * t.interrupt();
             * System.out.println("Pending Jobs - " + executor.getJobs());
             */
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public boolean shutdownApp() {
        try {
            executor.stop();
            final List<JobRequest> jobs = executor.getJobs();
            if(jobs.size() > 0) {
                writeListToFile(jobs);
            }
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void writeListToFile(final List<JobRequest> jobs) {
        //TODO
    }


    public boolean setup() {
        try {
            final List<JobRequest> jobs = loadJobsFromFile();
            executor = JobExecutor.<JobRequest>builder()
                    .withPreviousJobs(jobs)
                    .withConcurentHandler(new Server(5000, this))
                    .withProcessor(new JobProcessor(appStatus))
                    .withMaxJobTime(10).withMaxRetry(2)
                    .withConcurentJobs(2).build();

            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<JobRequest> loadJobsFromFile() {
        final List<JobRequest> jobs = new ArrayList<>();
        //TODO
        return jobs;
    }
}
