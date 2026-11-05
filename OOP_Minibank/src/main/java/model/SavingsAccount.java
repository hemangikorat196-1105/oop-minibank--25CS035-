package model;
public class SavingsAccount extends Account implements InterestBearing, WithdrawRule, Premium {
    private static final long serialVersionUID = 1L;
    private final long minBalance;
    public SavingsAccount(String owner, long opening, long minBalance, double rate) {
        super(owner, opening); this.minBalance = minBalance;
    }
    public SavingsAccount(String owner, long opening) { this(owner, opening, 0, 4.0); }
    public SavingsAccount(String owner, long opening, long minBalance) { this(owner, opening, minBalance, 4.0); }
    @Override public boolean canWithdraw(long amount) { return amount > 0 && getBalance() - amount >= minBalance; }
    @Override public long availableToWithdraw() { return Math.max(0, getBalance() - minBalance); }
    @Override public double interestRate() { return 4.0; }
    @Override public boolean allow(Account account, long amount) { return account.getBalance() - amount >= minBalance; }
    public long getMinBalance() { return minBalance; }
}
