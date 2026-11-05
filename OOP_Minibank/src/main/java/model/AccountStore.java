package model;
import java.util.*;

public class AccountStore {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();
    public synchronized void add(Account account) { Objects.requireNonNull(account); accounts.put(account.getAccountNumber(), account); }
    public synchronized Account get(String accountNumber) { return accounts.get(accountNumber); }
    public synchronized Collection<Account> all() { return new ArrayList<>(accounts.values()); }
    public synchronized ArrayList<Account> getAll() { return new ArrayList<>(accounts.values()); }
    public synchronized void addHistory(String line) { transactionHistory.add(line); }
    public synchronized List<String> history() { return new ArrayList<>(transactionHistory); }
    public synchronized ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparingLong(Account::getBalance).reversed()); return list;
    }
    public synchronized ArrayList<Account> above(long amount) {
        ArrayList<Account> result = new ArrayList<>();
        for (Account account : accounts.values()) if (account.getBalance() > amount) result.add(account);
        return result;
    }
}
