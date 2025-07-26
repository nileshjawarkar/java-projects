package co.in.nnj.learn.fun;

import java.util.function.Function;
import java.util.function.Supplier;

public class Ex4_HigherOrderFunctions {

    /**
     * Higher order function is the function which
     * 1) Take function as input
     * 2) Return function as input
     * 3) Taken function as input and return function as output
     **/

    public static void main(final String[] args) {
        final Supplier<Integer> randomNumFactory = createFactory(() -> Math.random() * 100, (num) -> num.intValue());
        System.out.println(randomNumFactory.get());
        System.out.println(randomNumFactory.get());
    }

    private static <T, R> Supplier<R> createFactory(final Supplier<T> producer, final Function<T, R> configurator) {
        return () -> {
            return configurator.apply(producer.get());
        };
    }
}
