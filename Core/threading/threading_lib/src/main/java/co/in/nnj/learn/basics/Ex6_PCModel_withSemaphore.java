package co.in.nnj.learn.basics;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Ex6_PCModel_withSemaphore {

    public static class ProducerCosumerModel {
        private String message = null;
        private final Semaphore add_msg = new Semaphore(1);
        private final Semaphore read_msg = new Semaphore(0);

        public void produce_message() throws InterruptedException {
            try {
                //-- Step 1 : 1st time when following line executes and try to acquire semaphore,
                //-- as it has size of 1, it succeeds.
                //-- Step 5 : when 2nd time this line executes, acquire semaphore fails and execution
                //-- blocks.
                add_msg.acquire();
                message = "Message gen at - " + System.currentTimeMillis();
                TimeUnit.MILLISECONDS.sleep(1);
            } finally {
                //-- Step 3 : It releases read_msg semaphore
                read_msg.release();
            }
        }

        public void consume_message() throws InterruptedException {
            try {
                //-- Step 2 (may be 1) : 1st time when following line executes and try to acquire semaphore,
                //-- as it has size of 0, it fails and block the execution.
                read_msg.acquire();
                //-- Step 4: As read_msg semaphore released at the end of produce_message, 
                //-- we come here and execute the following code.
                if (message != null) {
                    System.out.println(message);
                    message = null;
                }
            } finally {
                //-- Step 6: After reading message, consumer release the add_msg semaphore
                //-- which unblocks the producer and
                //-- execution continues from step 1 again.
                add_msg.release();
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
