package co.in.nnj.learn.java21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ex3_VirtualPerTaskExecutor {

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

    public static void main(final String[] args) {
        // -- final ExecutorService service = Executors.newFixedThreadPool(3);
        try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 15; i++) {
                service.submit(new Task(i));
            }
        }
    }
}
