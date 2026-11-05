import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.*;
import model.*;

class UtilityTest {
    @Test void validatorsAndParserWork() {
        assertTrue(Validator.isValidMobile("9876543210"));
        assertFalse(Validator.isValidMobile("123"));
        assertTrue(Validator.isValidEmail("abc@example.com"));
        assertFalse(Validator.isValidEmail("bad-email"));
        assertTrue(Validator.isValidPan("ABCDE1234F"));
        assertTrue(Validator.isValidIfsc("SBIN0001234"));
        Command command = CommandParser.parse("DEPOSIT AC0001 500");
        assertEquals(TransactionType.DEPOSIT, command.type());
        assertEquals("AC0001", command.accountNumber());
        assertEquals(500, command.amount());
        assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("DEPOSIT AC0001"));
    }

    @Test void repositoryUsesIdAnnotationForAccountsAndCustomers() {
        Repository<Account> accounts = new Repository<>();
        Account account = new SavingsAccount("Repo", 500, 0, 4.0);
        accounts.save(account);
        assertSame(account, accounts.findById(account.getAccountNumber()));
        assertEquals(1, accounts.findAll().size());
        assertTrue(accounts.delete(account.getAccountNumber()));
        assertEquals(0, accounts.size());

        Repository<Customer> customers = new Repository<>();
        Customer customer = new Customer("Riya", "riya@example.com", "9876543210");
        customers.saveAll(customer);
        assertSame(customer, customers.findById(customer.getCustomerId()));
    }

    @Test void boundedAverageWorksForNumericArrays() {
        assertEquals(2.0, Repository.average(new Integer[]{1, 2, 3}));
        assertEquals(2.5, Repository.average(new Double[]{1.5, 2.5, 3.5}), 0.0001);
        assertEquals(3.0, Repository.average(new Long[]{2L, 4L}));
    }

    @Test void statementFormatterIncludesAccountDetails() {
        Account account = new SavingsAccount("Statement User", 700, 0, 4.0);
        String statement = StatementFormatter.buildStatement(account);
        assertTrue(statement.contains(account.getAccountNumber()));
        assertTrue(statement.contains("Statement User"));
        assertTrue(statement.contains("700"));
    }
}
