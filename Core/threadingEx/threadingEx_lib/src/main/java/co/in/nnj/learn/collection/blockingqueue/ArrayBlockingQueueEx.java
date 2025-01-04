package co.in.nnj.learn.collection.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* Main feature of ArrayBlockingQueue cunsumer automaticaly wait and block itself
 * until it get next job,
 */
public class ArrayBlockingQueueEx {

    private static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final int limit;

        public Producer(final BlockingQueue<Integer> queue, final int limit) {
            this.queue = queue;
            this.limit = limit;
        }

        @Override
        public void run() {
            for (int i = 0; i < limit; i++) {
                try {
                    queue.put((i + 1) * 100);

                    //-- Added sleep to make Consumer wait
                    TimeUnit.SECONDS.sleep(2);

                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final int limit;
        private int recieved = 0;

        public Consumer(final BlockingQueue<Integer> queue, final int limit) {
            this.queue = queue;
            this.limit = limit;
        }

        @Override
        public void run() {
            while (recieved < limit) {
                final Integer v = queue.poll();
                if (v != null) {
                    recieved += 1;
                    System.out.println("Retrieved - " + v + ", count - " + recieved);
                }
            }
        }
    }

    public static void main(final String[] args) {
        final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        final ExecutorService service = Executors.newFixedThreadPool(2);

        service.execute(new Producer(queue, 15));
        service.execute(new Consumer(queue, 15));

        service.shutdown();
    }
}
