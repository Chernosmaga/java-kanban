package memory;

import enums.Status;
import exceptions.TaskConflictException;
import history.HistoryManager;
import managers.Managers;
import tasks.*;

import java.time.Instant;
import java.util.*;

public class InMemoryManager implements Manager {
    protected int id;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager;
    // компаратор для сравнения задач по типу времени начала
    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);
    // сет из отсортированных задач по компаратору
    private final Set<Task> prioritizedTasks = new TreeSet<>(comparator);

    public InMemoryManager() {
        id = 0;
        historyManager = Managers.getDefaultHistory();
    }

    public InMemoryManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int idIncrease() {
        return ++id;
    }

    @Override
    public Task addTask(Task task) {
        if (task != null && !tasks.containsKey(task.getId())) {
            int newId = idIncrease();
            task.setId(newId);
            tasks.put(newId, task);
            addTaskToPrioritizedList(task);
        } else {
            return null;
        }
        return task;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null && tasks.containsKey(id)) {
            historyManager.add(task);
        } else {
            return null;
        }
        return task;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        if (tasks.size() != 0) {
            return tasks;
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            addTaskToPrioritizedList(task);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.removeIf(task -> task.getId() == id);
        }
    }

    @Override
    public Epic addEpic(Epic epic) {
        if (epic != null && !epics.containsKey(epic.getId())) {
            int newId = idIncrease();
            epic.setId(newId);
            epics.put(newId, epic);
        } else {
            return null;
        }
        return epic;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null && epics.containsKey(id)) {
            historyManager.add(epic);
        } else {
            return null;
        }
        return epic;
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        if (epics.size() != 0) {
            return epics;
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            statusUpdate(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubtasksIds()) {
                historyManager.remove(id);
                subtasks.remove(subtaskId);
                prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), subtaskId));
            }
            epics.remove(id);
            historyManager.remove(epic.getId());
        }
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        if (subtask != null && !subtasks.containsKey(subtask.getId())) {
            int newId = idIncrease();
            subtask.setId(newId);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                addTaskToPrioritizedList(subtask);
                subtasks.put(newId, subtask);
                epic.setSubtasksIds(newId);
                statusUpdate(epic);
                updateEpicTime(epic);
            }
        } else {
            return null;
        }
        return subtask;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null && subtasks.containsKey(id)) {
            historyManager.add(subtask);
        } else {
            return null;
        }
        return subtask;
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
                Subtask subtask = subtasks.get(subtaskId);
                prioritizedTasks.remove(subtask);
            }
            epic.getSubtasksIds().clear();
        }
    }

    @Override
    public HashMap <Integer, Subtask> getSubtasks() { // получение списка всех подзадач
        if (subtasks.size() != 0) {
            return subtasks;
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            Epic epic = getEpicById(subtask.getId());
            subtasks.put(subtask.getId(), subtask);
            statusUpdate(epic);
            addTaskToPrioritizedList(subtask);
            updateEpicTime(epic);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            subtasks.remove(id);
            epic.getSubtasksIds().remove(id);
            historyManager.remove(id);
            statusUpdate(epic);
            updateEpicTime(epic);
            prioritizedTasks.remove(subtask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            List<Subtask> newSubtasks = new ArrayList<>();
            for (int i = 0; i < epic.getSubtasksIds().size(); i++) {
                newSubtasks.add(subtasks.get(epic.getSubtasksIds().get(i)));
            }
            return newSubtasks;
        } else {
            return new ArrayList<>();
        }
    }

    private void statusUpdate(Epic epic) {

        boolean isNew = true;
        boolean isDone = true;

        if (epic.getSubtasksIds().size() == 0) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (Integer epicSubtask : epic.getSubtasksIds()) {
            Status status = subtasks.get(epicSubtask).getStatus();
            if (status != Status.NEW) {
                isNew = false;
            }
            if (status != Status.DONE) {
                isDone = false;
            }
        }

        if (isNew) {
            epic.setStatus(Status.NEW);
        } else if (isDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private void updateEpicTime(Epic epic) {
        List<Subtask> subtasks = getSubtasksOfEpic(epic.getId());
        Instant startTime = subtasks.get(0).getStartTime();
        Instant endTime = subtasks.get(0).getEndTime();

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            } else if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        long duration = (endTime.toEpochMilli() - startTime.toEpochMilli());
        epic.setDuration(duration);
    }

    private void addTaskToPrioritizedList(Task task) {
        prioritizedTasks.add(task);
        validation();
    }

    private boolean timeChecker(Task task) {
        List<Task> tasks = getPrioritizedTasks();
        int size = 0;
        if (tasks.size() > 0) {
            for (Task value : tasks) {
                if (value.getStartTime() != null && value.getEndTime() != null) {
                    if (task.getStartTime().isBefore(value.getStartTime()) &&
                            task.getEndTime().isBefore(value.getStartTime())) {
                        return true;
                    } else if (task.getStartTime().isAfter(value.getEndTime()) &&
                            task.getEndTime().isAfter(value.getEndTime())) {
                        return true;
                    }
                } else {
                    size++;
                }
            }
            return size == tasks.size();
        } else {
            return true;
        }
    }

    private void validation() {
        List<Task> tasks = getPrioritizedTasks();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            boolean isTaskTimeSimilar = timeChecker(task);

            if (isTaskTimeSimilar) {
                throw new TaskConflictException("Task conflict caused by similar tasks time");
            }
        }
    }

}