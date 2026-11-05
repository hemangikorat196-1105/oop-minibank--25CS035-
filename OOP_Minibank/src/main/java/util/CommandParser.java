package util;
import model.Command;
import model.TransactionType;

public final class CommandParser {
    private CommandParser() { }
    public static Command parse(String line) {
        if (line == null || line.isBlank()) throw new IllegalArgumentException("Command cannot be empty.");
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) throw new IllegalArgumentException("Expected exactly 3 parts: DEPOSIT AC0001 500");
        TransactionType type;
        try { type = TransactionType.valueOf(parts[0].toUpperCase()); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("Type must be DEPOSIT, WITHDRAW or TRANSFER."); }
        if (!parts[1].matches("AC[0-9]{4,}")) throw new IllegalArgumentException("Invalid account number.");
        if (!Validator.isValidAmount(parts[2])) throw new IllegalArgumentException("Amount must be a positive whole number.");
        return new Command(type, parts[1], Long.parseLong(parts[2]));
    }
}
