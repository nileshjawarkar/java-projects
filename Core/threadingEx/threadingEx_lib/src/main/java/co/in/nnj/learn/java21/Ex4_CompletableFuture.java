package co.in.nnj.learn.java21;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/* Explanation is not clear about the use case. */
public class Ex4_CompletableFuture {

    public static class Task implements Callable<String>, Supplier<String> {
        private final String result;
        private final long jobTime;

        public Task(final long jobTime, final String result) {
            this.jobTime = jobTime;
            this.result = result;
        }

        @Override
        public String get() {
            try {
                return call();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String call() throws Exception {
            System.out.println("Executing job with -" + jobTime);
            TimeUnit.SECONDS.sleep(jobTime);
            return result;
        }
    }

    public static void blockingImpl() {
        final Task t1 = new Task(10, "result1");
        final Task t2 = new Task(2, "result2");
        try (var service = Executors.newVirtualThreadPerTaskExecutor()) {
            final Future<String> result1 = service.submit(t1);
            final Future<String> result2 = service.submit(t2);

            final String s1 = result1.get();
            final String s2 = result2.get();

            System.out.println("Final result = " + s1 + " + " + s2);
        } catch (final Exception e) {
        }
    }

    public static void non_blockingImpl() {
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            final String result = CompletableFuture.supplyAsync(new Task(10, "result1"), executorService)
                    .thenCombineAsync(CompletableFuture.supplyAsync(new Task(2, "result2"), executorService),
                            (s1, s2) -> {
                                return s1 + " + " + s2;
                            })
                    .thenApply(s -> "final result = " + s)
                    .join();

            System.out.println(result);
        }
    }

    public static void main(final String[] args) {
        blockingImpl();
        //-- non_blockingImpl();
    }
}
