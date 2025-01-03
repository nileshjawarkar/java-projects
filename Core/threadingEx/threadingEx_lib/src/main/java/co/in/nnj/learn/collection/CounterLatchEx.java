package co.in.nnj.learn.collection;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CounterLatchEx {
    public static class Task implements Runnable {
        private final int counter;
        private final CountDownLatch countDownLatch;

        public Task(final int counter, final CountDownLatch countDownLatch) {
            this.counter = counter;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                final long time = (long) (Math.random() * 7);
                System.out.println(Thread.currentThread().getName() + ", Executing task= " + counter + ", forTime= " + time);
                TimeUnit.SECONDS.sleep(time);
                countDownLatch.countDown();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(5);
        final ExecutorService service = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 5; i++) {
            final Task t = new Task(i + 1, countDownLatch);
            service.execute(t);
        }

        try {
            countDownLatch.await();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("All threads done execution");
        service.shutdown();

    }
}
