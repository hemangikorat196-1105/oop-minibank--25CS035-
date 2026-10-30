
package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class AccountStore {

    private final ConcurrentHashMap<String, Account> accounts =
            new ConcurrentHashMap<>();

    private final ArrayList<String> transactionHistory =
            new ArrayList<>();

    public void add(Account account) {
        accounts.put("AC" + String.format("%04d",
                account.getAccountNumber()), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sorted = getAll();
        sorted.sort(Comparator.comparingDouble(
                Account::getBalance).reversed());
        return sorted;
    }

    public synchronized void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public synchronized ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public ConcurrentHashMap<String, Account> getConcurrentAccounts() {
        return accounts;
    }
}
