package co.in.nnj.learn.basics;

import java.util.concurrent.TimeUnit;

/* This example demostrates that synchronized block on static and object method uses
 * lock of diffent objects -
 *  static methods = uses lock on State.public class 
 *  object methods = uses lock on this object 
 * To demostrate this, we are using static method with sleep of 10 sec and
 * started its thread in beginning. If both synchronized uses same lock, this method will delay
 * execution of other 2 threas, but it not happing. Which indicate both synchronized are using diffent
 * lock on diffent objects.
 *
 * Note - It is always better to use localised synchronized(this) block as compaire to synchronized on methods.
 * This reduces overall blocking time and improve performance.
 */

public class SyncroTest {

    public static class State {
        private int counter1 = 0;
        private int counter2 = 0;

        private static int counter3 = 0;

        //-- This synchronized declaration on static method, uses lock on 
        //-- State.class and it is diffent from non-static methods
        public static synchronized void incCounter3() {
            counter3++;
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (final InterruptedException e) {
            }
        }

        public static int getCounter3(){
           return counter3; 
        }

        //-- synchronized declaration uses lock on current instance (object) of class
        public synchronized void incCounter1() {
            // -- synchronized (this) {
            counter1++;
            // -- }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (final InterruptedException e) {
            }
        }

        public synchronized void incCounter2() {
            // -- synchronized (this) {
            counter2++;
            // -- }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (final InterruptedException e) {
            }
        }

        public int getCounter1() {
            return counter1;
        }

        public int getCounter2() {
            return counter2;
        }
    }

    public static void main(final String[] args) {

        final State state = new State();

        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (state.getCounter1() < 40) {
                    state.incCounter1();
                    System.out.println(Thread.currentThread().getName() + ": counter1 =>" + state.getCounter1());
                }
            }

        });

        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (state.getCounter2() < 20) {
                    state.incCounter2();
                    System.out.println(Thread.currentThread().getName() + ": counter2 =>" + state.getCounter2());
                }
            }

        });

        final Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (State.getCounter3() < 20) {
                    State.incCounter3();
                    System.out.println(Thread.currentThread().getName() + ": counter3 =>" + State.getCounter3());
                }
            }

        });

        t3.start();
        t1.start();
        t2.start();

        /*
         * try {
         * t1.join();
         * t2.join();
         * } catch (final InterruptedException e) {
         * }
         */
    }
}
