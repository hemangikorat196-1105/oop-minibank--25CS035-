import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentService service = new StudentService();

        while (true) {

            System.out.println("\n--- Student Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. Count Students");
            System.out.println("3. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {

                sc.nextLine();

                System.out.print("Enter name: ");
                String name = sc.nextLine();

                System.out.print("Enter age: ");
                int age = sc.nextInt();

                service.addStudent(new Student(name, age));

                System.out.println("Student added.");

            } else if (choice == 2) {

                System.out.println(
                    "Total students: " + service.getStudentCount()
                );

            } else if (choice == 3) {

                System.out.println("Exiting...");
                break;

            } else {

                System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }
}