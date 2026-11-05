package exception;
public class BankResource implements AutoCloseable {
    public void use(){System.out.println("Bank resource in use.");}
    @Override public void close(){System.out.println("Bank resource closed safely.");}
}
