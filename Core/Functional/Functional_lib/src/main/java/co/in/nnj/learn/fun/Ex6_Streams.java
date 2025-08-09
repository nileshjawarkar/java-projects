package co.in.nnj.learn.fun;

import java.util.List;
import java.util.stream.Collectors;

public class Ex6_Streams {

    public static void main(final String[] args) {
        final List<Book> books = Book.getBooks();
        // -- *** filter
        final List<Book> horrorBooks = books.stream()
                .filter(b -> b.getCategory().compareToIgnoreCase("horror") == 0)
                .filter(b -> b.getRatting() > 3)
                .collect(Collectors.toList());

        horrorBooks.forEach(System.out::println);
        System.out.println("\n");

        // -- *** map
        final List<String> titles = books.stream().map(b -> b.getTitle()).collect(Collectors.toList());
        titles.forEach(n -> System.out.println(n + "\n"));

        // -- *** map and reduce
        //-- final Optional<Double> totalPriceOpt = books.stream().map(b -> b.getPrice()).reduce((p1, p2) -> p1 + p2);
        final Double totalPrice = books.stream().map(b -> b.getPrice()).reduce(0.0d, (p1, p2) -> p1 + p2);
        //-- *** reduce(0.0d, (p1, p2) -> p1 + p2);
        //--             ^      ^
        //--             |      L__ Result identity value <-> passed as input    
        //--        Init identity value 
        //-- 1) Identity value is one of the value passed to binary function provided to reduce.
        //--    In this case, it will be value of p1.. it will hold value of previous operation.
        //--    In this case, sum of values done till now.
        //-- 2) If not provided, by default its start value is zero;
        System.out.println("Total price = " + totalPrice);
    }

}
