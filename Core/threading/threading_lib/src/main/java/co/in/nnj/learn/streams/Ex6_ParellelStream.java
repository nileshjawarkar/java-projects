package co.in.nnj.learn.streams;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import co.in.nnj.learn.util.TimeLogger;

public class Ex6_ParellelStream {

    public static long sum(final List<Long> numList) {
        return numList.stream().reduce(0L, Long::sum);
    }

    public static long sum_parallel(final List<Long> numList) {
        return numList.stream().parallel().reduce(0L, Long::sum);
    }

    public static void main(final String[] args) {
        final List<Long> longList = LongStream.range(1, 10000000).boxed().collect(Collectors.toList());
        final TimeLogger logger = new TimeLogger();
        logger.init("single threaded - sum");
        System.out.println("sum = " + sum(longList));
        logger.logTimeDiff();
        logger.init("parallel - sum");
        System.out.println("sum = " + sum_parallel(longList));
        logger.logTimeDiff();
    }
}
