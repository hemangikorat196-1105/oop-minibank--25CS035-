package model;
import exception.BankException;
public class AccountWorker implements Runnable {
    private final Account account; private final int times; private final long amount; private final boolean unsafe;
    public AccountWorker(Account account, int times, long amount) { this(account, times, amount, false); }
    public AccountWorker(Account account, int times, long amount, boolean unsafe) { this.account=account; this.times=times; this.amount=amount; this.unsafe=unsafe; }
    @Override public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        for (int i=0; i<times; i++) {
            try { if (unsafe) account.depositUnsafe(amount); else account.deposit(amount); }
            catch (BankException | RuntimeException e) { System.err.println("Worker transaction failed: " + e.getMessage()); break; }
        }
        System.out.println(Thread.currentThread().getName() + " finished");
    }
}
