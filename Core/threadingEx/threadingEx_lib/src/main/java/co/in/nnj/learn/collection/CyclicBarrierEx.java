package co.in.nnj.learn.collection;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierEx {
    private static final int MAX_THREADS = 5;

    public static class Task implements Runnable {
        private final int counter;
        private final CyclicBarrier cyclicBarrier;

        public Task(final int counter, final CyclicBarrier cyclicBarrier) {
            this.counter = counter;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                final long time = (long) (Math.random() * 7);
                System.out.println(
                        Thread.currentThread().getName() + ", Executing task= " + counter + ", forTime= " + time);
                TimeUnit.SECONDS.sleep(time);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(MAX_THREADS, new Runnable() {
            @Override
            public void run() {
                System.out.println("All threads done execution");
            }
        });

        final ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
        for (int i = 0; i < 5; i++) {
            final Task t = new Task(i + 1, cyclicBarrier);
            service.execute(t);
        }
        service.shutdown();
    }
}
