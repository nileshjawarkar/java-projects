package co.in.nnj.learn.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import co.in.nnj.learn.streams.model.Book;
import co.in.nnj.learn.streams.model.Book.Type;

public class Ex4_MapTo {

    public static void main(final String[] args) {
        final List<Book> books = new ArrayList<>();
        books.add(Book.init().withType(Type.NOVEL)
                .withTitle("Book1").withAuthor("Author1")
                .withPages(100).withPrice(300).get());
        books.add(Book.init().withType(Type.SCIENCE_FICTION)
                .withTitle("Book2").withAuthor("Author1")
                .withPages(150).withPrice(350).get());
        books.add(Book.init().withType(Type.SCIENCE_FICTION)
                .withTitle("Book3").withAuthor("Author1")
                .withPages(150).withPrice(350).get());
        books.add(Book.init().withType(Type.FANTASY)
                .withTitle("Book4").withAuthor("Author2")
                .withPages(500).withPrice(600).get());
        books.add(Book.init().withType(Type.HISTORY)
                .withTitle("Book5").withAuthor("Author2")
                .withPages(501).withPrice(500).get());

        // -- Example 1
        // -- Calculate sum of pages of all the books
        final Optional<Integer> sum1 = books.stream().map(Book::getPages)
                .collect(Collectors.reducing((p1, p2) -> p1 + p2));
        sum1.ifPresent(System.out::println);

        // -- Shrt way to doing same thing - use mapToInt to generate
        // -- IntStream which already has sum  method
        final int sum2 = books.stream().mapToInt(Book::getPages).sum();
        System.out.println("Sum of pages = " + sum2);

        // -- Example 2 Get max pages using mapToInt
        final OptionalInt max = books.stream().mapToInt(b -> b.getPages()).max();
        System.out.println("Max Pages = " + max.orElse(0));
    }
}
