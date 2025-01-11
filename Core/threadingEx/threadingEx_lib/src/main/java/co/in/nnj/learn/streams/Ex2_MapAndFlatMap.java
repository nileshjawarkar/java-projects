package co.in.nnj.learn.streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Ex2_MapAndFlatMap {

    public static void main(final String[] args) {

        System.out.println("Example 1:");
        // -- Ex1 split string from array and find distinct chars.
        final List<String> list1 = Arrays.asList("manya", "gnya", "banya");

        // -- Split the strings in chars and covert each word to list of chars.
        final List<String[]> list2 = list1.stream().map(s -> s.split("")).collect(Collectors.toList());

        // -- Now we have 2 lists for 2 word. But in order to find distinct chars, we
        // need to
        // -- create single stream for these 2 list. We can do this by using flatmap
        final List<String> list3 = list2.stream().flatMap(Arrays::stream).distinct().collect(Collectors.toList());
        list3.stream().forEach(s -> System.out.print(s + " "));

        /*
         * About code can also writen in single line as follows -
         * list1.stream().map(s -> s.split(""))
         * .flatMap(Arrays::stream).distinct()
         * .forEach(System.out::println);
         */

        System.out.println("\n\nExample 2:");
        // Ex2 - Create pair of int values in 2 arrays.
        // For example, for following 2 array [1, 2, 3] and [4, 5], we need to create
        // following pairs - [1, 4], [1, 5], [2, 4], [2, 5], [3, 4], [3, 5]
        final List<Integer> numList1 = Arrays.asList(1, 2, 3);
        final List<Integer> numList2 = Arrays.asList(4, 5);

        // -- Step 1: Create list for each combination
        // -- Map always create a stream, so here we will get Stream<List<Integer>> for each map execution.
        // -- Then flatmap will flaten them for collect and collect convert the result as List<List<Integer>>
        final List<List<Integer>> resList1 = numList1.stream()
                .flatMap(n -> numList2.stream().map(n1 -> Arrays.asList(n, n1)))
                .collect(Collectors.toList());
        resList1.stream().forEach(l -> System.out.print(l + " "));
    }
}
