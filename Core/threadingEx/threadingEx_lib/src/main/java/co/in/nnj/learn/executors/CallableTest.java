package co.in.nnj.learn.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallableTest {

    public static class Task implements Callable<Integer> {
        int id;

        public Task(final int id) {
            this.id = id;
        }

        @Override
        public Integer call() throws Exception {
            final int rnum = (int) (Math.random() * 5);
            TimeUnit.SECONDS.sleep(rnum);
            return rnum;
        }
    }

    public static class RespHandler<T> {
        List<Future<T>> futures;

        public RespHandler() {
            futures = new ArrayList<>();
        }

        public void add(final int index, final Future<T> future) {
            futures.add(future);
        }

        public boolean allDone() {
            for (final Future<T> future : futures) {
                if (!future.isDone()) {
                    return false;
                }
            }
            return true;
        }

        public int getSize() {
            return futures.size();

        }

        public T get(final int index) throws InterruptedException, ExecutionException {
            if (index >= futures.size() || index < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            return futures.get(index).get();
        }
    }

    public static void main(final String[] args) {
        final ExecutorService service = Executors.newFixedThreadPool(2);
        final RespHandler<Integer> handlers = new RespHandler<>();
        for (int i = 0; i < 10; i++) {
            final Task t = new Task(i);
            final Future<Integer> resHandler = service.submit(t);
            handlers.add(i, resHandler);
        }

        System.out.println("Waiting to complete all the jobs");
        while (!handlers.allDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < handlers.getSize(); i++) {
            try {
                System.out.println("index = " + i + " => " + handlers.get(i));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        service.shutdown();
    }
}
