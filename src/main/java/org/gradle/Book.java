package org.gradle;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Book implements Serializable, Comparable<Book> {
    private final String title;
    private final String author;
    private final int publicationYear;
    private final String isbn;


    public Book(String title, String author, int publicationYear, String isbn) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }

    public static void serializeToCSV(SortedSet<Book> books, String filename) {
        try {
            Path path = Paths.get(filename);
            for (Book book : books) {
                String content = book.prettyPrintToCSV();
                Files.write(path, (content + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SortedSet<Book> deserializeFromCSV(String csv) {
        List<String> bookLines = new ArrayList<>();
        try {
            bookLines = Files.readAllLines(Paths.get(csv));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SortedSet<Book> deserializedBooks = new TreeSet<>();
        for (String line : bookLines) {
            String[] split = line.split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].trim();
            }
            Book book = new Book(split[0], split[1], Integer.parseInt(split[2]), split[3]);
            deserializedBooks.add(book);
            System.out.println(deserializedBooks);
        }
        return deserializedBooks;
    }

    public static void serializeToXML(SortedSet<Book> books, String filename) {
        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.alias("book", Book.class);
            xstream.alias("books", Book[].class);
            for (Book book : books) {
                String xmlContent = xstream.toXML(book);
                Path path = Paths.get(filename);
                Files.write(path, (xmlContent + "\n").getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SortedSet<Book> deserializeFromXML(String filename) {
        SortedSet<Book> deserializedBooks = new TreeSet<>();
        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.allowTypes(new Class[]{Book.class});
            xstream.alias("book", Book.class);
            xstream.alias("books", Book[].class);

            String content = new String(Files.readAllBytes(Paths.get(filename)));
            Book[] bookWrapper = (Book[])xstream.fromXML(content);
            for (Book book : bookWrapper) {
                Book tempBook = new Book(book.title, book.author, book.publicationYear, book.isbn);
                deserializedBooks.add(tempBook);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return deserializedBooks;
    }

    public String prettyPrintToCSV() {
        return title + ", "
                + author + ", "
                + publicationYear + ", "
                + isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, publicationYear, isbn);
    }

    @Override
    public int compareTo(Book o) {
        return this.isbn.compareTo(o.isbn);
    }

    @Override
    public String toString() {
        return title +
                ", " + author +
                ", " + publicationYear +
                ", " + isbn;
    }
}
