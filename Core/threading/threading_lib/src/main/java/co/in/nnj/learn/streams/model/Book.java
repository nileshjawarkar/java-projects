package co.in.nnj.learn.streams.model;

public class Book {

    public static enum Type {
       FANTASY, MYSTERY, THRILLER, NOVEL, HISTORY, SCIENCE_FICTION, UNCATEGORISED 
    }

    private final String title;
    private final int pages;
    private final String author;
    private final double price;
    private final Type type;

    private Book(final Builder builder) {
        this.title = builder.title;
        this.pages = builder.pages;
        this.author = builder.author;
        this.price = builder.price;
        this.type = builder.type;
    }

    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public static Builder init() {
        return new Builder();
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Book{title=" + title + ", author=" + author + ", price=" + price + ", pages=" + pages + "}";
    }

    public static class Builder {
        private String title = "";
        private int pages = 0;
        private String author = "";
        private double price = 0.00;
        private Type type = Type.UNCATEGORISED;

        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder withType(final Type type) {
            this.type = type;
            return this;
        }
        public Builder withAuthor(final String author) {
            this.author = author;
            return this;
        }

        public Builder withPages(final int pages ) {
            this.pages = pages;
            return this;
        }

        public Builder withPrice(final double price) {
            this.price = price;
            return this;
        }

        public Book get() {
            return new Book(this);
        }
    }
}
