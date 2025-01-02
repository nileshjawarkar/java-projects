package co.in.nnj.learn.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleExecutorTest {

    private static final int INITIAL_DELAY = 2;

    public static class Task implements Runnable {
        private final int timePeriod;
        private final int id;

        public Task(final int id, final int timePeriod) {
            this.id = id;
            this.timePeriod = timePeriod;
        }

        @Override
        public void run() {
            final long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName() + "-" + now + " => Id = " + id + ", scheduledAt = " + timePeriod);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

   public static void main(final String[] args) {
        final ScheduledExecutorService service = Executors.newScheduledThreadPool(1) ;
        int timePeriod = 2;
        for (int i = 0; i < 3; i++) {
            final Task t = new Task(i + 1, timePeriod);
            service.scheduleWithFixedDelay(t, INITIAL_DELAY, timePeriod, TimeUnit.SECONDS);
            //-- service.scheduleAtFixedRate(t, INITIAL_DELAY, timePeriod, TimeUnit.SECONDS);
            timePeriod += 2;
        }

        try {
            TimeUnit.SECONDS.sleep(13);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();
   } 
}
