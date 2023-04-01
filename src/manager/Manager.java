package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    protected static int id;
    protected static HashMap<Integer, Task> tasks;
    protected static HashMap<Integer, Subtask> subtasks;
    protected static HashMap<Integer, Epic> epics;

    public Manager() {
        id = 0; // если не проинициализировать, то схвачу NullPointerException
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public void addTask(Task task) { // создание задачи
        task.setId(++id); // префиксная инкрементация, не знаю почему, но иначе не работает
        tasks.put(id, task);
    }

    public Task getTaskById(int id) { // получение задачи по идентификатору
        Task task = tasks.getOrDefault(id, null); // обязательно проверяю на null, чтоб не схватить ошибку
        return task;
    }

    public void deleteTasks() { // удаление всех задач из мапы
        tasks.clear();
    }

    public HashMap<Integer, Task> getTasks() { // получение списка всех задач
        return tasks;
    }

    public void updateTask(Task task) { // обновление задачи
        tasks.put(task.getId(), task);
    }

    public void deleteTaskById(int id) { // удаление задачи по идентификатору
            tasks.remove(id);
    }

    public void addEpic(Epic epic) { // добавление эпика
        epic.setId(++id);
        epic.setStatus(Status.NEW); // устанавливаю сразу же статус для нового эпика
        epics.put(id, epic);
    }

    public Epic getEpicById(int id) { // получение эпика по идентификатору
        return epics.getOrDefault(id, null);
    }

    public void deleteEpics() { // удаление всех эпиков из мапы
        epics.clear();
    }

    public HashMap<Integer, Epic> getEpics() { // получение списка всех эпиков
        return epics;
    }

    public void updateEpic(Epic epic) { // обновление эпика
        epic.setSubtasksIds(epics.get(epic.getId()).getSubtasksIds());
        epics.put(epic.getId(), epic);
        statusUpdate(epic); // обновление статуса
    }

    public void deleteEpicById(int id) { // удаление эпика по идентификатору
        if (epics.containsKey(id)) { // проверяю наличие идентификатора
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Integer subtaskId : epic.getSubtasksIds()) { // так же прохожусь по идентификаторам подзадач для удаления
                subtasks.remove(subtaskId);
            }
            epic.setSubtasksIds(new ArrayList<>()); // после этого обновляю лист с идентификаторами подзадачи
        }
    }

    public void addSubtask(Subtask subtask) { // добавление подзадачи
        subtask.setId(++id);
        subtasks.put(id, subtask);
        Epic.subtasksIds.add(id);
    }

    public Subtask getSubtaskById(int id) { // получение подзадачи по идентификатору
        return  subtasks.getOrDefault(id, null);
    }

    public void deleteSubtasks() {// удаление всех подзадач
        ArrayList<Epic> epicsForUpdate = new ArrayList<>(); // новый лист, чтоб сложить туда значения из мапы подзадач
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().setSubtasksIds(new ArrayList<>()); // снова обновляю лист с идентификаторами подзадач
            if (!epicsForUpdate.contains(subtask.getEpic())) {
                epicsForUpdate.add(subtask.getEpic());
            }
        }
        subtasks.clear(); // опустошаю мапу подзадач
        for (Epic epic : epicsForUpdate) { // устанавливаю для эпиков новый статус
            epic.setStatus(Status.NEW);
        }
    }

    public HashMap <Integer, Subtask> getSubtasks() { // получение списка всех подзадач
        return subtasks;
    }

    public void updateSubtask(Subtask subtask) { // обновление подзадачи
        subtasks.put(subtask.getId(), subtask);
        statusUpdate(subtask.getEpic());// обновление статуса
    }

    public void deleteSubtaskById(int id) { // удаление подзадачи по идентификатору
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getSubtasksIds().remove(id); // при удалении подзадачи нужно так же удалить идентификатор из листа
            statusUpdate(epic); // обновление статуса
            subtasks.remove(id);
        }
    }

    private void statusUpdate(Epic epic) { // проверка статуса, метод распространяется только на этот класс

        boolean isNew = true;
        boolean isDone = true;

        if (epic.getSubtasksIds().size() == 0) { // если у меня пустой лист с идентификаторами подзадач
            epic.setStatus(Status.NEW); // устанавливаю им статус NEW
            return;
        }

        for (Integer epicSubtask : epic.getSubtasksIds()) { // прохожусь по идентификаторам
            Status status = subtasks.get(epicSubtask).getStatus(); // переменной статус присваиваю значение идентификатора
            if (status != Status.NEW) {
                isNew = false;
            }
            if (status != Status.DONE) {
                isDone = false;
            }
        }

        if (isNew) { // проверка статуса
            epic.setStatus(Status.NEW);
        } else if (isDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
