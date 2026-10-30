package util;

public class TestCommand {
    public static void main(String[] args) {
        try {
            Command command = CommandParser.parse("DEPOSIT 500");

            System.out.println("Type: " + command.getType());
            System.out.println("Amount: " + command.getAmount());

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
