package co.in.nnj.learn.fun;

import java.util.List;

class Book {
    private String title;
    private String category;
    private int ratting;
    private double price;

    public Book() {
        title = null;
        category = null;
        price = 0;
        ratting = 0;
    }

    public Book(final String title, final String category) {
        this.title = title;
        this.category = category;
        price = 0;
        ratting = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public int getRatting() {
        return ratting;
    }

    public void setRatting(final int ratting) {
        this.ratting = ratting;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{title=" + title + ", category=" + category + ", ratting=" + ratting + ", price=" + price + "}";
    }

    public static List<Book> getBooks() {
        final Book b1 = new Book("book1", "horror");
        b1.setRatting(2);
        b1.setPrice(101.0);

        final Book b2 = new Book("Horror Cocktail", "horror");
        b2.setRatting(4);
        b2.setPrice(90.0);

        final Book b3 = new Book("House of Leaves", "Horror");
        b3.setRatting(5);
        b3.setPrice(200.0);

        final Book b4 = new Book("The Notebook", "Romance");
        b4.setRatting(4);
        b4.setPrice(110.0);

        final Book b5 = new Book("The Alchemist", "Adventure");
        b5.setRatting(5);
        b5.setPrice(50.0);

        return List.of(b1, b2, b3, b4, b5);
    }
}
