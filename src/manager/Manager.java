package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.HashMap;
import java.util.List;

public interface Manager {


    void addTask(Task task);

    Task getTaskById(int id);

    void deleteTasks();

    HashMap<Integer, Task> getTasks();

    void updateTask(Task task);

    void deleteTaskById(int id);

    void addEpic(Epic epic);

    Epic getEpicById(int id);

    void deleteEpics();

    HashMap<Integer, Epic> getEpics();

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    void addSubtask(Subtask subtask);

    Subtask getSubtaskById(int id);

    void deleteSubtasks();

    HashMap <Integer, Subtask> getSubtasks();

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    List<Task> getHistory();

}
