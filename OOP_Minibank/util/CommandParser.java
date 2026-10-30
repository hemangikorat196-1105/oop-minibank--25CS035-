package util;

public class CommandParser {

    public static Command parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty.");
        }

        String[] parts = input.trim().split("\\s+");

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Enter command in format: DEPOSIT 500");
        }

        TransactionType type;

        try {
            type = TransactionType.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type.");
        }

        double amount;

        try {
            amount = Double.parseDouble(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be numeric.");
        }

        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        return new Command(type, amount);
    }
}
