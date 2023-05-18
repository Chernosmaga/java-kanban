package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager { // отдельный интерфейс для хранения необходимых методов

    void add(Task task); // добавляет Задачу в лист просмотренных задач

    void remove(int id); // удаляет Задачу по идентификатору

    List<Task> getHistory();

}
