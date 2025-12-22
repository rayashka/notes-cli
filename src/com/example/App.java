package com.example;

import java.util.List;

public class App {
    public static void main(String[] args) {
        String cmd = null;
        String text = null;
        String idStr = null;

        for (String arg : args) {
            if (arg.startsWith("--cmd=")) {
                cmd = arg.substring(6);
            } else if (arg.startsWith("--text=")) {
                text = arg.substring(7);
            } else if (arg.startsWith("--id=")) {
                idStr = arg.substring(5);
            }
        }

        String dataPath = "/app/data/notes.csv";
        NotesStore store = new NotesStore(dataPath);

        try {
            if (cmd == null) {
                System.err.println("❌ Ошибка: не указана команда (--cmd=add|rm|count|list)");
                printUsage();
                System.exit(1);
            }

            switch (cmd) {
                case "add":
                    if (text == null) {
                        System.err.println("❌ Ошибка: для --cmd=add требуется --text=\"...\"");
                        printUsage();
                        System.exit(1);
                    }
                    long id = store.addNote(text);
                    System.out.println("✅ Добавлена заметка #" + id);
                    break;

                case "rm":
                    if (idStr == null) {
                        System.err.println("❌ Ошибка: для --cmd=rm требуется --id=N");
                        printUsage();
                        System.exit(1);
                    }
                    try {
                        long idToRemove = Long.parseLong(idStr);
                        boolean removed = store.removeNote(idToRemove);
                        if (removed) {
                            System.out.println("🗑️ Заметка #" + idToRemove + " удалена");
                        } else {
                            System.out.println("⚠️ Заметка #" + idToRemove + " не найдена");
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("❌ Ошибка: --id должен быть целым числом");
                        System.exit(1);
                    }
                    break;

                case "count":
                    long count = store.countNotes();
                    System.out.println("📊 Всего заметок: " + count);
                    break;

                case "list":
                    List<String> notes = store.listNotes();
                    if (notes.isEmpty()) {
                        System.out.println("📭 Нет заметок");
                    } else {
                        System.out.println("📋 Список заметок:");
                        for (String note : notes) {
                            System.out.println("  • " + note);
                        }
                    }
                    break;

                default:
                    System.err.println("❌ Неизвестная команда: " + cmd);
                    printUsage();
                    System.exit(1);
            }

        } catch (Exception e) {
            System.err.println("❗ Ошибка выполнения: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("\nИспользование:");
        System.out.println("  --cmd=add   --text=\"текст\"     → добавить");
        System.out.println("  --cmd=rm    --id=N              → удалить по ID");
        System.out.println("  --cmd=count                      → посчитать");
        System.out.println("  --cmd=list                       → показать все");
    }
}
