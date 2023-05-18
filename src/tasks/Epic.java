package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private static ArrayList<Integer> subtasksIds = new ArrayList<>(); // лист с идентификаторами подзадач
    private Type type;

    public Epic(String title, String description, Integer id, Status status) { // конструктор для менеджера
        super(title, description, id, status);
        this.type = type.EPIC;
    }

    public Epic(String title, String description, Status status) { // конструктор для пользователя
        super(title, description, status);
        this.type = type.EPIC;
    }

    public Epic() {}

    public static ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        Epic.subtasksIds = subtasksIds;
    }

    @Override
    public Type getType() {
        return type;
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

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", getId(), getType(), getTitle(),
                getStatus(), getDescription(),"");
    }

}
