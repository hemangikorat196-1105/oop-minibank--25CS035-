package model;
public class FixedDepositAccount extends Account implements InterestBearing {
    private static final long serialVersionUID = 1L;
    public FixedDepositAccount(String owner, long opening, double rate) { super(owner, opening);  }
    public FixedDepositAccount(String owner, long opening) { this(owner, opening, 7.0); }
    @Override public boolean canWithdraw(long amount) { return false; }
    @Override public long availableToWithdraw() { return 0; }
    @Override public double interestRate() { return 7.0; }
}
