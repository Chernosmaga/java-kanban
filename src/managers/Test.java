package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;

public class Test {

    public static void main(String[] args) {

        System.out.println("Creating the first manager...\n");
        FileBackedTasksManager fileBackedTaskManagerFirst = new FileBackedTasksManager(new File("resources/back up.csv"));

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
        FileBackedTasksManager fileBackedTaskManagerSecond =
                fileBackedTaskManagerFirst.loadFromFile(new File("resources/back up.csv"));

        System.out.println("\nThe history for the second manager:");
        System.out.println(fileBackedTaskManagerSecond.getHistory());

        System.out.println("\nThese are tasks of a manager:");
        System.out.println(fileBackedTaskManagerSecond.getTasks());
        System.out.println(fileBackedTaskManagerSecond.getEpics());
        System.out.println(fileBackedTaskManagerSecond.getSubtasks());
    }
}
