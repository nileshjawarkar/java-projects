package co.in.nnj.learn.fun;

public class Ex2_FuncInterfaceWithLamda {

    @FunctionalInterface
    public interface Operation<T> {
        T apply(T a, T b);
    }

    public static void main(final String[] args) {
        final Operation<Integer> addOperation = (a, b) -> {
            return a + b;
        };

        System.out.println(addOperation.apply(20, 11));

        final Operation<Integer> subOperation = (a, b) -> {
            return (a - b);
        };
        System.out.println(subOperation.apply(20, 11));

        final Operation<String> apendOp = (a, b) -> a + b;
        System.out.println(apendOp.apply("str1", "str2"));
    }
}
