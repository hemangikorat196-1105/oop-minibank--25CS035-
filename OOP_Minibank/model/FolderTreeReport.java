package model;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderTreeReport {
    public static void main(String[] args) throws IOException {
        Path folder = Paths.get(".");
        Path report = Paths.get("folder-report.txt");

        try (var writer = Files.newBufferedWriter(report);
             var paths = Files.walk(folder)) {

            paths.filter(Files::isRegularFile).forEach(path -> {
                try {
                    BasicFileAttributes attr =
                            Files.readAttributes(path, BasicFileAttributes.class);

                    String line = "Name: " + path
                            + ", Size: " + attr.size()
                            + " bytes, Modified: " + attr.lastModifiedTime();

                    writer.write(line);
                    writer.newLine();
                    System.out.println(line);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }

        System.out.println("Report saved to folder-report.txt");
    }
}