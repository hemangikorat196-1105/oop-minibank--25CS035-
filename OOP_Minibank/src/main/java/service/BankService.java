package service;

import exception.*;
import model.*;
import util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Service layer: validates requests, coordinates repository/store, and records transactions. */
public class BankService {
    private final Repository<Account> repository;
    private final AccountStore store;
    public BankService(Repository<Account> repository, AccountStore store) {
        this.repository = repository; this.store = store;
    }
    public Account openAccount(String owner, long opening) throws BankException {
        if (owner == null || owner.isBlank() || owner.trim().length() > 60) throw new BankException("Owner name must contain 1–60 characters.");
        if (opening < 0) throw new InvalidAmountException();
        Account account = new SavingsAccount(owner.trim(), opening, 0, 4.0);
        repository.save(account); store.add(account);
        return account;
    }
    public Account openAccount(String owner, String mobile, String email, long opening) throws BankException {
        if (!Validator.isValidMobile(mobile)) throw new BankException("Invalid mobile number.");
        if (!Validator.isValidEmail(email)) throw new BankException("Invalid email address.");
        return openAccount(owner, opening);
    }
    private Account require(String id) throws AccountNotFoundException {
        Account account = repository.findById(id);
        if (account == null) throw new AccountNotFoundException(id);
        return account;
    }
    public void deposit(String id, long amount) throws BankException {
        if (amount <= 0) throw new InvalidAmountException();
        Account account = require(id);
        account.deposit(amount);
        log("DEPOSIT " + id + " " + amount);
    }
    public void withdraw(String id, long amount) throws BankException {
        if (amount <= 0) throw new InvalidAmountException();
        Account account = require(id);
        account.withdraw(amount);
        log("WITHDRAW " + id + " " + amount);
    }
    public void transfer(String from, String to, long amount) throws BankException {
        if (amount <= 0) throw new InvalidAmountException();
        Account source = require(from), target = require(to);
        source.transfer(target, amount);
        log("WITHDRAW " + from + " " + amount);
        log("DEPOSIT " + to + " " + amount);
    }
    public String statement(String id) throws BankException { return StatementFormatter.buildStatement(require(id)); }
    public List<Account> listAccounts() { return store.sortedByBalance(); }
    public List<Account> searchByOwner(String owner) {
        ArrayList<Account> matches = new ArrayList<>();
        for (Account account : store.all()) if (account.getOwnerName().equalsIgnoreCase(owner.trim())) matches.add(account);
        return matches;
    }
    public void execute(Command command) throws BankException {
        switch (command.type()) {
            case DEPOSIT -> deposit(command.accountNumber(), command.amount());
            case WITHDRAW -> withdraw(command.accountNumber(), command.amount());
            case TRANSFER -> throw new BankException("TRANSFER command requires source and destination account numbers; use the transfer menu.");
        }
    }
    private void log(String line) throws BankException {
        store.addHistory(line);
        try { TransactionLog.append(Path.of("logs", "transactions.log"), line); }
        catch (IOException e) { throw new BankException("Transaction completed but log could not be written: " + e.getMessage()); }
    }
}
