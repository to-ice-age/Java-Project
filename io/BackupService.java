package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class BackupService {
    private final Path backupRoot;
    private final DateTimeFormatter timestampFormat;

    public BackupService(Path backupRoot) {
        this.backupRoot = backupRoot;
        this.timestampFormat = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        createDirectoryIfNotExists(backupRoot);
    }

    public Path createBackup(Path sourceDirectory) throws IOException {
        String timestamp = LocalDateTime.now().format(timestampFormat);
        Path backupDir = backupRoot.resolve("backup_" + timestamp);
        Files.createDirectories(backupDir);

        try (Stream<Path> paths = Files.walk(sourceDirectory)) {
            paths.filter(Files::isRegularFile)
                 .forEach(source -> {
                     Path relativePath = sourceDirectory.relativize(source);
                     Path targetPath = backupDir.resolve(relativePath);
                     try {
                         Files.createDirectories(targetPath.getParent());
                         Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         throw new UncheckedIOException("Failed to backup file: " + source, e);
                     }
                 });
        }

        return backupDir;
    }

    public long calculateBackupSize(Path backupDirectory) {
        AtomicLong size = new AtomicLong(0);
        try (Stream<Path> paths = Files.walk(backupDirectory)) {
            paths.filter(Files::isRegularFile)
                 .forEach(file -> {
                     try {
                         size.addAndGet(Files.size(file));
                     } catch (IOException e) {
                         throw new UncheckedIOException("Failed to get file size: " + file, e);
                     }
                 });
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to walk backup directory", e);
        }
        return size.get();
    }

    public void listBackupContents(Path backupDirectory, int maxDepth) throws IOException {
        try (Stream<Path> paths = Files.walk(backupDirectory, maxDepth)) {
            paths.forEach(path -> {
                int depth = backupDirectory.relativize(path).getNameCount();
                String indent = "  ".repeat(depth);
                System.out.printf("%s%s%n", indent, path.getFileName());
            });
        }
    }

    public void cleanupOldBackups(int keepLatest) throws IOException {
        try (Stream<Path> backups = Files.list(backupRoot)
                .filter(Files::isDirectory)
                .filter(path -> path.getFileName().toString().startsWith("backup_"))
                .sorted((p1, p2) -> p2.getFileName().toString().compareTo(p1.getFileName().toString()))) {
            
            backups.skip(keepLatest)
                   .forEach(path -> {
                       try {
                           deleteRecursively(path);
                       } catch (IOException e) {
                           throw new UncheckedIOException("Failed to delete backup: " + path, e);
                       }
                   });
        }
    }

    private void deleteRecursively(Path directory) throws IOException {
        if (Files.exists(directory)) {
            Files.walk(directory)
                 .sorted((p1, p2) -> -p1.compareTo(p2)) // Reverse order to delete files before directories
                 .forEach(path -> {
                     try {
                         Files.delete(path);
                     } catch (IOException e) {
                         throw new UncheckedIOException("Failed to delete: " + path, e);
                     }
                 });
        }
    }

    private void createDirectoryIfNotExists(Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create directory: " + directory, e);
        }
    }

    public String formatSize(long bytes) {
        final String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        double size = bytes;

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", size, units[unitIndex]);
    }
}