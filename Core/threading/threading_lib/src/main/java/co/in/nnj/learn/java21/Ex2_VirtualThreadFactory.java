package co.in.nnj.learn.java21;

import java.util.concurrent.TimeUnit;

public class Ex2_VirtualThreadFactory {
    public static void main(final String[] args) {
        //-- create factory or builder
        final var factory = Thread.ofVirtual().name("TestThread -").factory();
        final var t1 = factory.newThread(new Runnable() {
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

        t1.start();

        try {
            t1.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

    }
}
