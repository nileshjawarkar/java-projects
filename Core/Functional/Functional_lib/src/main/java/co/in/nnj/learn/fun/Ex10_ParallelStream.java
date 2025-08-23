package co.in.nnj.learn.fun;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ex10_ParallelStream {

    private static class TimeLogger {
        long start;

        public TimeLogger() {
            start = 0;
        }

        public void start() {
            start = System.currentTimeMillis();
        }

        public void end() {
            if (start == 0) {
                throw new IllegalStateException("Must start logging before ending it.");
            }
            final long end = System.currentTimeMillis();
            System.out.println("Total time taken - " + (end - start));
            start = 0;
        }
    }

    public static void main(final String[] args) {
        final List<Employee> employees = Stream.generate(() -> {
            final Random random = new Random();
            final String name = "name-" + random.nextInt(1, 100000);
            final double sal = random.nextDouble(10000, 200000);
            return Employee.builder()
                    .withName(name).withSalary(sal)
                    .withCity("Pune").withDesignation("Developer")
                    .build();
        }).limit(5000).collect(Collectors.toList());

        final TimeLogger logger = new TimeLogger();
        logger.start();
        final long numEmp1 = employees.stream()
                .filter(emp -> emp.getSalary() > 15000)
                .count();
        System.out.println("numEmp1 = " + numEmp1);
        logger.end();

        logger.start();
        final long numEmp2 = employees.parallelStream()
                .filter(emp -> emp.getSalary() > 15000)
                .count();
        System.out.println("numEmp2 = " + numEmp2);
        logger.end();
    }
}
