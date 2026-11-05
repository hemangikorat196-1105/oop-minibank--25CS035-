package exception;
public class InvalidAmountException extends BankException {
    private static final long serialVersionUID = 1L;
    public InvalidAmountException() { super("Amount must be a positive whole number."); }
}
