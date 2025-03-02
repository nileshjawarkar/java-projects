package co.in.nnj.learn;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.executor.JobExecutor;
import co.in.nnj.learn.executor.JobProcessor;
import co.in.nnj.learn.executor.JobStatus;

public class AppMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppMain.class.getName());

    static List<JobRequest> jobs = List.<JobRequest>of(
        new JobRequest(), new JobRequest(),
        new JobRequest(20),
        new JobRequest(), new JobRequest(),
        new JobRequest(), new JobRequest(),
        new JobRequest(), new JobRequest(),
        new JobRequest(), new JobRequest(),
        new JobRequest(), new JobRequest(),
        new JobRequest(), new JobRequest()
    );

    public static class JobRequest {
        private static Random random = new Random();
        public final String id;
        public long jobTime;

        public JobRequest(final long jobTime) {
            this.id = UUID.randomUUID().toString();
            this.jobTime = jobTime;
        }

        public JobRequest() {
            this.id = UUID.randomUUID().toString();
            jobTime = random.nextLong(1, 3);
        }

        @Override
        public String toString() {
            return "{id=" + id + ", time=" + jobTime + "}";
        }
    }

    public static class JobProcessorImpl implements JobProcessor<JobRequest> {
        @Override
        public boolean init() {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (final InterruptedException e) {
            }
            return true;
        }

        int i = 0;
        @Override
        public JobRequest retrieveJob() {
            final JobRequest req = jobs.get(i);
            i++;
            if(i >= jobs.size()) {
                i = 4;
            }
            return req;
        }

        @Override
        public JobStatus processJob(final JobRequest jobReq) {
            LOGGER.info("Job started [" + jobReq.id + "]");
            final int s = new Random().nextInt(0, 4);
            try {
                TimeUnit.SECONDS.sleep(jobReq.jobTime);
            } catch (final InterruptedException e) {
            } finally {
                LOGGER.info("Job ended [" + jobReq.id + "]");
            }

            JobStatus status = JobStatus.FAILED;
            if(s == 0) {
                status = JobStatus.SUCCEDED;
            } else if(s == 2) {
                status = JobStatus.FAILED_RETRY_REQ;
            } else if(s == 3) {
                status = JobStatus.FAILED_INIT_RETRY_REQ;
            }
            return status; 
        }
    }

    public static void main(final String[] args) {
        JobExecutor<JobRequest> executor;
        try {
            
            executor = JobExecutor.<JobRequest>builder().withProcessor(new JobProcessorImpl())
                    .withMaxJobTime(10)
                    .withConcurentJobs(2).build();
            final Thread t = new Thread(executor);
            t.start();
            t.join(25 * 1000);
            executor.stop();
            t.interrupt();
            System.out.println(executor.getJobs());

        } catch (final InvalidAttributesException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
