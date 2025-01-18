package co.in.nnj.learn.java21;

import java.util.concurrent.TimeUnit;

public class VirtualThreads {
    public static void main(final String[] args) {
        //-- create factory or builder
        final var builder = Thread.ofVirtual().name("TestThread -");
        final var t1 = builder.start(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task 1 - Started");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task 1 - Ended");
            }
        });

        try {
            t1.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

    }
}
