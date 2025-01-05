package co.in.nnj.learn.problems;

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

        public boolean tryToEat() {
            if (left.acquire(this, ChopStickSide.LEFT)) {
                if (right.acquire(this, ChopStickSide.RIGHT)) {
                    mode = PhilosopherMode.EATTING;
                    return true;
                } else {
                    left.release();
                }
            }
            return false;
        }

        public void endEatting() {
            if (mode == PhilosopherMode.EATTING) {
                left.release();
                right.release();
                mode = PhilosopherMode.THINKING;
            }
        }

        @Override
        public void run() {
            while (true) {
                if (mode == PhilosopherMode.THINKING) {
                    tryToEat();
                } else {
                    endEatting();
                }

                System.out.println(this);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (final InterruptedException e) {
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

        new Thread(p1).start();
        new Thread(p2).start();
        new Thread(p3).start();
        new Thread(p4).start();
        new Thread(p5).start();

    }
}
