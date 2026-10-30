package model;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class FileSerializationDemo {

    public static void main(String[] args) {
        BankRecord record = new BankRecord(1001, "Yatri", "pass123");

        try (ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream("bankrecord.dat"))) {
            out.writeObject(record);
            System.out.println("Object saved successfully.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (ObjectInputStream in =
                new ObjectInputStream(new FileInputStream("bankrecord.dat"))) {
            BankRecord loaded = (BankRecord) in.readObject();
            System.out.println("Loaded object:");
            System.out.println(loaded);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}