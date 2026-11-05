package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.Positive;
import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Transactable, Comparable<Account>, Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private static long accountCounter = 0;
    @Id private final String accountNumber;
    private String ownerName;
    @Positive private long balance;
    private boolean active = true;

    protected Account(String ownerName, long openingBalance) {
        if (ownerName == null || ownerName.isBlank()) throw new IllegalArgumentException("Owner name is required.");
        if (openingBalance < 0) throw new IllegalArgumentException("Opening balance cannot be negative.");
        this.accountNumber = nextAccountNumber();
        this.ownerName = ownerName.trim();
        this.balance = openingBalance;
    }

    protected Account(String ownerName) { this(ownerName, 0); }

    private static synchronized String nextAccountNumber() {
        return String.format("AC%04d", ++accountCounter);
    }

    /** Updates the in-memory counter after loading previously serialized accounts. */
    public static synchronized void synchronizeCounter(Account[] accounts) {
        for (Account account : accounts) {
            String id = account.getAccountNumber();
            try { accountCounter = Math.max(accountCounter, Long.parseLong(id.substring(2))); }
            catch (RuntimeException ignored) { /* Keep the current counter for nonstandard ids. */ }
        }
    }

    // Intentionally unsafe: used only by the Practical 9 race-condition demonstration.
    public void depositUnsafe(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        long current = balance;
        Thread.yield();
        balance = Math.addExact(current, amount);
    }

    @Override public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException();
        try { balance = Math.addExact(balance, amount); }
        catch (ArithmeticException e) { throw new InvalidAmountException(); }
    }

    @Override public synchronized boolean withdraw(long amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) throw new InvalidAmountException();
        if (!active || !canWithdraw(amount)) throw new InsufficientFundsException(Math.max(0, amount - availableToWithdraw()));
        balance = Math.subtractExact(balance, amount);
        return true;
    }

    public abstract boolean canWithdraw(long amount);
    public long availableToWithdraw() { return Math.max(0, getBalance()); }
    public abstract double interestRate();
    public double monthlyInterest() { return getBalance() * interestRate() / 100.0 / 12.0; }
    public synchronized long getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public boolean isActive() { return active; }
    public synchronized void close() { active = false; }

    /** Atomic transfer with deterministic account-number lock ordering. */
    public boolean transferTo(Account target, long amount) {
        Objects.requireNonNull(target, "Target account is required.");
        if (target == this) throw new IllegalArgumentException("Cannot transfer to the same account.");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        Account first = compareTo(target) < 0 ? this : target;
        Account second = first == this ? target : this;
        synchronized (first) {
            synchronized (second) {
                if (!active || !target.active || !canWithdraw(amount)) return false;
                long newTargetBalance = Math.addExact(target.balance, amount);
                this.balance = Math.subtractExact(this.balance, amount);
                target.balance = newTargetBalance;
                return true;
            }
        }
    }

    /** Exception-based transfer wrapper used by the service layer (Practical 8). */
    public void transfer(Account target, long amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) throw new InvalidAmountException();
        try {
            if (!transferTo(target, amount)) throw new InsufficientFundsException(Math.max(0, amount - availableToWithdraw()));
        } catch (InsufficientFundsException e) {
            throw e;
        } finally {
            // The finally block is intentionally present to demonstrate guaranteed cleanup/logging boundary.
        }
    }

    @Override public int compareTo(Account other) { return accountNumber.compareTo(other.accountNumber); }
    @Override public Account clone() { try { return (Account) super.clone(); } catch (CloneNotSupportedException e) { throw new AssertionError(e); } }
    @Override public String toString() { return getClass().getSimpleName() + "{" + accountNumber + ", owner=" + ownerName + ", balance=" + getBalance() + ", active=" + active + "}"; }
    @Override public boolean equals(Object o) { return o instanceof Account a && accountNumber.equals(a.accountNumber); }
    @Override public int hashCode() { return accountNumber.hashCode(); }
    public String statement() { return "ACCOUNT STATEMENT\nNumber: " + accountNumber + "\nOwner: " + ownerName + "\nBalance: ₹" + getBalance() + "\nActive: " + active; }
}
