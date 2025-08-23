package co.in.nnj.learn.fun;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Ex11_Collectors {

    public static void main(final String[] args) {
        try {
            final Path path = Paths.get(Ex11_Collectors.class.getResource("EmpData.txt").toURI());
            try (Stream<String> lineStream = Files.lines(path);) {
                final Stream<String> wordStream = lineStream.flatMap(line -> Arrays.stream(line.split(",")));
                final EmployeeSplitrator spliterator = new EmployeeSplitrator(wordStream.spliterator());
                final List<Employee> emps = StreamSupport.stream(spliterator, false).collect(Collectors.toList());
                //-- System.out.println(emps);
                
                //-- Print emp ids
                final Set<Long> empIds = emps.stream().map( e -> e.getId()).collect(Collectors.toSet());
                System.out.println(empIds);
                System.out.println("------x---------(Set)---------x--------");

                //-- Print sorted list by salary
                final TreeSet<Employee> sortedList = emps.stream().collect(Collectors.toCollection(() -> new TreeSet<>()));
                System.out.println(sortedList);
                System.out.println("------x---------(Collection)---------x--------");

                final Map<Long, String> empNames = emps.stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
                System.out.println(empNames);
                System.out.println("------x---------(Map)---------x--------");

                //-- Group by
                final Map<String, List<Employee>> groupByList = emps.stream().collect(Collectors.groupingBy(e -> e.getDesignation()));
                System.out.println(groupByList);
                System.out.println("------x---------(Map - Group by designation)---------x--------");
            }
        } catch (final URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
