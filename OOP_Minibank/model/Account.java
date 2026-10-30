
package model;

import java.io.Serializable;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import exception.BankException;
import model.annotation.Id;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {

    private static final long serialVersionUID = 1L;

    @Id
    protected int accountNumber;

    @Positive
    protected double balance;

    public Account(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public int compareTo(Account other) {
        return Integer.compare(this.accountNumber, other.accountNumber);
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }

        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }

        if (balance < amount) {
            throw new InsufficientFundsException(
                    "Insufficient funds.", (long) (amount - balance));
        }

        balance -= amount;
        return true;
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    @Override
    public String toString() {
        return "Account{accountNumber=" + accountNumber
                + ", balance=" + balance + "}";
    }

    public void transfer(Account to, long amount) throws BankException {
        try {
            withdraw(amount);
            to.deposit(amount);
        } catch (InsufficientFundsException | InvalidAmountException e) {
            throw e;
        } finally {
            System.out.println("Transfer operation completed.");
        }
    }
}
