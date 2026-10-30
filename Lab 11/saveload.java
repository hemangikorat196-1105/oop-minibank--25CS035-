import java.io.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    int age;
    transient String password;

    Student(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Password: " + password);
    }
}

public class saveload {
    public static void main(String[] args) {

        Student[] students = {
            new Student("Hemangi", 20, "abc123"),
            new Student("Dhanushi", 21, "xyz456"),
            new Student("Khushi", 20, "pqr789")
        };

        try {
            ObjectOutputStream out =new ObjectOutputStream(new FileOutputStream("students.dat"));

            out.writeObject(students);
            out.close();

            System.out.println("Objects saved successfully.");

        } catch (IOException e) {
            System.out.println("Error while saving: " + e.getMessage());
        }

        try {
            ObjectInputStream in =new ObjectInputStream(new FileInputStream("students.dat"));

            Student[] loadedStudents = (Student[]) in.readObject();
            in.close();

            System.out.println("\nObjects loaded successfully.");
            System.out.println("Values after loading:");

            for (Student s : loadedStudents) {
                s.display();
                System.out.println();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading: " + e.getMessage());
        }
    }
}