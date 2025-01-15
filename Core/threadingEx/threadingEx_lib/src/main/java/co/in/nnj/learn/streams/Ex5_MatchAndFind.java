package co.in.nnj.learn.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        boolean allMatch = books.stream().allMatch(b -> b.getPages() >= 100);
        System.out.println("Is all elems gretorthan 100 : " + allMatch);

        allMatch = books.stream().allMatch(b -> b.getPages() >= 300);
        System.out.println("Is all elems gretorthan 300 : " + allMatch);

        boolean noneMatch = books.stream().noneMatch(b -> b.getPages() > 500);
        System.out.println("Is none of them gretorthan 500 : " + noneMatch);

        noneMatch = books.stream().noneMatch(b -> b.getPages() > 700);
        System.out.println("Is none of them gretorthan 700 : " + noneMatch);

        final boolean anyOne = books.stream().anyMatch(b -> b.getPages() > 500);
        System.out.println("Is one of them gretorthan 500 : " + anyOne);

        final Optional<Book> any = books.stream().filter(b -> b.getPages() > 400).findAny();
        final long count = books.stream().filter(b -> b.getPages() > 400).count();
        System.out.println("any of " + count + " gretorthan 400 =" + any.get());

        final Optional<Book> first = books.stream()
                .filter(b -> b.getPages() > 400)
                .sorted((a, b) -> {
                    if (a.getPages() > b.getPages()) {
                        return -1;
                    } else if (a.getPages() < b.getPages()) {
                        return 1;
                    }
                    return 0;
                }).findFirst();
        System.out.println("First of " + count + " gretorthan 400 =" + first.get());
    }
}
