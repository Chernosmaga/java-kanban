package task;

import java.util.ArrayList;

public class Epic extends Task {
    private static ArrayList<Integer> subtasksIds = new ArrayList<>(); // лист с идентификаторами подзадач

    public Epic(String title, String description, Integer id, Status status) { // конструктор для менеджера
        super(title, description, id, status);
    }

    public Epic(String title, String description, Status status) { // конструктор для пользователя
        super(title, description, status);
    }

    public static ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        Epic.subtasksIds = subtasksIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }

}
