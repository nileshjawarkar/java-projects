package co.in.nnj.learn.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import co.in.nnj.learn.executor.JobExecutor;
import co.in.nnj.learn.server.Server;
import co.in.nnj.learn.util.ByteArrayConverter;

public class AppController {
    private final AppStatus appStatus;
    private JobExecutor<JobRequest> executor;
    private final String backupFilePath;

    public AppController() {
        appStatus = new AppStatus();
        backupFilePath = System.getProperty("java.io.tmpdir");
    }

    public AppStatus getAppStatus() {
        return appStatus;
    }

    public void start() {
        try {
            executor.run();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public boolean shutdownApp() {
        try {
            executor.stop();
            final List<JobRequest> jobs = executor.getJobs();
            if (jobs.size() > 0) {
                System.out.println("No of jobs writing to file [" + jobs.size() + "]");
                writeListToFile(jobs);
            }
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void writeListToFile(final List<JobRequest> jobs) {
        if (backupFilePath == null) {
            System.out.println("Error - Failed to tmp dir path");
            return;
        }

        System.out.println("Tmp path - " + backupFilePath);
        try {
            Files.write(Paths.get(backupFilePath, "jobListBackup.byt"),
                ByteArrayConverter.toByteArray(jobs));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setup() {
        try {
            final List<JobRequest> jobs = loadJobsFromFile();
            executor = JobExecutor.<JobRequest>builder()
                           .withPreviousJobs(jobs)
                           .withConcurentHandler(new Server(5000, this))
                           .withProcessor(new JobProcessor(appStatus))
                           .withMaxJobTime(10)
                           .withMaxRetry(2)
                           .withConcurentJobs(2)
                           .build();

            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private List<JobRequest> loadJobsFromFile() {
        final List<JobRequest> jobs = new ArrayList<>();
        final Path path = Paths.get(backupFilePath, "jobListBackup.byt");
        if (Files.exists(path)) {
            try {
                final byte[] allBytes = Files.readAllBytes(path);
                if (allBytes != null && allBytes.length > 0) {
                    try (final ByteArrayInputStream baiStream = new ByteArrayInputStream(allBytes);
                        final ObjectInputStream ooStream = new ObjectInputStream(baiStream);) {
                        final Object obj = ooStream.readObject();
                        if (obj instanceof List<?>) {
                            jobs.addAll((List<JobRequest>) obj);
                        }
                    }
                }
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jobs;
    }
}
