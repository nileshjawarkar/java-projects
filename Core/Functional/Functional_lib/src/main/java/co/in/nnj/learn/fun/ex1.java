package co.in.nnj.learn.fun;

public class ex1 {

    @FunctionalInterface
    public interface Operation {
        int apply(int a, int b);
    }

    public static void main(final String[] args) {
        final Operation addOperation = new Operation() {
            @Override
            public int apply(final int a, final int b) {
                return a + b;
            }
        };
        System.out.println(addOperation.apply(20, 11));

        final Operation subOperation = (a, b) -> {
            return (a - b);
        };
        System.out.println(subOperation.apply(20, 11));
    }
}
