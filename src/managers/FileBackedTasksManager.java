package managers;

import exceptions.*;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static tasks.Type.*;

public class FileBackedTasksManager extends InMemoryManager {
    private static File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
        file = new File("back up.csv");
        if (!file.isFile()) {
            try {
                Path path = Files.createFile(Paths.get("back up.csv"));
            } catch (IOException exception) {
                throw new ManagerSaveException("There is a problem creating a file, maybe it's already exists");
            }
        }
    }

    public FileBackedTasksManager() {}

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task foundTask = super.getTaskById(id);
        if (foundTask != null) {
            save();
            return foundTask;
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask foundSubtask = super.getSubtaskById(id);
        if (foundSubtask != null) {
            save();
            return foundSubtask;
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Epic foundEpic = super.getEpicById(id);
        if (foundEpic != null) {
            save();
            return foundEpic;
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    public void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,title,status,description,epic\n");
            HashMap<Integer, String> allTasks = new HashMap<>();

            HashMap<Integer, Task> tasks = super.getTasks();
            for (Integer id : tasks.keySet()) {
                allTasks.put(id, tasks.get(id).toStringFromFile());
            }

            HashMap<Integer, Epic> epics = super.getEpics();
            for (Integer id : epics.keySet()) {
                allTasks.put(id, epics.get(id).toStringFromFile());
            }

            HashMap<Integer, Subtask> subtasks = super.getSubtasks();
            for (Integer id : subtasks.keySet()) {
                allTasks.put(id, subtasks.get(id).toStringFromFile());
            }

            for (String value : allTasks.values()) {
                 writer.write(String.format("%s\n", value));
            }
            writer.write("\n");

            for (Task task : getHistory()) {
                writer.write(task.getId() + ",");
            }

        } catch (IOException exception) {
            throw new ManagerSaveException("Unable to read file");
        }
    }

     public static Task fromString(String content, Type type, FileBackedTasksManager manager) {
        Task task = new Task();
        String epicId = null;
        String[] elements = content.split(",");
        int id = Integer.parseInt(elements[0]);
        String title = String.valueOf(elements[2]);
        Status status = Status.valueOf(elements[3]);
        String description = elements[4];
        if (elements.length == 6) {
            epicId = elements[5];
        }

        if (type == TASK) {
            return new Task(title, description, id, status);
        } else if (type == SUBTASK) {
            return new Subtask(title, description, id, status, manager.getEpics().get(Integer.valueOf(epicId)));
        } else if (type == EPIC) {
            return new Epic(title, description, id, status);
        }
        return task;
    }

    private static Task getTasksOfDifferentKinds(int id, InMemoryManager inMemoryManager) {
        Task task = inMemoryManager.getTasks().get(id);
        if (task != null) {
            return task;
        }
        Task epic = inMemoryManager.getEpics().get(id);
        if (epic != null) {
            return epic;
        }
        Task subtask = inMemoryManager.getSubtasks().get(id);
        if (subtask != null) {
            return subtask;
        }
        return null;
    }

    private static List<Integer> historyFromString(String value) {
        String[] idsString = value.split(",");
        List<Integer> tasksId = new ArrayList<>();
        for (String idString : idsString) {
            tasksId.add(Integer.valueOf(idString));
        }
        return tasksId;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String data;
        try {
            data = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Unable to read file");
        }
        String[] lines = data.split("\n");
        String history = "";
        boolean isTitle = true;
        boolean isTask = true;
        int maxId = 0;
        int id;

        for (String line : lines) {
            if (isTitle) {
                isTitle = false;
                continue;
            }
            if (line.isEmpty() || line.equals("\r")) {
                isTask = false;
                continue;
            }
            if (isTask) {
                Type type = Type.valueOf(line.split(",")[1]);
                switch (type) {
                    case EPIC:
                        Epic epic = (Epic) fromString(line, type.EPIC, fileBackedTasksManager);
                        id = epic.getId();
                        if (id > maxId) {
                            maxId = id;
                        }
                        fileBackedTasksManager.epics.put(id, epic);
                        break;

                    case SUBTASK:
                        Subtask subtask = (Subtask) fromString(line, type.SUBTASK, fileBackedTasksManager);
                        id = subtask.getId();
                        if (id > maxId) {
                            maxId = id;
                        }
                        fileBackedTasksManager.subtasks.put(id, subtask);
                        break;

                    case TASK:
                        Task task = fromString(line, type.TASK, fileBackedTasksManager);

                        id = task.getId();
                        if (id > maxId) {
                            maxId = id;
                        }
                        fileBackedTasksManager.tasks.put(id, task);
                        break;

                }
            } else {
                history = line;
            }
        }
        fileBackedTasksManager.id = maxId;
        List<Integer> ids = historyFromString(history);
        for (Integer taskId : ids) {
            fileBackedTasksManager.historyManager.add(getTasksOfDifferentKinds(taskId, fileBackedTasksManager));
        }
        return fileBackedTasksManager;
    }

    public static void main(String[] args) {

        System.out.println("Creating the first manager...\n");
        FileBackedTasksManager fileBackedTaskManagerFirst = new FileBackedTasksManager(new File("back up.csv"));

        System.out.println("Creating tasks and adding them into manager...");
        Task task1 = new Task("First task", "Here's the first task", Status.NEW);
        fileBackedTaskManagerFirst.addTask(task1);
        Task task2 = new Task("Second task", "This is the second one", Status.NEW);
        fileBackedTaskManagerFirst.addTask(task2);
        Epic epic1 = new Epic("First epic", "The first epic", Status.DONE);
        fileBackedTaskManagerFirst.addEpic(epic1);
        Epic epic2 = new Epic("Second epic", "Here's the second epic", Status.DONE);
        fileBackedTaskManagerFirst.addEpic(epic2);
        Subtask subtask1 = new Subtask("First subtask", "Subtask number one", Status.IN_PROGRESS, epic1);
        fileBackedTaskManagerFirst.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Second subtask", "The second subtask", Status.NEW, epic2);
        fileBackedTaskManagerFirst.addSubtask(subtask2);

        fileBackedTaskManagerFirst.getTaskById(2);
        fileBackedTaskManagerFirst.getEpicById(3);
        fileBackedTaskManagerFirst.getSubtaskById(5);

        System.out.println("\nThere is the history:");
        System.out.println(fileBackedTaskManagerFirst.getHistory());

        System.out.println("\nCreating the second manager...");
        FileBackedTasksManager fileBackedTaskManagerSecond = loadFromFile(file);

        System.out.println("\nThe history for the second manager:");
        System.out.println(fileBackedTaskManagerSecond.getHistory());

        System.out.println("\nThese are tasks of a manager:");
        System.out.println(fileBackedTaskManagerSecond.getTasks());
        System.out.println(fileBackedTaskManagerSecond.getEpics());
        System.out.println(fileBackedTaskManagerSecond.getSubtasks());
    }
}