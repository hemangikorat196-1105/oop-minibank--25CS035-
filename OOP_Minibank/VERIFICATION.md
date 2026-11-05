# Build and Verification Record

## Checks completed in the preparation environment

- Compiled every `src/main/java` source file with `javac --release 17 -Xlint:all`: PASS (no compiler warnings).
- Created `target/oop-minibank-1.0.0.jar` with `Main-Class: MiniBank`: PASS.
- Started the runnable JAR and exercised the menu: PASS.
- Opened an account with mobile/email validation: PASS.
- Deposited and printed a statement: PASS.
- Attempted an over-limit withdrawal and verified the shortfall exception: PASS.
- Transferred funds between two accounts: PASS.
- Ran the 10-task transaction batch on the four-thread executor: PASS.
- Generated the NIO end-of-day report from the append-only transaction log: PASS.
- Restarted the program and loaded saved account state: PASS.
- Verified a newly opened account receives the next account number after restart: PASS.
- Demonstrated opposite-order lock blocking and the ordered-lock transfer fix: PASS.
- Demonstrated the unsafe race result and synchronized result (10,000): PASS.
- Demonstrated producer/consumer coordination with no missing items: PASS.
- Compiled JUnit test source files against temporary API stubs to check Java syntax: PASS.
- Ran a separate manual integration smoke test for service operations, checked exceptions, transfer, repository, parser, persistence and batch processing: PASS.

## Not run here

The actual JUnit 5 test runner (`mvn clean test`) was **not executed** because Maven is not installed in this environment and external dependency downloads are unavailable. The project includes the JUnit 5 dependency and test classes. Run `mvn clean test` on a computer with Maven and internet access to execute them for real. Do not report the JUnit suite as passed until that command succeeds.

## Local verification commands

```bash
mvn clean test
mvn package
java -jar target/oop-minibank-1.0.0.jar
```

On Windows without Maven, use:

```bat
build.bat
java -jar target\oop-minibank-1.0.0.jar
```
