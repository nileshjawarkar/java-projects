package co.in.nnj.learn.problems;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* - The dining philosopher's problem is the classical problem of synchronization 
 * which says that Five philosophers are sitting around a circular table and their 
 * - job is to think and eat alternatively. 
 * A bowl of noodles is placed at the center of the table along with five chopsticks 
 * for each of the philosophers. 
 * - To eat a philosopher needs both their right and a left chopstick. A philosopher can only 
 * eat if both immediate left and right chopsticks of the philosopher is available. 
 * - In case if both immediate left and right chopsticks of the philosopher are not 
 * available then the philosopher puts down their (either left or right) chopstick 
 * and starts thinking again.
*/
public class DiningPProb {

    public static enum ChopStickSide {
        LEFT, RIGHT
    }

    public static class ChopStick {
        private final String id;
        private final Lock lock;

        public ChopStick(final String id) {
            this.id = id;
            lock = new ReentrantLock();
        }

        public boolean acquire(final Philosopher philosopher, final ChopStickSide side) {
            try {
                if (lock.tryLock(50, TimeUnit.MILLISECONDS)) {
                    // -- System.out.println(philosopher + " acquired chopstick " + side + "
                    // hand.");
                    return true;
                }
            } catch (final InterruptedException e) {
            }
            return false;
        }

        public void release() {
            lock.unlock();
        }

        @Override
        public String toString() {
            return "ChopStick {id=" + id + "}";
        }
    }

    public static enum PhilosopherMode {
        EATTING, THINKING
    }

    public static class Philosopher implements Runnable {
        private final ChopStick left;
        private final ChopStick right;
        private final String id;
        private PhilosopherMode mode;
        private long counter = 0;

        private boolean isFull = false;

        public Philosopher(final String id, final ChopStick right, final ChopStick left) {
            this.id = id;
            this.right = right;
            this.left = left;
            mode = PhilosopherMode.THINKING;
        }

        @Override
        public String toString() {
            return "Philosopher {id=" + id + ", mode=" + mode + "}";
        }

        public void setFull() {
            isFull = true;
        }

        public boolean acquireChopsticks() {
            if (left.acquire(this, ChopStickSide.LEFT)) {
                if (right.acquire(this, ChopStickSide.RIGHT)) {
                    return true;
                } else {
                    left.release();
                }
            }
            return false;
        }

        public void releaseChopsticks() {
            if (mode == PhilosopherMode.EATTING) {
                left.release();
                right.release();
            }
        }

        public void eat() {
            mode = PhilosopherMode.EATTING;
            counter += 1;
        }

        public void think() {
            mode = PhilosopherMode.THINKING;
        }

        public long getCouter() {
            return counter;            
        }

        @Override
        public void run() {
            while (!isFull) {
                long jobTime = 3;
                if (mode == PhilosopherMode.THINKING && acquireChopsticks()) {
                    eat();
                    jobTime = 2;
                } else {
                    releaseChopsticks();
                    think();
                }
                System.out.println(this);
                try {
                    TimeUnit.SECONDS.sleep(jobTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(final String[] args) {
        final ChopStick s1 = new ChopStick("S1");
        final ChopStick s2 = new ChopStick("S2");
        final ChopStick s3 = new ChopStick("S3");
        final ChopStick s4 = new ChopStick("S4");
        final ChopStick s5 = new ChopStick("S5");

        final Philosopher p1 = new Philosopher("P1", s1, s2);
        final Philosopher p2 = new Philosopher("P2", s2, s3);
        final Philosopher p3 = new Philosopher("P3", s3, s4);
        final Philosopher p4 = new Philosopher("P4", s4, s5);
        final Philosopher p5 = new Philosopher("P5", s5, s1);

        /*
        new Thread(p1).start();
        new Thread(p2).start();
        new Thread(p3).start();
        new Thread(p4).start();
        new Thread(p5).start(); */

        //-- Start the threads
        ExecutorService service = Executors.newFixedThreadPool(5);
        service.execute(p1);
        service.execute(p2);
        service.execute(p3);
        service.execute(p4);
        service.execute(p5);

        //-- Wait for 20 SECONDS
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //-- End the execution
        p1.setFull();
        p2.setFull();
        p3.setFull();
        p4.setFull();
        p5.setFull();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //-- Stop all the threads
        service.shutdown();
        System.out.println("p1 eatting count - " + p1.getCouter());
        System.out.println("p2 eatting count - " + p2.getCouter());
        System.out.println("p3 eatting count - " + p3.getCouter());
        System.out.println("p4 eatting count - " + p4.getCouter());
        System.out.println("p5 eatting count - " + p5.getCouter());

    }
}
