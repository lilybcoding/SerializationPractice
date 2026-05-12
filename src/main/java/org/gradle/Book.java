package org.gradle;

import java.io.Serializable;
import java.util.*;

public class Book implements Serializable, Comparable<Book> {
    private String title;
    private String author;
    private int publication;
    private String isbn;

    public Book() {}

    public Book(String title, String author, int publication, String isbn) {
        this.title = title;
        this.author = author;
        this.publication = publication;
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, publication, isbn);
    }

    @Override
    public int compareTo(Book o) {
        return this.isbn.compareTo(o.isbn);
    }

    @Override
    public String toString() {
        return  title +
                ", " + author +
                ", " + publication +
                ", " + isbn;
    }
}
