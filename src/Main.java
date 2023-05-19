import managers.InMemoryManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {
    static InMemoryManager manager = new InMemoryManager();
    public static void main(String[] args) {

        // создаю задачу и добавляю в мапу
        Task task1 = new Task("Task 1", "Groceries", Status.NEW);
        manager.addTask(task1);

        // создаю эпик и добавляю в мапу
        Epic epic1 = new Epic("Epic 1", "Go to the shop", Status.NEW);
        manager.addEpic(epic1);

        // создаю подзадачи и обе добавляю в мапу
        Subtask subtask1 = new Subtask("Shopping", "Bread", Status.NEW, epic1);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Shopping", "Milk", Status.IN_PROGRESS, epic1);
        manager.addSubtask(subtask2);

        Task task2 = new Task("Task 2", "Sport", Status.NEW);
        manager.addTask(task2);

        Epic epic2 = new Epic("Epic 2", "Go to the gym", Status.NEW);
        manager.addEpic(epic2);

        Subtask subtask3 = new Subtask("Gym", "Running", Status.NEW, epic2);
        manager.addSubtask(subtask3);

        // печать всех задач, эпиков и подзадач, которые есть на данный момент
        System.out.println("Tasks: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("Subtasks: " + manager.getSubtasks());

        // обновление эпика и подзадачи
        manager.updateTask(epic2);
        manager.updateSubtask(subtask2);

        // печать обновлённых эпика и подзадачи
        System.out.println("Updated epics: " + manager.getEpics());
        System.out.println("Updated subtasks: " + manager.getSubtasks());

        // удаление рандомного эпика и подзадачи
        manager.deleteEpicById(1);
        manager.deleteSubtaskById(2);

        // печать оставшихся задач, эпиков и подзадач
        System.out.println("Tasks: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("Subtasks: " + manager.getSubtasks());

        // получение рандомной Задачи и Эпика
        manager.getTaskById(5);
        manager.getEpicById(2);
        manager.getTaskById(1);
        manager.getEpicById(2);

        // печать истории полученной Задачи и Эпика
        System.out.println("There's the history :\n" + manager.getHistory());

    }
}
