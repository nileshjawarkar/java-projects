package co.in.nnj.learn.fun;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Ex12_Collections {
    public static void main(final String[] args) {
        // -- List
        final List<Book> books = Book.getBooks();

        System.out.println("-----x--(Sorting)---x--------");
        books.stream()
                .sorted((b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice()))
                .forEach(System.out::println);
        System.out.println("-----x--(filter)---x--------");
        final Set<Book> highPriceBooks = books.stream().filter(b -> b.getPrice() > 100).collect(Collectors.toSet());
        highPriceBooks.forEach(System.out::println);

        System.out.println("-----x--(map)---x--------");
        final Set<Double> prices = books.stream()
                .map(b -> b.getPrice()).collect(Collectors.toSet());
        prices.forEach(System.out::println);

        // -- Set
        System.out.println("-----x--(Set:Sorting)---x--------");
        highPriceBooks.stream()
                .sorted((b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice()))
                .forEach(System.out::println);
        System.out.println("-----x--(Set:Filtering)---x--------");
        highPriceBooks.stream().filter(b -> b.getPrice() > 150).forEach(System.out::println);

        // -- Map
        // -- List to map coversion
        final Map<String, Book> titleToBookMap = books.stream().collect(Collectors.toMap(b -> b.getTitle(), b -> b));

        // -- Filter
        titleToBookMap.entrySet().stream()
                .filter(e -> e.getValue().getPrice() > 150)
                .forEach(System.out::println);
    }
}
