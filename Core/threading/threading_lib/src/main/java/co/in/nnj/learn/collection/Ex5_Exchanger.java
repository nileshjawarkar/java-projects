package co.in.nnj.learn.collection;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Ex5_Exchanger {

    public static void main(final String[] args) {
        final Exchanger<Integer> exchanger = new Exchanger<>();

        final Thread t1 = new Thread(new Runnable() {
            int counter = 10;

            @Override
            public void run() {
                while (true) {
                    counter++;
                    System.out.println("t1 => Incrementing counter to = " + counter);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        counter = exchanger.exchange(counter);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t1 => Setting counter to value from t2 = " + counter);
                }
            }
        });

        final Thread t2 = new Thread(new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                while (true) {
                    counter--;
                    System.out.println("t2 => Decrementing counter to = " + counter);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        counter = exchanger.exchange(counter);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t2 => Setting counter to value from t1 = " + counter);
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (final Exception e) {
        }
    }
}
