package com.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class NotesStore {
    private static final String FILE_PATH = "data/notes.csv";

    public NotesStore() {
        try {
            Files.createDirectories(Paths.get("data"));
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.createFile(Paths.get(FILE_PATH));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла: " + e.getMessage());
        }
    }

    public void addNote(String text) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        int newId = lines.size() + 1;
        String newLine = newId + ";" + text;
        Files.write(Paths.get(FILE_PATH), (newLine + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        System.out.println("Заметка добавлена с ID: " + newId);
    }

    public void listNotes() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        if (lines.isEmpty()) {
            System.out.println("(empty)");
        } else {
            for (String line : lines) {
                System.out.println(line);
            }
        }
    }

    public void removeNote(int id) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        boolean found = false;
        List<String> newLines = new ArrayList<>();
        for (String line : lines) {
            int currentId = Integer.parseInt(line.split(";")[0]);
            if (currentId == id) {
                found = true;
                continue;
            }
            newLines.add(line);
        }
        if (!found) {
            System.out.println("Not found #" + id);
            return;
        }
        // Перезаписываем файл с новыми ID
        Files.write(Paths.get(FILE_PATH), new byte[0]); // Очистка
        for (int i = 0; i < newLines.size(); i++) {
            String[] parts = newLines.get(i).split(";", 2);
            String newLine = (i + 1) + ";" + parts[1];
            Files.write(Paths.get(FILE_PATH), (newLine + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        }
        System.out.println("Заметка #" + id + " удалена");
    }

    public int countNotes() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        return lines.size();
    }
}


public void removeNote(int id) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
    boolean found = false;
    List<String> newLines = new ArrayList<>();
    
    for (String line : lines) {
        int currentId = Integer.parseInt(line.split(";")[0]);
        if (currentId == id) {
            found = true;
            continue;
        }
        newLines.add(line);
    }
    
    if (!found) {
        System.out.println("Not found #" + id);
        return;
    }
    
    // Перезаписываем файл с новыми ID
    Files.write(Paths.get(FILE_PATH), new byte[0]); // Очистка
    for (int i = 0; i < newLines.size(); i++) {
        String[] parts = newLines.get(i).split(";", 2);
        String newLine = (i + 1) + ";" + parts[1];
        Files.write(Paths.get(FILE_PATH), (newLine + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
    }
    System.out.println("Заметка #" + id + " удалена");
}