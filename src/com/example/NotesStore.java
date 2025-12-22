package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class NotesStore {
    private final Path dataFile;
    private final ReentrantLock lock = new ReentrantLock();

    public NotesStore(String filePath) {
        this.dataFile = Paths.get(filePath);
        ensureDataDir();
    }

    private void ensureDataDir() {
        try {
            Path dir = dataFile.getParent();
            if (dir != null && !Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            if (!Files.exists(dataFile)) {
                Files.createFile(dataFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию или файл: " + dataFile, e);
        }
    }

    public long addNote(String text) throws IOException {
        lock.lock();
        try {
            List<String> lines = Files.exists(dataFile) ? Files.readAllLines(dataFile) : new ArrayList<>();
            long newId = lines.isEmpty() ? 1 : parseId(lines.get(lines.size() - 1)) + 1;
            String newLine = newId + "," + escapeCsv(text);
            Files.write(dataFile, (newLine + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return newId;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeNote(long idToRemove) throws IOException {
        lock.lock();
        try {
            if (!Files.exists(dataFile)) return false;

            List<String> lines = Files.readAllLines(dataFile);
            boolean found = false;
            List<String> updated = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                long id = parseId(line);
                if (id == idToRemove) {
                    found = true;
                    continue;
                }
                updated.add(line);
            }

            if (found) {
                Files.write(dataFile, updated);
            }
            return found;
        } finally {
            lock.unlock();
        }
    }

    public long countNotes() throws IOException {
        if (!Files.exists(dataFile)) return 0;
        try (var linesStream = Files.lines(dataFile)) {
            return linesStream.filter(line -> !line.trim().isEmpty()).count();
        }
    }

    public List<String> listNotes() throws IOException {
        List<String> notes = new ArrayList<>();
        if (!Files.exists(dataFile)) return notes;

        List<String> lines = Files.readAllLines(dataFile);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            int comma = line.indexOf(',');
            if (comma == -1) continue;
            String id = line.substring(0, comma).trim();
            String text = unescapeCsv(line.substring(comma + 1));
            notes.add(id + ": " + text);
        }
        return notes;
    }

    // --- Вспомогательные методы ---

    private long parseId(String line) {
        int comma = line.indexOf(',');
        if (comma == -1) return -1;
        try {
            return Long.parseLong(line.substring(0, comma).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String escapeCsv(String s) {
        if (s == null) return "";
        s = s.replace("\"", "\"\"");
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            s = "\"" + s + "\"";
        }
        return s;
    }

    private String unescapeCsv(String s) {
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1).replace("\"\"", "\"");
        }
        return s;
    }
}
