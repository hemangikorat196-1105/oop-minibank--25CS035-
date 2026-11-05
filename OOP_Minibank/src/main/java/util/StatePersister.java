package util;
import java.io.*;
import java.nio.file.*;
import model.Account;

public final class StatePersister {
    private StatePersister() { }
    public static void save(Account[] accounts, Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        Path temp = file.resolveSibling(file.getFileName() + ".tmp");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(temp,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) { out.writeObject(accounts); }
        try { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
        catch (AtomicMoveNotSupportedException e) { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING); }
    }
    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) return new Account[0];
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            Account[] accounts = (Account[]) in.readObject();
            Account.synchronizeCounter(accounts);
            return accounts;
        }
    }
}
