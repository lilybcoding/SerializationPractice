package org.gradle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Book implements Serializable, Comparable<Book> {
    private final String title;
    private final String author;
    private final int publicationYear;
    private final String isbn;

    // Do I need this since I am using the next one for deserialization?
    /*
    public Book() {
        this.title = "";
        this.author = "";
        this.publicationYear = 0;
        this.isbn = "";
    }

     */

    public Book(String title, String author, int publication, String isbn) {
        this.title = title;
        this.author = author;
        this.publicationYear = publication;
        this.isbn = isbn;
    }

    public static String serializeToCSV(SortedSet<Book> books) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("books.csv"));
            for (Book book : books) {
                // is this the best way to print this?
                writer.write(book.title + ", " + book.author + "," + book.publicationYear + "," + book.isbn);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "books.csv";
    }

    public static SortedSet<Book> deserializeFromCSV(String csv) {
        List<String> bookLines = new ArrayList<>();
        try {
            bookLines = Files.readAllLines(Paths.get(csv));
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        SortedSet<Book> deserializedBooks = new TreeSet<>();
        for (String line : bookLines) {
            String[] split = line.split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].trim();
            }
            Book book = new Book(split[0], split[1], Integer.parseInt(split[2]), split[3]);
            deserializedBooks.add(book);
        }
        return deserializedBooks;
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
