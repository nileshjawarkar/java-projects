package co.in.nnj.learn.basics;

import java.util.concurrent.TimeUnit;

public class Ex1_StartAndJoin {

    public static class Loop1 implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 30; i++) {
                System.out.println("Loop1 - i = " + i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (final InterruptedException e) {
                }
            }
        }
    }

    public static class Loop2 implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 15; i++) {
                System.out.println("Loop2 - i = " + i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (final InterruptedException e) {
                }
            }
        }
    }

    public static void main(final String[] args) {
        final Thread t1 = new Thread(new Loop1());
        final Thread t2 = new Thread(new Loop2());

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (final InterruptedException e) {
        }
    }
}
