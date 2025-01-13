package co.in.nnj.learn.streams;

import java.util.ArrayList;
import java.util.List;

import co.in.nnj.learn.streams.model.Book;
import co.in.nnj.learn.streams.model.Book.Type;

public class Ex5_MatchAndFind {

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

        final boolean allMatch1 = books.stream().map(b -> b.getPages()).allMatch(p -> p >= 500);
        final boolean allMatch2 = books.stream().allMatch(b -> b.getPages() >= 100);
        System.out.println(allMatch1 + ", " + allMatch2);
    }
}
