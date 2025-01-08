package co.in.nnj.learn.forknjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FibonacciGenEx {

    public static class FibonacciGenerator extends RecursiveTask<Integer> {
        private int num;
        public FibonacciGenerator(int num) {
            this.num = num;
        }

        @Override
        protected Integer compute() {
            //-- Step 1: Define base condition for recursion to end
            if (num <= 1) {
                return num;
            }

            //-- Step 2: Devide job in to subtasks
            FibonacciGenerator f1 = new FibonacciGenerator(num - 1);
            FibonacciGenerator f2 = new FibonacciGenerator(num - 2);

            //-- Step 3: Start the jobs
            //-- f1.fork();
            f2.fork();

            //-- Step 4: Merge the results of the jobs
            //-- return f1.join() + f2.join();
            //-- Lets re-use current thread, as in above case current thread will be in waiting phase and remain underutilised.
            return f1.compute() + f2.join(); //-- Now we are using current thread for executing f1
        }
    }

    public static void main(final String[] args) {
        ForkJoinPool pool = new ForkJoinPool(2);
        try {
            System.out.println("Fibonacci of 33 => " + pool.invoke(new FibonacciGenerator(33)));
        } finally {
            pool.close();
        }
    }
}
