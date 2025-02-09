package co.in.nnj.learn.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ex1_SingleThreadExecutor {

    public static class Task implements Runnable {
        private final int counter;

        public Task(final int counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": Executing task - " + counter);
            try {
                final long time = (long) (Math.random() * 5);
                TimeUnit.SECONDS.sleep(time);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 15; i++) {
            service.execute(new Task(i));
        }

        try {
            service.awaitTermination(25, TimeUnit.SECONDS);
            System.out.println("Done");
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }
}
