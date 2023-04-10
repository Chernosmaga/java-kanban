package manager;

import task.Task;

import java.util.List;

public interface HistoryManager { // отдельный интерфейс для хранения необходимых методов

    void addTask(Task task); // добавляет Задачу в лист просмотренных задач

    void removeTask(int id); // удаляет Задачу по идентификатору

    List<Task> getHistory();

}
