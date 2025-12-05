package com.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Используйте: --cmd=<add|list|rm|count> [--text=\"текст\"] [--id=<номер>]");
            return;
        }

        Map<String, String> params = parseArgs(args);
        String cmd = params.get("cmd");

        NotesStore store = new NotesStore();

        try {
            switch (cmd) {
                case "add":
                    String text = params.get("text");
                    if (text == null || text.isEmpty()) {
                        System.out.println("Ошибка: не указан текст заметки");
                        return;
                    }
                    store.addNote(text);
                    break;
                case "list":
                    store.listNotes();
                    break;
                case "rm":
                    String idStr = params.get("id");
                    if (idStr == null || idStr.isEmpty()) {
                        System.out.println("Ошибка: не указан ID");
                        return;
                    }
                    int id = Integer.parseInt(idStr);
                    store.removeNote(id);
                    break;
                case "count":
                    int count = store.countNotes();
                    System.out.println(count);
                    break;
                default:
                    System.out.println("Неизвестная команда: " + cmd);
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: ID должен быть числом");
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> params = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.substring(2).split("=", 2);
                if (parts.length == 2) {
                    params.put(parts[0], parts[1].replaceAll("^\"|\"$", ""));
                } else {
                    params.put(parts[0], "");
                }
            }
        }
        return params;
    }
}

