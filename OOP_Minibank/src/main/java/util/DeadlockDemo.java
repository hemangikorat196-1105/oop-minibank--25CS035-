package util;
import model.Account;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public final class DeadlockDemo {
    private DeadlockDemo() { }
    /** Demonstrates opposite lock order in daemon threads so the console app can continue. */
    public static void demonstrateUnsafe(Account a, Account b) {
        CountDownLatch ready = new CountDownLatch(2);
        Thread one = new Thread(() -> lockOpposite(a, b, ready, "A-to-B"), "A-to-B");
        Thread two = new Thread(() -> lockOpposite(b, a, ready, "B-to-A"), "B-to-A");
        one.setDaemon(true); two.setDaemon(true); one.start(); two.start();
        try { ready.await(); Thread.sleep(250); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println("Unsafe lock-order demo: thread states = " + one.getState() + ", " + two.getState() + " (BLOCKED indicates deadlock risk).");
    }
    private static void lockOpposite(Account first, Account second, CountDownLatch ready, String label) {
        synchronized (first) {
            ready.countDown();
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            synchronized (second) { System.out.println(label + " completed (no deadlock this run)."); }
        }
    }
    public static void demonstrateSafeTransfers(List<Account> accounts) {
        if (accounts.size() < 2) { System.out.println("Need two accounts for the demo."); return; }
        Account a = accounts.get(0), b = accounts.get(1);
        Thread one = new Thread(() -> { for (int i = 0; i < 100; i++) a.transferTo(b, 1); }, "safe-A-to-B");
        Thread two = new Thread(() -> { for (int i = 0; i < 100; i++) b.transferTo(a, 1); }, "safe-B-to-A");
        one.start(); two.start();
        try { one.join(); two.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println("Safe transfers completed using ascending account-number lock order.");
    }
}
