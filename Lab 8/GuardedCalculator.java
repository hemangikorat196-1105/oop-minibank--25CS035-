import java.util.Scanner;

class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {

    public static double calculate(double a, double b, char operator)
            throws DivideByZeroException {

        if (operator == '/' && b == 0) {
            throw new DivideByZeroException("Cannot divide by zero.");
        }

        switch (operator) {
            case '+':
                return a + b;

            case '-':
                return a - b;

            case '*':
                return a * b;

            case '/':
                return a / b;

            default:
                throw new IllegalArgumentException("Invalid operator.");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            try {
                System.out.print("Enter first number: ");
                double a = Double.parseDouble(sc.nextLine());

                System.out.print("Enter operator (+, -, *, /): ");
                char operator = sc.nextLine().charAt(0);

                System.out.print("Enter second number: ");
                double b = Double.parseDouble(sc.nextLine());

                double result = calculate(a, b, operator);

                System.out.println("Result = " + result);

                break;

            } catch (NumberFormatException e) {

                System.out.println("Invalid number input. Please enter numbers only.");

            } catch (DivideByZeroException e) {

                System.out.println("Error: " + e.getMessage());

            } catch (IllegalArgumentException e) {

                System.out.println("Error: " + e.getMessage());

            } finally {

                System.out.println("Attempt logged.\n");
            }
        }

        sc.close();
    }
}