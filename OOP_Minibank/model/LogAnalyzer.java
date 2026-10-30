package model;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class LogAnalyzer {

    public static void main(String[] args) throws IOException {
        Path folder = Paths.get("logs");
        String keyword = "Deposit";

        int totalLines = 0;
        int matchingLines = 0;

        try (DirectoryStream<Path> files = Files.newDirectoryStream(folder, "*.txt")) {
            for (Path file : files) {
                int fileLines = 0;

                for (String line : Files.readAllLines(file)) {
                    totalLines++;
                    fileLines++;

                    if (line.contains(keyword)) {
                        matchingLines++;
                    }
                }

                BasicFileAttributes attributes =
                        Files.readAttributes(file, BasicFileAttributes.class);

                System.out.println("File: " + file.getFileName());
                System.out.println("Lines: " + fileLines);
                System.out.println("Size: " + attributes.size() + " bytes");
            }
        }

        System.out.println("Total lines: " + totalLines);
        System.out.println("Lines containing '" + keyword + "': " + matchingLines);
    }
}