package util;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import model.Account;
import model.AccountStore;
import model.TransactionType;

/** Four-worker transaction pool backed by a ConcurrentHashMap of the live Account objects. */
public class TransactionProcessor implements AutoCloseable {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> sharedAccounts = new ConcurrentHashMap<>();
    private final AtomicInteger submitted = new AtomicInteger();
    public TransactionProcessor(AccountStore store) { for (Account account : store.all()) sharedAccounts.put(account.getAccountNumber(), account); }
    public void submit(Runnable task) { submitted.incrementAndGet(); executor.execute(task); }
    public void submitTransaction(String accountId, TransactionType type, long amount) {
        submit(new TransactionTask(sharedAccounts, accountId, type, amount));
    }
    public Account getSharedAccount(String id) { return sharedAccounts.get(id); }
    public int submittedCount() { return submitted.get(); }
    public void stop() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) throw new IllegalStateException("Transaction workers did not stop.");
        }
    }
    @Override public void close() { try { stop(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } }
}
