package co.in.nnj.learn.fun;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Ex9_FlatMap {
    public static void main(final String[] args) {
        final List<Integer> list1 = List.of(1, 2, 3, 4, 5);
        final List<Integer> list2 = List.of(6, 7, 8, 9, 10);

        /**
         * If some function return list where each element of list is also list.
         * But in such case, flatmap help us, to create single stream outoff those list.
         **/
        List.of(list1, list2).stream().flatMap(list -> list.stream())
                .forEach(System.out::println);

        /**
         * Another example - Read lines from file and split each lines to word.
         **/
        try {
            final Path path = Paths.get(Ex9_FlatMap.class.getResource("Words.txt").toURI());
            final List<String> allLines = Files.readAllLines(path);
            allLines.stream().flatMap(line -> Arrays.stream(line.split(" ")))
                    .forEach(System.out::println);
        } catch (final URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
