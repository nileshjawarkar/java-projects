package co.in.nnj.learn.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.in.nnj.learn.streams.model.Book;
import co.in.nnj.learn.streams.model.Book.Type;

public class Ex3_CategoriesAndLimit {
    public static void main(final String[] args) {
        // Ex1 - Find max number of pages
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

        // -- Group books by category
        final Map<Type, List<Book>> grpedBooks = books.stream().collect(Collectors.groupingBy(Book::getType));

        System.out.println("Example 1 = books categorised\n");
        grpedBooks.entrySet().stream().forEach(System.out::println);

        // -- Get top 2 books with max pages
       final List<Book> top2BooksWithMaxPage = books.stream().sorted((b1, b2) -> {
            if (b1.getPages() > b2.getPages()) {
                return -1;
            } else if (b1.getPages() < b2.getPages()) {
                return 1;
            }
            return 0;
        }).limit(2).collect(Collectors.toList());

        System.out.println("\n\nExample 2 = Top 2 books with max number of page\n");
        top2BooksWithMaxPage.stream().forEach(System.out::println);
    }
}
