package co.in.nnj.learn.streams;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Ex1_FilterAndSorting {
    public static void main(final String[] args) {
        // -- int streams
        // Printing
        // -- Stream.of(1, 20, -9, 30, 5, 8, 10).forEach(System.out::println);;
        // -- IntStream.range(1, 6).forEach(System.out::println);
        System.out.println("Foreach Example =>");
        IntStream.range(1, 6).forEach(n -> System.out.print(n + " "));
        System.out.println("\nFilter Example - Print even numbers =>");
        IntStream.range(1, 30).filter(n -> (n % 2) == 0).forEach(n -> System.out.print(n + " "));

        System.out.println("\nFilter Example - Print name starting with b=>");
        Stream.of("banya", "ganya", "manya", "bandya")
            .filter(s -> s.startsWith("b"))
            .forEach(s -> System.out.print(s + ", "));

        System.out.println("\nSort Example =>");
        Stream.of("banya", "ganya", "manya", "bandya")
            .sorted()
            .forEach(s -> System.out.print(s + ", "));
        System.out.println("\nSort Example / reverseOrder =>");
        Stream.of("banya", "ganya", "manya", "bandya")
            .sorted(Comparator.reverseOrder())
            .forEach(s -> System.out.print(s + ", "));
    }
}
