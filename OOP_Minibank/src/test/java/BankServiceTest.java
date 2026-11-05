import model.*;
import service.BankService;
import util.*;
import exception.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;

class BankServiceTest {
    private BankService service() { return new BankService(new Repository<>(), new AccountStore()); }

    @Test void depositIncreasesBalance() throws Exception {
        BankService service = service();
        Account account = service.openAccount("Test User", 1000);
        service.deposit(account.getAccountNumber(), 500);
        assertEquals(1500, account.getBalance());
    }

    @Test void withdrawalOverLimitThrowsWithShortfall() throws Exception {
        BankService service = service();
        Account account = service.openAccount("Test User", 1000);
        InsufficientFundsException error = assertThrows(InsufficientFundsException.class,
                () -> service.withdraw(account.getAccountNumber(), 2000));
        assertEquals(1000, error.getShortfall());
        assertEquals(1000, account.getBalance());
    }

    @Test void concurrentDepositsKeepCorrectBalance() throws Exception {
        Account account = new SavingsAccount("Concurrent", 0, 0, 4.0);
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) { threads[i] = new Thread(new AccountWorker(account, 1000, 1)); threads[i].start(); }
        for (Thread thread : threads) thread.join();
        assertEquals(10000, account.getBalance());
    }

    @Test void transferMovesMoneyAtomically() throws Exception {
        BankService service = service();
        Account source = service.openAccount("Source", 1000);
        Account target = service.openAccount("Target", 200);
        service.transfer(source.getAccountNumber(), target.getAccountNumber(), 300);
        assertEquals(700, source.getBalance());
        assertEquals(500, target.getBalance());
    }

    @Test void saveAndLoadPreservesAccountDataAndNextId() throws Exception {
        Account account = new SavingsAccount("Persist User", 2500, 0, 4.0);
        Path file = Files.createTempFile("minibank", ".dat");
        try {
            StatePersister.save(new Account[]{account}, file);
            Account[] loaded = StatePersister.load(file);
            assertEquals(account.getAccountNumber(), loaded[0].getAccountNumber());
            assertEquals(2500, loaded[0].getBalance());
            Account next = new SavingsAccount("Next", 0, 0, 4.0);
            assertTrue(Integer.parseInt(next.getAccountNumber().substring(2)) > Integer.parseInt(account.getAccountNumber().substring(2)));
        } finally { Files.deleteIfExists(file); }
    }
}
