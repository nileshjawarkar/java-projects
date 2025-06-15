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
                            System.out.println(Thread.currentThread().getName() + " waiting...");
                            this.notify();
                            this.wait();
                        }
                        System.out.println(Thread.currentThread().getName() + " added message...");
                        messageQueue.add("message " + count + ", size = " + messageQueue.size());
                        count++;
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
                            this.notify();
                            this.wait();
                        }

                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("Consumed = " + messageQueue.poll());
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

        //-- Start the thread and wait for 20 SECONDS.
        t1.start();
        t2.start();

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (final InterruptedException e) {
        }

        //-- After 20 SECONDS, stop the loop.
        task.setEnd(true);

        //-- At the end one of the thread will be waiting and blocked.
        //-- So we need to kill the blocked thread here.
        if(t1.isAlive()) t1.interrupt();
        if(t2.isAlive()) t2.interrupt();
    }
}
