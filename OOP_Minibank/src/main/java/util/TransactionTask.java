package util;
import model.Account;
import model.AccountStore;
import model.TransactionType;
import exception.BankException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

/** A Runnable that performs a real deposit or withdrawal on the shared live account. */
public class TransactionTask implements Runnable {
    private final ConcurrentHashMap<String, Account> sharedAccounts;
    private final AccountStore store;
    private final String id;
    private final TransactionType type;
    private final long amount;
    public TransactionTask(ConcurrentHashMap<String, Account> accounts, String id, TransactionType type, long amount) {
        this(accounts, null, id, type, amount);
    }
    public TransactionTask(AccountStore store, String id, TransactionType type, long amount) {
        this(toMap(store), store, id, type, amount);
    }
    private TransactionTask(ConcurrentHashMap<String, Account> accounts, AccountStore store, String id, TransactionType type, long amount) {
        this.sharedAccounts = accounts; this.store = store; this.id = id; this.type = type; this.amount = amount;
    }
    private static ConcurrentHashMap<String, Account> toMap(AccountStore store) {
        ConcurrentHashMap<String, Account> map = new ConcurrentHashMap<>();
        for (Account account : store.all()) map.put(account.getAccountNumber(), account);
        return map;
    }
    @Override public void run() {
        Account account = sharedAccounts.get(id);
        if (account == null || amount <= 0) return;
        String line;
        try {
            if (type == TransactionType.DEPOSIT) { account.deposit(amount); line = "DEPOSIT " + id + " " + amount; }
            else if (type == TransactionType.WITHDRAW) {
                account.withdraw(amount);
                line = "WITHDRAW " + id + " " + amount;
            } else return; // A transfer needs both account ids and is handled by BankService.
            if (store != null) store.addHistory(line);
            TransactionLog.append(Path.of("logs", "transactions.log"), line);
        } catch (BankException | RuntimeException | IOException e) { System.err.println("Transaction task failed: " + e.getMessage()); }
    }
}
