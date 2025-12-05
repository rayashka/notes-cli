# Notes CLI

**GitHub:** rayashka (https://github.com/rayashka)


Консольная утилита для управления заметками на Java.

## Команды запуска

### Локально (без Docker):

# Компиляция
javac src/com/example/*.java

# Добавление заметки
java -cp src com.example.App --cmd=add --text="Купить хлеб"

# Просмотр всех заметок
java -cp src com.example.App --cmd=list

# Удаление заметки по ID
java -cp src com.example.App --cmd=rm --id=1

# Подсчет заметок
java -cp src com.example.App --cmd=count