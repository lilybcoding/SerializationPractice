package org.gradle;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    @Test
    void BookSerializerFromManual() {
        SortedSet<Book> books = new TreeSet<>();
        Book book1 = new Book("1984", "George Orwell", 1949, "9780151660346");
        Book book2 = new Book("Dracula", "Bram Stoker", 1897, "9798721052927");
        Book book3 = new Book("Sense and Sensibility", "Jane Austen", 1811, "97811401687922");

        books.add(book1);
        books.add(book2);
        books.add(book3);

        String filename = Book.serializeToCSV(books);
        SortedSet<Book> deserializedBooks = Book.deserializeFromCSV(filename);

        assertEquals(books, deserializedBooks);
    }

    @Test
    void BookSerializerFromFile() {

        SortedSet<Book> books = new TreeSet<>();
        List<String> bookLines = new ArrayList<>();
        try {
            bookLines = Files.readAllLines(Paths.get("books.txt"));
        } catch (IOException e) {
            System.out.println("Error reading books.txt file");
        }
        int lineNumber = 0;
        boolean invalid;
        for (String line : bookLines) {
            invalid = false;
            lineNumber++;
            String[] split = line.split(",");
            for (int i = 0; i<split.length; i++) {
                split[i] = split[i].trim();
                if (split[i].isEmpty()) {
                    // Do you handle input errors the same in testing vs in a driver class?
                    System.out.println("Empty value in line " + lineNumber + ". Not Adding.");
                    invalid = true;
                    break;
                }
            }
            if  (invalid) {
                continue;
            }
            if (split[3].length() != 13) {
                System.out.println("Invalid ISBN in line " + lineNumber + ". Not Adding.");
            } else {
                Book book = new Book(split[0], split[1], Integer.parseInt(split[2]), split[3]);
                books.add(book);
            }

            String filename = Book.serializeToCSV(books);
            SortedSet<Book> deserializedBooks = Book.deserializeFromCSV(filename);

            assertEquals(books, deserializedBooks);
        }
    }
    @Test
    void BinarySerializerWithBook() {
        Book book1 = new Book("1984", "George Orwell", 1949, "9780151660346");

        String binaryFilename = BinarySerializer.serialize(book1);
        Book book1copy = (Book) BinarySerializer.deserialize(binaryFilename);

        assertEquals(book1, book1copy);
    }

    @Test
    void BinarySerializerWithString() {
        String string = "hello";

        String binaryFilenameString = BinarySerializer.serialize(string);
        String stringCopy = BinarySerializer.deserialize(binaryFilenameString).toString();

        assertEquals(string, stringCopy);
    }

}