package co.in.nnj.learn.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ex3_WaitNotify {

    public static class Worker {
        private final List<String> jobList = new ArrayList<>();
        private final Object lock = new Object();

        private int counter = 0;
        private final int MAX_LEN = 4;

        public void producer() throws InterruptedException {
            synchronized (lock) {
                while (counter < 40) {
                    if (jobList.size() < MAX_LEN) {
                        final String str = "JobNumber-" + counter;
                        jobList.add(str);
                        counter++;
                        System.out.println("Added - " + str);
                        if (jobList.size() == MAX_LEN) {
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
            synchronized (lock) {
                while (counter < 40 || jobList.size() > 0) {
                    if ( jobList.size() == 0 ) {
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
