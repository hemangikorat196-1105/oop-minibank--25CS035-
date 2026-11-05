package model;
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final long overdraftLimit;
    public CurrentAccount(String owner, long opening, long overdraftLimit, double rate) {
        super(owner, opening); this.overdraftLimit = overdraftLimit;
    }
    public CurrentAccount(String owner, long opening, long overdraftLimit) { this(owner, opening, overdraftLimit, 0.0); }
    @Override public boolean canWithdraw(long amount) { return amount > 0 && amount <= getBalance() + overdraftLimit; }
    @Override public long availableToWithdraw() { return Math.max(0, getBalance() + overdraftLimit); }
    @Override public double interestRate() { return 0.0; }
    public long getOverdraftLimit() { return overdraftLimit; }
}
