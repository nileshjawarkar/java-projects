package co.in.nnj.learn.basics;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ex8_PCModelQueue_withSemaphore {

    public static class ProducerCosumerModel {
        private final Queue<String> msgs = new ArrayDeque<>();
        // -- Producer can add max 5 message, then need to wait
        // -- cunsumer has to consume existing messages to add new one.
        // -- Added sleep to of 1 sec in consumer to visualised this

        private final Semaphore add_msg = new Semaphore(5);
        private final Semaphore read_msg = new Semaphore(0);

        Lock lock = new ReentrantLock();
        int count = 0;

        public void produce_message() throws InterruptedException, IllegalMonitorStateException {
            try {
                add_msg.acquire();
                try {
                    lock.lock();
                    count += 1;
                    final String message = Thread.currentThread().getName() + "=> Count - " + count
                            + ", Gen time queue size - "
                            + msgs.size();
                    msgs.add(message);
                } finally {
                    lock.unlock();
                }
            } finally {
                read_msg.release();
            }
        }

        public void consume_message() throws InterruptedException, IllegalMonitorStateException {
            try {
                read_msg.acquire();
                if (lock.tryLock(10, TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " => Current queue size = " + msgs.size()
                                + ", Message (" + msgs.poll() + ")");
                    } finally {
                        lock.unlock();
                    }
                }
            } finally {
                add_msg.release();
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    public static class Producer extends Thread {
        private final ProducerCosumerModel model;

        public Producer(final ProducerCosumerModel model) {
            this.model = model;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    model.produce_message();
                } catch (final InterruptedException | IllegalMonitorStateException e) {
                    break;
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final ProducerCosumerModel model;

        public Consumer(final ProducerCosumerModel model) {
            this.model = model;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    model.consume_message();
                } catch (final InterruptedException | IllegalMonitorStateException e) {
                    break;
                }
            }
        }
    }

    public static void main(final String[] args) {
        final ProducerCosumerModel producerCosumerModel = new ProducerCosumerModel();
        final Producer producer1 = new Producer(producerCosumerModel);
        final Producer producer2 = new Producer(producerCosumerModel);

        final Consumer consumer1 = new Consumer(producerCosumerModel);
        final Consumer consumer2 = new Consumer(producerCosumerModel);
        // -- final Consumer consumer3 = new Consumer(producerCosumerModel);
        // -- final Consumer consumer4 = new Consumer(producerCosumerModel);

        producer1.start();
        producer2.start();

        consumer1.start();
        consumer2.start();
        // -- consumer3.start();
        // -- consumer4.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (final InterruptedException e) {
        }

        producer1.interrupt();
        producer2.interrupt();
        System.out.println("Producers stoped");
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (final InterruptedException e) {
        }

        consumer1.interrupt();
        consumer2.interrupt();
        // -- consumer3.interrupt();
        // -- consumer4.interrupt();
        System.out.println("Consumers stoped");
    }
}
