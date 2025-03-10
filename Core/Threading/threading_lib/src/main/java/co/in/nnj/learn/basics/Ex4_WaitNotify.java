package co.in.nnj.learn.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//-- Must follow this struture for wait and notity
//-- Producer:
//-- synchronized(lock) {
//--    .. Perform the task and notify
//--    lock.notify();
//--    .. Wait for other thread to notify
//--    lock.wait();
//-- }
//-- Consumer:
//-- synchronized(lock) {
//--    .. Wait for other thread to notify
//--    lock.wait();
//--    .. Perform the task and notify other thread then wait
//--    lock.notify();
//-- }

public class Ex4_WaitNotify {

    public static class Worker {
        private final List<String> jobList = new ArrayList<>();
        private final Object lock = new Object();

        private int counter = 0;
        private final int MAX_JOB_SIZE = 4;

        public void producer() throws InterruptedException {
            // -- Producer add 4 (MAX_JOB_SIZE) messages to list,
            // -- then notify the other thread, so that it can acquire the lock
            // -- and proceed. And it self call the wait and wait other thread to notify it.
            synchronized (lock) {
                while (counter < 40) {
                    if (jobList.size() < MAX_JOB_SIZE) {
                        final String str = "JobNumber-" + counter;
                        jobList.add(str);
                        counter++;
                        System.out.println("Added - " + str);
                        if (jobList.size() == MAX_JOB_SIZE) {
                            lock.notify();
                        }
                    } else {
                        System.out.println("Waiting cusumer to consume the jobs");
                        lock.wait();
                    }
                }
            }
        }

        public void consumer() throws InterruptedException {
            // -- Consumer check the jobList size, if it is zero,
            // -- it will wait for the other thread to add jobs and notify.
            // -- When other thread notify and it will acquire the lock and 
            // -- read the jobs. When jobList become empty, it notify the other Thread
            // -- to continue and goes to wait state.
            synchronized (lock) {
                while (counter < 40 || jobList.size() > 0) {
                    if (jobList.size() == 0) {
                        System.out.println("Waiting producer to produce the jobs");
                        lock.wait();
                    } else {
                        final String str = jobList.remove(jobList.size() - 1);
                        System.out.println(str);
                        if (jobList.size() == 0) {
                            lock.notify();
                        }
                    }
                }
            }
        }
    }

    public static void main(final String[] args) {
        final Worker worker = new Worker();
        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    worker.producer();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    worker.consumer();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
