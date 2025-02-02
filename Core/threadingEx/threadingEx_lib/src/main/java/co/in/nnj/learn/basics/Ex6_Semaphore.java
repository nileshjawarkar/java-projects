package co.in.nnj.learn.basics;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Ex6_Semaphore {

    public static class ProducerCosumerModel {
        private String message = null;
        private final Semaphore empty = new Semaphore(1);
        private final Semaphore full = new Semaphore(0);

        public void produce_message() throws InterruptedException {
            try {
                empty.acquire();
                message = "Message gen at - " + System.currentTimeMillis();
                TimeUnit.MILLISECONDS.sleep(10);
            } finally {
                full.release();
            }
        }

        public void consume_message() throws InterruptedException {
            try {
                full.acquire();
                if (message != null) {
                    System.out.println(message);
                    message = null;
                }
            } finally {
                empty.release();
            }
        }
    }

    public static void main(final String[] args) {
        final ProducerCosumerModel producerCosumerModel = new ProducerCosumerModel();
        final Thread producer = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    producerCosumerModel.produce_message();
                } catch (final InterruptedException e) {
                    break;
                }
            }
        });

        final Thread consumer = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    producerCosumerModel.consume_message();
                } catch (final InterruptedException e) {
                    break;
                }
            }
        });

        producer.start();
        consumer.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        producer.interrupt();
        consumer.interrupt();
    }
}
