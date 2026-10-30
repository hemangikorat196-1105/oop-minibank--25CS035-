import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class LogAnalyzer {
    public static void main(String[] args) {

        try {
            // Create sample log files
            Files.write(Paths.get("log1.txt"), Arrays.asList(
                "INFO User logged in",
                "ERROR Database connection failed",
                "INFO Data loaded",
                "ERROR File not found"
            ));

            Files.write(Paths.get("log2.txt"), Arrays.asList(
                "INFO Server started",
                "INFO User logged in",
                "WARNING Low memory",
                "ERROR Database connection failed"
            ));

            Files.write(Paths.get("log3.txt"), Arrays.asList(
                "INFO Request received",
                "ERROR Server error",
                "INFO Request completed"
            ));

            Path[] files = {
                Paths.get("log1.txt"),
                Paths.get("log2.txt"),
                Paths.get("log3.txt")
            };

            String keyword = "ERROR";

            int totalLines = 0;
            int keywordLines = 0;

            for (Path file : files) {

                int fileLines = 0;
                int fileKeywordLines = 0;

                List<String> lines = Files.readAllLines(file);

                for (String line : lines) {
                    totalLines++;
                    fileLines++;

                    if (line.contains(keyword)) {
                        keywordLines++;
                        fileKeywordLines++;
                    }
                }

                long size = Files.size(file);

                System.out.println("File: " + file.getFileName());
                System.out.println("Size: " + size + " bytes");
                System.out.println("Lines: " + fileLines);
                System.out.println("Keyword count: " + fileKeywordLines);
                System.out.println();
            }

            System.out.println("Total lines: " + totalLines);
            System.out.println("Total lines containing '" +
                               keyword + "': " + keywordLines);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}