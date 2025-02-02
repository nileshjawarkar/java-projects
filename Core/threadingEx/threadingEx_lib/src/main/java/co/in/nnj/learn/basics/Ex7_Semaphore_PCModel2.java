package co.in.nnj.learn.basics;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ex7_Semaphore_PCModel2 {

    public static class ProducerCosumerModel {
        private final Queue<String> msgs = new ArrayDeque<>();
        //-- Producer can add max 5 message, then need to wait
        //-- cunsumer to consume some messages to add new one.
        //-- Added sleep to of 1 sec in consumer to visualised this
        private final Semaphore empty = new Semaphore(5);
        private final Semaphore full = new Semaphore(0);

        Lock lock = new ReentrantLock();

        public void produce_message() throws InterruptedException, IllegalMonitorStateException {
            try {
                empty.acquire();
                final String message = Thread.currentThread().getName() + ", Message time - "
                        + System.currentTimeMillis();
                lock.lock();
                System.out.println("Gen - " + message);
                msgs.add(message);
            } finally {
                lock.unlock();
                full.release();
            }
        }

        public void consume_message() throws InterruptedException, IllegalMonitorStateException {
            try {
                full.acquire();
                lock.lock();
                System.out.println(msgs.poll());
            } finally {
                lock.unlock();
                //-- TimeUnit.MILLISECONDS.sleep(10);
                TimeUnit.SECONDS.sleep(1);
                empty.release();
            }
        }
    }

    public static void main(final String[] args) {
        final ProducerCosumerModel producerCosumerModel = new ProducerCosumerModel();
        final Thread producer1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    producerCosumerModel.produce_message();
                } catch (final InterruptedException | IllegalMonitorStateException e) {
                    break;
                }
            }
        });

        final Thread producer2 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    producerCosumerModel.produce_message();
                } catch (final InterruptedException | IllegalMonitorStateException e) {
                    break;
                }
            }
        });

        final Thread consumer = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    producerCosumerModel.consume_message();
                } catch (final InterruptedException | IllegalMonitorStateException e) {
                    break;
                }
            }
        });

        producer1.start();
        producer2.start();
        consumer.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (final InterruptedException e) {
        }

        producer1.interrupt();
        producer2.interrupt();

        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Producers stoped");
        } catch (final InterruptedException e) {
        }

        consumer.interrupt();
        System.out.println("Consumers stoped");
    }
}
