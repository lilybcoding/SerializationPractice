package org.gradle;

import java.io.*;

public class BinarySerializer {

    // should I also have methods to serialize/deserialize sets of objects?
    public static String serialize(Object object) {
        try {
            FileOutputStream file = new FileOutputStream("serialized.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(object);
            out.close();
            file.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return "serialized.ser";
    }

    public static Object deserialize(String filename) {
        Object object;
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            object = in.readObject();
            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return object;
    }
}
