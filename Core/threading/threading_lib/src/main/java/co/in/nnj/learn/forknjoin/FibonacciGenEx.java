package co.in.nnj.learn.forknjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import co.in.nnj.learn.util.TimeLogger;

public class FibonacciGenEx {

    private static Integer genFibonacciNumber(final int num) {
        if (num <= 1) {
            return num;
        }
        return genFibonacciNumber(num - 1) + genFibonacciNumber(num - 2);
    }

    public static class FibonacciGenerator extends RecursiveTask<Integer> {
        private final int num;

        public FibonacciGenerator(final int num) {
            this.num = num;
        }

        @Override
        protected Integer compute() {
            // -- Step 1: Define base condition for recursion to end
            if (num <= 20) {
                // -- return num;
                // Sequential algo work perfectly good till 20, then its PCS degrades
                // So for numbers above 20 use parallel algo
                return genFibonacciNumber(num);
            }

            // -- Step 2: Devide job in to subtasks
            final FibonacciGenerator f1 = new FibonacciGenerator(num - 1);
            final FibonacciGenerator f2 = new FibonacciGenerator(num - 2);

            // -- Step 3: Start the jobs
            // -- f1.fork();
            f2.fork();

            // -- Step 4: Merge the results of the jobs
            // -------------------------------------
            // -- return f1.join() + f2.join();
            // -------------------------------------
            // -- Lets re-use current thread, as in above case current thread will be in
            // waiting
            // -- phase and remain underutilised.
            return f1.compute() + f2.join(); // -- Now we are using current thread for executing f1
        }
    }

    public static void main(final String[] args) {
        final TimeLogger logger = new TimeLogger();

        // -- Run it Sequentially
        logger.init("Sequential");
        System.out.println("Fibonacci of 45 => " + genFibonacciNumber(45));
        logger.logTimeDiff();

        // -- Run it parallelly
        final ForkJoinPool pool = new ForkJoinPool(2);
        logger.init("Parallel");
        try {
            System.out.println("Fibonacci of 45 => " + pool.invoke(new FibonacciGenerator(45)));
        } finally {
            pool.close();
        }
        logger.logTimeDiff();
    }
}
