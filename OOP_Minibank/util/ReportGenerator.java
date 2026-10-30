package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {

    public static void generate(Path logsFolder, Path reportFile)
            throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        try (BufferedWriter writer = Files.newBufferedWriter(reportFile);
             DirectoryStream<Path> files =
                     Files.newDirectoryStream(logsFolder, "*.txt")) {

            writer.write("End-of-Day Transaction Report");
            writer.newLine();

            for (Path file : files) {
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split("\\s+");

                        if (parts.length >= 2) {
                            try {
                                long amount = Long.parseLong(parts[1]);

                                if (parts[0].equalsIgnoreCase("DEPOSIT")) {
                                    totalDeposits += amount;
                                } else if (parts[0].equalsIgnoreCase("WITHDRAWAL")) {
                                    totalWithdrawals += amount;
                                }
                            } catch (NumberFormatException e) {
                                // Ignore lines without a numeric amount.
                            }
                        }
                    }
                }

                BasicFileAttributes attr =
                        Files.readAttributes(file, BasicFileAttributes.class);

                writer.write("File: " + file.getFileName()
                        + ", Size: " + attr.size()
                        + " bytes, Modified: " + attr.lastModifiedTime());
                writer.newLine();
            }

            writer.write("Total Deposits: " + totalDeposits);
            writer.newLine();
            writer.write("Total Withdrawals: " + totalWithdrawals);
            writer.newLine();
        }

        System.out.println("Report saved to: " + reportFile);
        System.out.println("Total Deposits: " + totalDeposits);
        System.out.println("Total Withdrawals: " + totalWithdrawals);
    }
}