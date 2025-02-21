package co.in.nnj.learn.basics;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

//-- Not a good example of PCModel
//-- Can not use multiple producer and consumers
public class Ex7_PCModel_WithWaitAndNotify {

    /* With wait and notify we can not use 3 thread.
     * I mean, we can have only 1 producer and 1 consumer. */
    public static class ProducerAndConsumer {
        private final Queue<String> messageQueue = new ArrayDeque<>();
        private boolean end = false;
        private int count = 1;

        public void setEnd(final boolean end) {
            this.end = end;
        }

        public void producer() {
            while (!end) {
                synchronized (this) {
                    try {
                        if (messageQueue.size() > 4) {
                            this.wait();
                        }
                        messageQueue.add("message " + count + ", size = " + messageQueue.size());
                        count++;
                        this.notify();
                    } catch (final InterruptedException e) {
                    }
                }
            }
        }

        public void consumer() {
            while (!end) {
                synchronized (this) {
                    try {
                        if (messageQueue.size() == 0) {
                            System.out.println(Thread.currentThread().getName() + " waiting...");
                            this.wait();
                        }

                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("Consumed = " + messageQueue.poll());
                        this.notify();
                    } catch (final InterruptedException e) {
                    }
                }
            }
        }

    }

    public static void main(final String[] args) {
        final ProducerAndConsumer task = new ProducerAndConsumer();
        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                task.producer();
            }
        });

        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                task.consumer();
            }
        });

        t1.start();
        t2.start();

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (final InterruptedException e) {
        }

        task.setEnd(true);
    }
}
