package tasks;

public class Subtask extends Task {

    private Epic epic;
    private Type type;
    public Subtask(String title, String description, Integer id, Status status) { // конструктор для менеджера
        super(title, description, id, status);
        this.type = type.SUBTASK;
    }

    public Subtask(String title, String description, Status status, Epic epic) { // конструктор для пользователя
        super(title, description, status);
        this.epic = epic;
        this.type = type.SUBTASK;
    }

    public Subtask(String title, String description, int id, Status status, Epic epic) {
        super(title, description, id, status);
        this.epic = epic;
        this.type = type.SUBTASK;
    }

    public Subtask() {}

    public Epic getEpic() { // метод для получения эпика определённой подзадачи
        return epic;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", getId(), getType(), getTitle(),
                getStatus(), getDescription(), epic.getId());
    }

}
