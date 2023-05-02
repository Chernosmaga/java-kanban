package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.*;

public class InMemoryManager implements Manager {
    private int id;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;
    private HistoryManager historyManager;

    public InMemoryManager() {
        id = 0; // если не проинициализировать, то схвачу NullPointerException
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void addTask(Task task) {
        task.setId(++id); // префиксная инкрементация, не знаю почему, но иначе не работает
        tasks.put(id, task);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.getOrDefault(id, null); // обязательно проверяю на null, чтоб не схватить ошибку
        if (id != 0) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void deleteTasks() { // удаление всех задач из мапы
        tasks.clear();
    }

    @Override
    public HashMap<Integer, Task> getTasks() { // получение списка всех задач
        return tasks;
    }

    @Override
    public void updateTask(Task task) { // обновление задачи
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) { // удаление задачи по идентификатору
        tasks.remove(id);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epic.setStatus(Status.NEW); // сразу же устанавливаю статус для нового эпика
        epics.put(id, epic);
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.getOrDefault(id, null);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void deleteEpics() { // удаление всех эпиков из мапы
        epics.clear();
    }

    @Override
    public HashMap<Integer, Epic> getEpics() { // получение списка всех эпиков
        return epics;
    }

    @Override
    public void updateEpic(Epic epic) {
        epic.setSubtasksIds(epics.get(epic.getId()).getSubtasksIds());
        epics.put(epic.getId(), epic);
        statusUpdate(epic); // обновление статуса
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) { // проверяю наличие идентификатора
            Epic epic = epics.get(id);
            epics.remove(id);
            historyManager.remove(id);
            for (Integer subtaskId : epic.getSubtasksIds()) { // так же прохожусь по идентификаторам подзадач для удаления
                subtasks.remove(subtaskId);
            }
            epic.setSubtasksIds(new ArrayList<>()); // после этого обновляю лист с идентификаторами подзадачи
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
        Epic.getSubtasksIds().add(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.getOrDefault(id, null);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void deleteSubtasks() {
        ArrayList<Epic> epicsForUpdate = new ArrayList<>(); // новый лист, чтоб сложить туда значения из мапы подзадач
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().setSubtasksIds(new ArrayList<>()); // снова обновляю лист с идентификаторами подзадач
            if (!epicsForUpdate.contains(subtask.getEpic())) {
                epicsForUpdate.add(subtask.getEpic());
            }
        }
        subtasks.clear(); // опустошаю мапу подзадач
        historyManager.remove(id); // удаляю из просмотренного листа задачу по идентификатору
        for (Epic epic : epicsForUpdate) { // устанавливаю для эпиков новый статус
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public HashMap <Integer, Subtask> getSubtasks() { // получение списка всех подзадач
        return subtasks;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        statusUpdate(subtask.getEpic());
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getSubtasksIds().remove(id); // при удалении подзадачи нужно так же удалить идентификатор из листа
            statusUpdate(epic); // обновление статуса
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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