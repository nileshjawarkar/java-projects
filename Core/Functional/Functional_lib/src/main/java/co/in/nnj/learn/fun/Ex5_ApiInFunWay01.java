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

        final Function<String, Integer> fun1 = strNum -> Integer.valueOf(strNum);

        //-- Example 3 - mix type chaining
        final var result = fun1.andThen(num -> num * 3).andThen( num -> (double)num / 2).apply("5");
        System.out.println(result.getClass().getSimpleName());

        final Cmd<String, Integer> cmd1 = strNum -> Integer.valueOf(strNum);
        final Double res = cmd1.andThen( num -> num * 5).andThen( num -> (double) num / 2).execute("5");
        System.out.println(res);
    }

    @FunctionalInterface
    static interface Cmd<T,R> {
        public R execute(T t);
        default public <V> Cmd<T,V> andThen(final Cmd<R,V> cmd) {
            return (final T t) -> cmd.execute(execute(t));
        }
    }
}
