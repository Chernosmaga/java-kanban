package task;

public class Subtask extends Task {

    private Epic epic;
    public Subtask(String title, String description, Integer id, Status status) { // конструктор для менеджера
        super(title, description, id, status);
    }

    public Subtask(String title, String description, Status status, Epic epic) { // конструктор для пользователя
        super(title, description, status);
        this.epic = epic;
    }

    public Epic getEpic() { // метод для получения эпика определённой подзадачи
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
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

}
