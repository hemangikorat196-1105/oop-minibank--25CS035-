# MiniBank — OOP CEUE203 (Practicals 1–14)

A complete console-based Java banking project for the **Part B — MiniBank** requirements in the CEUE203 practical list. It uses JDK 17 features (records, switch expressions, pattern matching for `instanceof`), a layered structure, collections, reflection, concurrency, persistence, JUnit 5 tests and a runnable JAR.

> Educational demonstration only. This is not a real banking application. Do not use real customer or financial information.

## 1. Requirements

- JDK 17 or newer (`java -version`, `javac -version`)
- Maven 3.8+ to run JUnit tests and the Maven build (`mvn -version`)
- Windows: VS Code terminal / PowerShell / Command Prompt
- macOS/Linux: Terminal

## 2. Build and run on Windows (no Maven required for the app)

1. Extract the ZIP.
2. Open the `MiniBank_Project` folder in VS Code.
3. Open Terminal → New Terminal.
4. Run:

```bat
build.bat
java -jar target\oop-minibank-1.0.0.jar
```

The included `build.bat` compiles all Java source files using `javac --release 17` and creates the runnable JAR. You can also double-click `run.bat` after building.

## 3. Run tests and Maven package

If Maven is installed and internet is available for the first dependency download:

```bash
mvn clean test
mvn package
java -jar target/oop-minibank-1.0.0.jar
```

JUnit 5 tests are in `src/test/java`. Maven writes test reports to `target/surefire-reports/`.

## 4. Menu and demo flow

| Choice | Operation |
|---:|---|
| 1 | Open account (validates owner, mobile and email) |
| 2 | Deposit |
| 3 | Withdraw (prints the exception shortfall when insufficient) |
| 4 | Transfer between accounts |
| 5 | Print account statement |
| 6 | List accounts sorted by balance |
| 7 | Run a 10-task concurrent deposit batch using a four-thread pool |
| 8 | Generate end-of-day report from transaction logs |
| 9 | Search account by owner name |
| 10 | Demonstrate opposite lock ordering and the ordered-lock fix |
| 11 | Demonstrate unsafe race condition and synchronized deposits |
| 12 | Demonstrate producer-consumer using `wait()` / `notifyAll()` |
| 0 | Save data and exit |

On first run, three demonstration accounts are created. The application stores accounts in `data/accounts.dat`, appends transaction lines to `logs/transactions.log`, and writes the report to `reports/end-of-day-report.txt`. These files are created when the application runs.

## 5. Project structure

```text
MiniBank_Project/
├── src/main/java/
│   ├── MiniBank.java
│   ├── model/                 # Account hierarchy, Customer, records, interfaces, store
│   │   └── annotation/        # @Id, @Positive, @MaxLength
│   ├── service/               # BankService
│   ├── util/                  # validation, parser, repository, threads, persistence, report
│   └── exception/             # checked banking exceptions and AutoCloseable example
├── src/test/java/             # JUnit 5 tests
├── target/oop-minibank-1.0.0.jar (included runnable build)
├── pom.xml
├── MiniBank.mf
├── build.bat
├── run.bat
└── README.md
```

## 6. Practical 1–14 Part B coverage map

| Practical | Implementation in this project |
|---|---|
| 1 | `MiniBank` main, `BankInfo` record, `MenuOption` enum, Scanner menu loop, switch expression, EXIT |
| 2 | Encapsulated `Customer` and `Account`, private/final fields, constructor chaining, generated IDs, deposit/withdraw, account array in startup examples |
| 3 | `toString`, `equals`, `hashCode`, `Comparable`, cloneable `Customer`, static nested `Customer.Address`, `instanceof` |
| 4 | Compiled regex `Validator`, `TransactionType`, `Command` record, `CommandParser`, `StatementFormatter` |
| 5 | Abstract `Account`, `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`, polymorphic interest and withdrawal rules |
| 6 | `Transactable`, `InterestBearing` default methods, `WithdrawRule` anonymous class and lambda, `Premium`, packages, static import, runnable JAR |
| 7 | Runtime `@Id`, `@Positive`, `@MaxLength`; `AnnotationValidator` uses reflection and returns error messages |
| 8 | Checked `BankException` hierarchy, `InsufficientFundsException.shortfall`, validation exceptions, transfer try/catch/finally, `AutoCloseable` try-with-resources |
| 9 | `AccountWorker`, unsafe race demonstration, synchronized deposit/withdraw, thread names and joins |
| 10 | Four-thread `ExecutorService`, runnable deposit/withdraw tasks, circular array producer-consumer with wait/notify, opposite-lock deadlock and ordered-lock solution |
| 11 | Serializable model classes, `StatePersister`, append-only `TransactionLog`, NIO `ReportGenerator`, startup load and exit save |
| 12 | `HashMap` account store, `ArrayList` history, `Comparator` sorting, `Comparable` account number order, `ConcurrentHashMap` in transaction processor |
| 13 | Generic `Repository<T>` CRUD using reflected `@Id`, `@SafeVarargs saveAll`, bounded `<N extends Number> average`, Account and Customer repositories |
| 14 | `BankService`, fully connected menu, persistence, batch processor, report, JUnit tests, runnable JAR and this README |

## 7. Important implementation notes

- Account balances are private; there is no public balance setter.
- Account IDs are advanced after loading serialized accounts so newly opened accounts do not reuse an existing ID.
- `Account.withdraw()` throws checked banking exceptions for invalid amounts and insufficient funds, as required by the later exception practical. `canWithdraw()` still supplies each account type's polymorphic rule.
- The unsafe race-condition method is intentionally isolated and used only in Practical 9's demonstration. Normal deposits and withdrawals are synchronized.
- The deadlock demonstration uses disposable demonstration accounts. Actual transfers acquire locks in ascending account-number order.
- `TransactionProcessor` uses four workers and a `ConcurrentHashMap` containing the same live account objects used by the application store.
- The persistence file uses Java serialization for coursework. Never deserialize files from untrusted sources.

## 8. Suggested faculty demonstration

1. Start the application and show the header, account types and menu.
2. List accounts; deposit into one account; print its statement.
3. Try withdrawing more than the available limit and show the shortfall.
4. Transfer money between two accounts and list balances again.
5. Run the concurrent batch and show the final balance.
6. Run the race-condition demo, producer-consumer demo and deadlock demo.
7. Generate the end-of-day report.
8. Exit and restart; show that the accounts load with their previous balances.
9. Run `mvn clean test` and `java -jar target/oop-minibank-1.0.0.jar` on a machine with Maven.

## 9. Submission reminder

Before submission, run the application on your own Windows/VS Code setup, take screenshots of the operations above, run the JUnit tests with Maven, and commit the final source and README to your own GitHub repository. Add your faculty as a collaborator if your practical instructions require it.
