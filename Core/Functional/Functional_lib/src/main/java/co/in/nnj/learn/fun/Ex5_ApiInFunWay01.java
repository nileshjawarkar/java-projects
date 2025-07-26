package co.in.nnj.learn.fun;

import java.util.function.Function;

public class Ex5_ApiInFunWay01 {
    /**
     * Functional chaining
     **/
    public static void main(final String[] args) {
        //-- Example 1
        final Function<Integer, Integer> f1 = num -> num + 2;
        final Function<Integer, Integer> f2 = num -> num * 2;
        final Function<Integer, Integer> f3 = f1.andThen(f2);
        System.out.println(f3.apply(10));

        //-- Example 2
        final int out = f1.andThen(num -> num * 4).andThen(num -> num / 2).apply(10);
        System.out.println(out);
    }
}
