import model.*;
import model.annotation.Positive;
import service.BankService;

import util.*;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.BankResource;

import java.nio.file.Path;
import java.util.*;

import static util.Validator.isValidMobile;
import static util.Validator.isValidEmail;

/** Console entry point for the integrated MiniBank practical project. */
public class MiniBank {
    private static final Path DATA_FILE = Path.of("data", "accounts.dat");
    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        BankInfo info = new BankInfo("MiniBank", "Surat Main Branch");
        System.out.println("\n================================");
        System.out.println(info.name() + " | " + info.branch());
        System.out.println("================================");
        demonstrateInterfaces();

        AccountStore store = new AccountStore();
        Repository<Account> repository = new Repository<>();
        BankService service = new BankService(repository, store);
        showPolymorphism(store);
        boolean running = true;
        while (running) {
            showMenu();
            String raw = SC.nextLine().trim();
            MenuOption option = switch (raw) {
                case "1" -> MenuOption.OPEN_ACCOUNT;
                case "2" -> MenuOption.DEPOSIT;
                case "3" -> MenuOption.WITHDRAW;
                case "4" -> MenuOption.TRANSFER;
                case "5" -> MenuOption.STATEMENT;
                case "6" -> MenuOption.LIST_ACCOUNTS;
                case "7" -> MenuOption.RUN_BATCH;
                case "8" -> MenuOption.REPORT;
                case "9" -> MenuOption.SEARCH_OWNER;
                case "10" -> MenuOption.DEADLOCK_DEMO;
                case "11" -> MenuOption.CONCURRENCY_DEMO;
                case "12" -> MenuOption.PRODUCER_CONSUMER_DEMO;
                case "0" -> MenuOption.EXIT;
                default -> null;
            };
            if (option == null) { System.out.println("Invalid choice. Enter a number from 0 to 12."); continue; }
            try {
                switch (option) {
                    case OPEN_ACCOUNT -> open(service);
                    case DEPOSIT -> deposit(service);
                    case WITHDRAW -> withdraw(service);
                    case TRANSFER -> transfer(service);
                    case STATEMENT -> statement(service);
                    case LIST_ACCOUNTS -> service.listAccounts().forEach(System.out::println);
                    case RUN_BATCH -> runBatch(store);
                    case REPORT -> System.out.println(ReportGenerator.generate(Path.of("logs"), Path.of("reports", "end-of-day-report.txt")));
                    case SEARCH_OWNER -> {
                        System.out.print("Owner name: ");
                        List<Account> found = service.searchByOwner(SC.nextLine());
                        if (found.isEmpty()) System.out.println("No matching account found."); else found.forEach(System.out::println);
                    }
                    case DEADLOCK_DEMO -> {
                        Account dummyA = new SavingsAccount("Deadlock A", 100, 0, 4.0);
                        Account dummyB = new SavingsAccount("Deadlock B", 100, 0, 4.0);
                        DeadlockDemo.demonstrateUnsafe(dummyA, dummyB);
                        DeadlockDemo.demonstrateSafeTransfers(service.listAccounts().stream().limit(2).toList());
                    }
                    case CONCURRENCY_DEMO -> RaceConditionDemo.run();
                    case PRODUCER_CONSUMER_DEMO -> TransactionBuffer.demo();
                    case EXIT -> running = false;
                }
            } catch (InsufficientFundsException e) {
                System.out.println("Withdrawal failed: " + e.getMessage());
                System.out.println("Shortfall amount: ₹" + e.getShortfall());
            } catch (BankException | IllegalArgumentException e) {
                System.out.println("Operation failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        try {
            Account[] current = service.listAccounts().toArray(Account[]::new);
            StatePersister.save(current, DATA_FILE);
            Account[] reloaded = StatePersister.load(DATA_FILE);
            System.out.println("Saved and verified " + reloaded.length + " account(s).");
        } catch (Exception e) { System.out.println("Could not save state: " + e.getMessage()); }
        finally { System.out.println("Shutdown persistence phase completed."); }
        System.out.println("Thank you for using MiniBank. Goodbye!");
    }

    private static void showMenu() {
        System.out.println("\n--- MINIBANK MENU ---");
        System.out.println("1 Open account");
        System.out.println("2 Deposit");
        System.out.println("3 Withdrawal");
        System.out.println("4 Transfer");
        System.out.println("5 Statement");
        System.out.println("6 List accounts");
        System.out.println("7 Run transaction batch");
        System.out.println("8 End-of-day report");
        System.out.println("9 Search owner");
        System.out.println("10 Deadlock demo");
        System.out.println("11 Race-condition demo");
        System.out.println("12 Producer-consumer demo");
        System.out.println("0 Exit");
        System.out.print("Choose: ");
    }

    private static void seed(AccountStore store, Repository<Account> repo) {
        Account[] accounts = {
            new SavingsAccount("Riya Shah", 5000, 500, 4.0),
            new CurrentAccount("Aarav Patel", 2000, 1000, 0.0),
            new FixedDepositAccount("Neha Desai", 9000, 7.0)
        };
        for (Account account : accounts) { store.add(account); repo.save(account); }
    }

    private static void open(BankService service) throws BankException {
        System.out.print("Owner name: "); String name = SC.nextLine().trim();
        System.out.print("Mobile (10 digits): "); String mobile = SC.nextLine().trim();
        if (!isValidMobile(mobile)) throw new IllegalArgumentException("Invalid mobile number. Expected 10 digits starting 6–9.");
        System.out.print("Email: "); String email = SC.nextLine().trim();
        if (!isValidEmail(email)) throw new IllegalArgumentException("Invalid email address.");
        Customer customer = new Customer(name, email, mobile);
        System.out.println("Customer profile created: " + customer.getCustomerId());
        Account account = service.openAccount(name, mobile, email, 0);
        System.out.println("Account created: " + account.getAccountNumber());
    }
    private static void deposit(BankService service) throws BankException {
        System.out.print("Account number: "); String id = SC.nextLine().trim();
        System.out.print("Amount: "); long amount = Long.parseLong(SC.nextLine().trim());
        service.deposit(id, amount); System.out.println("Deposit successful.");
    }
    private static void withdraw(BankService service) throws BankException {
        System.out.print("Account number: "); String id = SC.nextLine().trim();
        System.out.print("Amount: "); long amount = Long.parseLong(SC.nextLine().trim());
        service.withdraw(id, amount); System.out.println("Withdrawal successful.");
    }
    private static void transfer(BankService service) throws BankException {
        System.out.print("From account: "); String from = SC.nextLine().trim();
        System.out.print("To account: "); String to = SC.nextLine().trim();
        System.out.print("Amount: "); long amount = Long.parseLong(SC.nextLine().trim());
        service.transfer(from, to, amount); System.out.println("Transfer successful.");
    }
    private static void statement(BankService service) throws BankException {
        System.out.print("Account number: "); System.out.println(service.statement(SC.nextLine().trim()));
    }
    private static void runBatch(AccountStore store) throws InterruptedException {
        List<Account> accounts = store.sortedByBalance();
        if (accounts.isEmpty()) { System.out.println("No accounts available."); return; }
        String id = accounts.get(0).getAccountNumber();
        try (TransactionProcessor processor = new TransactionProcessor(store)) {
            for (int i = 0; i < 10; i++) processor.submitTransaction(id, TransactionType.DEPOSIT, 10);
            processor.stop();
            System.out.println("Completed " + processor.submittedCount() + " concurrent deposits.");
        }
        System.out.println(id + " new balance: ₹" + store.get(id).getBalance());
    }
    private static void demonstrateInterfaces() {
        WithdrawRule anonymous = new WithdrawRule() {
            @Override public boolean allow(Account account, long amount) { return account.getBalance() >= amount; }
        };
        WithdrawRule lambda = (account, amount) -> account.getBalance() >= amount;
        System.out.println("WithdrawRule anonymous class and lambda ready: " + (anonymous != null && lambda != null));
        System.out.println("Validator sample: mobile=" + isValidMobile("9876543210") + ", email=" + isValidEmail("student@example.com"));
        System.out.println("Generic average sample: " + Repository.average(new Integer[]{10, 20, 30}));
        Command sample = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("Parsed command: " + sample.type() + " | " + sample.accountNumber() + " | " + sample.amount());
        System.out.println("Invalid mobile sample: " + isValidMobile("12345") + ", invalid email sample: " + isValidEmail("not-an-email"));
        Repository<Customer> customerRepository = new Repository<>();
        Customer customer = new Customer("Demo Student", "demo@example.com", "9876543210");
        customer.setAddress(new Customer.Address("Main Road", "Surat", "395001"));
        customerRepository.save(customer);
        System.out.println("Customer repository lookup: " + customerRepository.findById(customer.getCustomerId()));
        System.out.println("Customer clone sample: " + customer.clone());
        try (BankResource resource = new BankResource()) { resource.use(); } finally { System.out.println("Finally block executed after resource use."); }
        System.out.println("Annotation validator sample errors: " + Arrays.toString(AnnotationValidator.validate(new ValidationSample(-5, "name-that-is-too-long"))));

    }
    private static void showPolymorphism(AccountStore store) {
        System.out.println("\nAccount types (runtime polymorphism):");
        Account[] accountsArray = store.sortedByBalance().toArray(Account[]::new);
        for (Account account : accountsArray) {
            System.out.printf("%s | annual rate %.1f%% | monthly interest ₹%.2f%n", account.getAccountNumber(), account.interestRate(), account.monthlyInterest());
            if (account instanceof SavingsAccount savings) System.out.println("  Savings minimum balance: ₹" + savings.getMinBalance());
        }
        List<Account> accounts = store.sortedByBalance();
        if (!accounts.isEmpty()) {
            Account sample = accounts.get(0);
            try {
                java.lang.reflect.Field balance = Account.class.getDeclaredField("balance");
                balance.setAccessible(true);
                long original = sample.getBalance();
                balance.setLong(sample, -100);
                System.out.println("Account annotation validation sample: " + Arrays.toString(AnnotationValidator.validate(sample)));
                balance.setLong(sample, original);
            } catch (ReflectiveOperationException e) { System.out.println("Account annotation demo: " + e.getMessage()); }
        }
        if (accounts.size() >= 2) System.out.println("Account equality check: " + accounts.get(0).equals(accounts.get(1)));
    }
    private static class ValidationSample {
        @Positive private final long balance;
        @model.annotation.MaxLength(10) private final String name;
        ValidationSample(long balance, String name) { this.balance = balance; this.name = name; }
    }
}
