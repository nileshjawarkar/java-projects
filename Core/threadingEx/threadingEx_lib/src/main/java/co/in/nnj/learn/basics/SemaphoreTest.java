package co.in.nnj.learn.basics;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
 * Semaphore used to limit concurrent execution of some task.
 * In this example, we will use while loop with 50 iterations, but
 * semaphore used here with 3 limit, allows only 3 concurrent threads.
 *
 * Note - when 4rth thread statted and when it execute task method,
 * this method will try to acquire the scemaphore and ask limit of 3 crossed,
 * this acquire method blocks its execution.
 */

public class SemaphoreTest {
    public static class Tasks{
        private int counter = 0;

        //--This scemaphore allows only 3 concurrent executions
        Semaphore semaphore = new Semaphore(3, true);

        public void task(){
            try {
                semaphore.acquire();
                counter++;
                System.out.println("TaskNo-" + counter);
                TimeUnit.SECONDS.sleep(10);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }

    }

    public static void main(final String[] args) {
        final Tasks  ts =  new Tasks();

        int i = 0;
        while(i < 100) {
            final Thread t =  new Thread(new Runnable() {
                @Override
                public void run() {
                    ts.task();
                }
            });
            t.start();
            i++;
        }
    }
}
