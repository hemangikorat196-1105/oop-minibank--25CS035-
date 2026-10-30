import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.WithdrawRule;
import model.AccountWorker;
import model.TransactionProcessor;
import model.TransactionTask;
import model.ProducerConsumer;

import util.AnnotationValidator;
import util.StatePersister;
import util.TransactionLog;
import util.ReportGenerator;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.AccountNotFoundException;
import exception.InvalidAmountException;
import exception.BankResource;

public class MiniBank {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Customer customer = new Customer(101, "Rahul", "rahul@gmail.com");

        Account[] accounts = {
            new SavingsAccount(1001, 5000, 1000),
            new CurrentAccount(1002, 2000, 1000),
            new FixedDepositAccount(1003, 10000)
        };

        Path accountsFile = Paths.get("accounts.dat");
        Path logsFolder = Paths.get("logs");
        Path transactionFile = logsFolder.resolve("transactions.txt");
        Path reportFile = Paths.get("end-of-day-report.txt");

        try {
            if (java.nio.file.Files.exists(accountsFile)) {
                accounts = StatePersister.load(accountsFile);
                System.out.println("Accounts loaded successfully.");
            } else {
                StatePersister.save(accounts, accountsFile);
                System.out.println("Accounts saved successfully.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Persistence error: " + e.getMessage());
        }

        Account invalidAccount = new SavingsAccount(1004, -100, 0);

        for (String error : AnnotationValidator.validate(invalidAccount)) {
            System.out.println(error);
        }

        WithdrawRule rule1 = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.canWithdraw(amount);
            }
        };

        WithdrawRule rule2 = (account, amount) -> account.canWithdraw(amount);

        System.out.println("Anonymous class result: "
                + rule1.allow(accounts[0], 500));

        System.out.println("Lambda result: "
                + rule2.allow(accounts[0], 500));

        int choice;

        do {
            System.out.println("\n--- MiniBank Menu ---");
            System.out.println("1. Customer Details");
            System.out.println("2. Display All Accounts");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Thread Test");
            System.out.println("7. Exit");
            System.out.println("8. Producer-Consumer Test");
            System.out.println("9. Save Accounts");
            System.out.println("10. Load Accounts");
            System.out.println("11. Generate Report");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            try {
                switch (choice) {
                    case 1:
                        customer.displayCustomer();
                        break;

                    case 2:
                        for (Account account : accounts) {
                            System.out.println(account);
                            System.out.println("Interest Rate: "
                                    + account.interestRate() + "%");
                            System.out.println("Yearly Interest: "
                                    + account.yearlyInterest());

                            if (account instanceof SavingsAccount) {
                                System.out.println("Type: Savings Account");
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter deposit amount: ");
                        long depositAmount = sc.nextLong();
                        accounts[0].deposit(depositAmount);

                        TransactionLog.append(transactionFile,
                                "DEPOSIT " + depositAmount);

                        System.out.println("Deposit successful.");
                        break;

                    case 4:
                        System.out.print("Enter withdrawal amount: ");
                        long withdrawalAmount = sc.nextLong();
                        accounts[0].withdraw(withdrawalAmount);

                        TransactionLog.append(transactionFile,
                                "WITHDRAWAL " + withdrawalAmount);

                        System.out.println("Withdrawal successful.");
                        break;

                    case 5:
                        System.out.print("Enter transfer amount: ");
                        long transferAmount = sc.nextLong();

                        accounts[0].transfer(accounts[1], transferAmount);

                        TransactionLog.append(transactionFile,
                                "WITHDRAWAL " + transferAmount);
                        TransactionLog.append(transactionFile,
                                "DEPOSIT " + transferAmount);

                        System.out.println("Transfer successful.");
                        break;

                    case 6:
                        Account testAccount = new SavingsAccount(1005, 0, 0);
                        Thread[] threads = new Thread[10];

                        for (int i = 0; i < 10; i++) {
                            threads[i] = new Thread(
                                    new AccountWorker(testAccount, 1000, 1),
                                    "Worker-" + (i + 1));
                            threads[i].start();
                        }

                        for (Thread thread : threads) {
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.out.println("Thread interrupted.");
                                break;
                            }
                        }

                        System.out.println("Final balance: "
                                + testAccount.getBalance());
                        break;

                    case 7:
                        System.out.println("Thank you!");
                        break;

                    case 8:
                        ProducerConsumer pc = new ProducerConsumer();

                        Thread producer = new Thread(() -> {
                            for (int i = 1; i <= 10; i++) {
                                pc.produce(i);
                            }
                        }, "Producer");

                        Thread consumer = new Thread(() -> {
                            for (int i = 1; i <= 10; i++) {
                                pc.consume();
                            }
                        }, "Consumer");

                        producer.start();
                        consumer.start();

                        try {
                            producer.join();
                            consumer.join();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Thread interrupted.");
                        }
                        break;

                    case 9:
                        StatePersister.save(accounts, accountsFile);
                        System.out.println("Accounts saved successfully.");
                        break;

                    case 10:
                        accounts = StatePersister.load(accountsFile);
                        System.out.println("Accounts loaded successfully.");
                        for (Account account : accounts) {
                            System.out.println(account);
                        }
                        break;

                    case 11:
                        ReportGenerator.generate(logsFolder, reportFile);
                        break;

                    default:
                        if (choice != 7) {
                            System.out.println("Invalid choice.");
                        }
                }

            } catch (InsufficientFundsException e) {
                System.out.println("Withdrawal failed: short by "
                        + e.getShortfall());

            } catch (InvalidAmountException e) {
                System.out.println("Invalid amount: " + e.getMessage());

            } catch (AccountNotFoundException e) {
                System.out.println("Account not found: " + e.getMessage());

            } catch (BankException e) {
                System.out.println("Bank error: " + e.getMessage());

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("File operation failed: " + e.getMessage());

            } finally {
                if (choice >= 3 && choice <= 5) {
                    System.out.println("Transaction operation finished.");
                }
            }

        } while (choice != 7);

        try {
            StatePersister.save(accounts, accountsFile);
            System.out.println("Final account state saved.");

            ReportGenerator.generate(logsFolder, reportFile);

        } catch (IOException e) {
            System.out.println("Could not save final data: " + e.getMessage());
        }

        TransactionProcessor processor = new TransactionProcessor();

        for (int i = 1; i <= 10; i++) {
            processor.submit(new TransactionTask(i));
        }

        processor.stop();
        System.out.println("All tasks completed.");

        try (BankResource resource = new BankResource()) {
            System.out.println("Resource opened.");
            throw new RuntimeException("Demo exception");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        sc.close();
    }
}