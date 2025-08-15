package co.in.nnj.learn.fun;

import java.util.Random;
import java.util.stream.Stream;

public class Ex8_InfiniteStream {

    public static void streamIterate() {
        Stream.iterate(1, i -> i + 1)
                .limit(15)
                .forEach(System.out::println);
    }

    public static void streamGenerate() {
        /* Stream.generate(() -> Math.random() * 1000)
                .limit(15)
                .forEach(System.out::println); */
        Stream.generate(() -> new Random().nextInt(0, 1000))
                .limit(15)
                .forEach(System.out::println);
    }

    public static void main(final String[] args) {
        streamIterate();
        streamGenerate();
    }
}
