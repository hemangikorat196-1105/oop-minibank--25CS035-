package util;
import java.io.IOException; import java.nio.charset.StandardCharsets; import java.nio.file.*;
public final class TransactionLog {
    private TransactionLog(){}
    public static void append(Path file,String line)throws IOException{
        Files.createDirectories(file.toAbsolutePath().getParent());
        Files.write(file,(line+System.lineSeparator()).getBytes(StandardCharsets.UTF_8),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
    }
}
