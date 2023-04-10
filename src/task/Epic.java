package task;

import java.util.ArrayList;

public class Epic extends Task {
    public static ArrayList<Integer> subtasksIds = new ArrayList<>(); // лист с идентификаторами подзадач

    public Epic(String name, String description, Integer id, Status status) { // конструктор для менеджера
        super(name, description, id, status);
    }

    public Epic(String name, String description, Status status) { // конструктор для пользователя
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        Epic.subtasksIds = subtasksIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

}
