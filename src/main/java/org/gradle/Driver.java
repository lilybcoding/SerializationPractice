package org.gradle;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.*;

public class Driver {

    public static void main(String[] args) {
        // create set of books
        SortedSet<Book> books = new TreeSet<>();

        /* create manually
        Book book1 = new Book("1984", "George Orwell", 1949, "9780151660346");
        Book book2 = new Book("Dracula", "Bram Stoker", 1897, "9798721052927");
        Book book3 = new Book("Sense and Sensibility", "Jane Austen", 1811, "97811401687922");

        books.add(book1);
        books.add(book2);
        books.add(book3);
         */

        //read from file
        List<String> bookLines = new ArrayList<>();
        try {
            bookLines = Files.readAllLines(Paths.get("books.txt"));
        } catch (IOException e) {
            System.out.println("Error reading books.txt file");
        }
        int lineNumber = 0;
        boolean invalid = false;
        for (String line : bookLines) {
            invalid = false;
            lineNumber++;
            String[] split = line.split(",");
            for (int i = 0; i<split.length; i++) {
                split[i] = split[i].trim();
                if (split[i].equals("")) {
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
                //System.out.println(book);
                books.add(book);
            }

        }

        // serialize
        try {
            FileOutputStream file = new FileOutputStream("book.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            for (Book book : books) {
                out.writeObject(book);
            }
            out.close();
            file.close();
            System.out.println("\nSerialization complete.");
        } catch (IOException e) {
            System.out.println("Error writing book.ser");
        }
        // create another by deserializing
        SortedSet<Book> deserializedBooks = new TreeSet<>();
        try {
            FileInputStream file = new FileInputStream("book.ser");
            ObjectInputStream in = new ObjectInputStream(file);
            while (true) {
                try {
                    Book book = (Book) in.readObject();
                    deserializedBooks.add(book);
                } catch (EOFException e) {
                    break; // stops when no more objects to read
                }
            }
            in.close();
            file.close();
            System.out.println("Deserialization complete.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading book.ser");
        }

        // compare
        boolean equals = true;
        if (books.size() == deserializedBooks.size()) {
            Iterator<Book> booksIterator = books.iterator();
            Iterator<Book> deserializedIterator = deserializedBooks.iterator();
            while (booksIterator.hasNext() && deserializedIterator.hasNext()) {
                if (!booksIterator.next().equals(deserializedIterator.next())) {
                    equals = false;
                }
            }
        }
        if (equals) {
            System.out.println("Serialization matches.");
            System.out.println("\nComplete list of books:");
            for (Book book : deserializedBooks) {
                System.out.println(book);
            }
        } else {
            System.out.println("Serialization does not match.");
        }
    }
}
