package org.gradle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    private SortedSet<Book> books;
    private String CSV_filename;
    private String binaryFilenameString;
    private Book book1;
    private String book1File;
    private String XML_filename;

    @BeforeEach
    void setUp() {
        CSV_filename = "books.csv";
        binaryFilenameString = "string.csv";
        book1File = "book1.txt";
        XML_filename = "books.xml";
        books = new TreeSet<>();
        book1 = new Book("1984", "George Orwell", 1949, "9780151660346");
        Book book2 = new Book("Dracula", "Bram Stoker", 1897, "9798721052927");
        Book book3 = new Book("Sense and Sensibility", "Jane Austen", 1811, "97811401687922");

        books.add(book1);
        books.add(book2);
        books.add(book3);
    }

    // Not sure what goes in tearDown --> put books.clear() for now
    @AfterEach
    void tearDown() {

    }

    @Test
    void BookSerializerCSV() {

        Book.serializeToCSV(books, CSV_filename);
        SortedSet<Book> deserializedBooks = Book.deserializeFromCSV(CSV_filename);

        assertEquals(books, deserializedBooks);
    }

    @Test
    void BookSerializerXML() {
        Book.serializeToXML(books, XML_filename);
        SortedSet<Book> deserializedBooks = Book.deserializeFromXML(XML_filename);

        assertEquals(books, deserializedBooks);
    }

    @Test
    void BinarySerializerWithBook() {
        BinarySerializer.serialize(book1, book1File);
        Book book1copy = (Book) BinarySerializer.deserialize(book1File);

        assertEquals(book1, book1copy);
    }

    @Test
    void BinarySerializerWithBooks() {

        BinarySerializer.serialize(books, CSV_filename);
        @SuppressWarnings("unchecked")
        SortedSet<Book> deserializedBooks = (SortedSet<Book>) BinarySerializer.deserialize(CSV_filename);

        assertEquals(books, deserializedBooks);
    }

    @Test
    void BinarySerializerWithString() {
        String string = "hello";

        BinarySerializer.serialize(string, binaryFilenameString);
        String stringCopy = BinarySerializer.deserialize(binaryFilenameString).toString();

        assertEquals(string, stringCopy);
    }

}