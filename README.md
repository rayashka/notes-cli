# Notes CLI

**GitHub:** rayashka (https://github.com/rayashka)


Консольная утилита для управления заметками на Java.

## Команды запуска

### Запуск в Docker:

# Компиляция
docker build -t notes-cli .

# Добавление заметки
docker run -v notes-data:/app/data notes-cli --cmd=add --text="Моя первая заметка"

# Просмотр всех заметок
docker run -v notes-data:/app/data notes-cli --cmd=list

# Удаление заметки по ID
docker run -v notes-data:/app/data notes-cli --cmd=rm --id=1

# Подсчет заметок
docker run -v notes-data:/app/data notes-cli --cmd=count
