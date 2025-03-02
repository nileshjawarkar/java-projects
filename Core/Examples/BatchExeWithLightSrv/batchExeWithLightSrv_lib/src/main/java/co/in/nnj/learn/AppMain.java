package co.in.nnj.learn;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppMain.class.getName());

    public static class JobRequest {
        public final String id;

        public JobRequest() {
            this.id = UUID.randomUUID().toString();
        }

        @Override
        public String toString() {
            return "{id=" + id + "}";
        }
    }

    public static class JobProcessor implements JobExecutor.JobProcessor<JobRequest, Integer> {
        @Override
        public boolean init() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (final InterruptedException e) {
            }
            return true;
        }

        @Override
        public JobRequest retrieveJob() {
            final int s = new Random().nextInt(1, 3);
            try {
                TimeUnit.SECONDS.sleep(s);
            } catch (final InterruptedException e) {
            }
            if (s == 3) {
                return null;
            }
            return new JobRequest();
        }

        @Override
        public Integer processJob(final JobRequest jobReq) {
            LOGGER.info("Job started [" + jobReq.id + "]");
            final int s = new Random().nextInt(0, 4);
            final int t = new Random().nextInt(1, 3);
            try {
                TimeUnit.SECONDS.sleep(t);
            } catch (final InterruptedException e) {
            } finally {
                LOGGER.info("Job ended [" + jobReq.id + "]");
            }
            return Integer.valueOf(s);
        }

        @Override
        public boolean isJobFailed(final Integer status) {
            if (status != 0) {
                return true;
            }
            return false;

        }

        @Override
        public boolean isRetryRequired(final Integer status) {
            if (status == 2) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isInitRequired(final Integer status) {
            if (status == 3) {
                return true;
            }
            return false;
        }

    }

    public static void main(final String[] args) {
        // -- final JobExecutor<JobRequest, Integer> executor = new JobExecutor<>(new
        // JobProcessor(), 2);
        // final JobExecutor<JobRequest, Integer> executor = new
        // JobExecutor.Builder<JobRequest, Integer>()
        // .withProcessor(new JobProcessor())
        // .withJobs(2).build();
        JobExecutor<JobRequest, Integer> executor;
        try {
            executor = JobExecutor.<JobRequest, Integer>builder().withProcessor(new JobProcessor())
                    .withConcurentJobs(2).build();
            final Thread t = new Thread(executor);
            t.start();
            t.join(20000);
            executor.stop();
            t.interrupt();

        } catch (final InvalidAttributesException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
