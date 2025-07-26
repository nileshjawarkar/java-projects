package co.in.nnj.learn.fun;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Ex3_PredefinedFuncInterfaces {
    /*
     * Java provides following fucntional interfaces in java.util.function package
     * 1) Predicate -> Take 1 input and return boolean, isOdd(T) -> boolean
     * 2) Producer -> Take o input and return 1 output, genInteger() -> R
     * 3) Consumer -> Take 1 input and return nothing, printNum(T) -> void
     * 4) Fuctional -> Take 1 input and return 1 output, convert(T) -> R
     */

    public static void executeCheck(final Predicate<Integer> isOdd, final int num, final String msg) {
        if (isOdd.test(num)) {
            System.out.println(msg);
        }
    }

    public static void main(final String[] args) {
        // -- Predicate example
        executeCheck(n -> (n % 2) != 0, 51, "51 - Odd number");
        executeCheck(n -> (n % 2) == 0, 50, "50 - Even number");

        // -- Producer / Supplier example
        final Supplier<Integer> randomSupplier = () -> (int) (Math.random() * 100);
        System.out.println(randomSupplier.get());
        System.out.println(randomSupplier.get());

        // -- Consumer example
        //-- final Consumer<Integer> randomIntConsumer = System.out::println;
        final Consumer<Integer> randomIntConsumer = num -> System.out.println(num);
        randomIntConsumer.accept(randomSupplier.get());
        randomIntConsumer.accept(randomSupplier.get());

        //-- functional use example
        final Function<String, Integer> converter = str -> Integer.valueOf(str);
        randomIntConsumer.accept(converter.apply("100"));
    }
}
