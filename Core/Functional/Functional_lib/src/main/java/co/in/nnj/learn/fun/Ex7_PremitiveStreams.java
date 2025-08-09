package co.in.nnj.learn.fun;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Ex7_PremitiveStreams {

    public static void main(final String[] args) {

        final List<Book> books = Book.getBooks();
        // -- map and reduce
        //-- *** final Double totalPrice = books.stream().map(b -> b.getPrice()).reduce(0.0d, (p1, p2) -> p1 + p2);
        //-- In above case, we want to calculate sum.
        //-- Java already proved DoubleStream, we can utilise above operation, in more effective way.
        final DoubleStream doubleStream = books.stream().mapToDouble(b -> b.getPrice());
        System.out.println("Total price = " + doubleStream.sum());
        //-- *** This live will raise error.. one stream can be used once. When terminal 
        //-- operation executed on it, it is in invalid state.
        //-- System.out.println("Average price = " + doubleStream.average()); 
        //
        //-- *** So now to get average, we need new stream
        final OptionalDouble average = books.stream().mapToDouble(b -> b.getPrice()).average();
        System.out.println("Total price = " + average.getAsDouble());

        //-- *** We also have 2 other premitive streams -> IntStream, LongStream
        final OptionalDouble average2 = IntStream.of(10, 22, 5, 44, 11, 88, 331, 9, 50)
            .average();
        System.out.println("Average of nums = " + average2.getAsDouble());

        final OptionalDouble average3 = LongStream.of(10L, 22L, 5L, 44L, 11L, 88L, 331L, 9L, 50L)
            .average();
        System.out.println("Average of nums = " + average3.getAsDouble());
    }
}
