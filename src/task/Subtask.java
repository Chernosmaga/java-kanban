package task;

public class Subtask extends Task {

    Epic epic;
    public Subtask(String name, String description, Integer id, Status status) { // конструктор для менеджера
        super(name, description, id, status);
    }

    public Subtask(String name, String description, Status status, Epic epic) { // конструктор для пользователя
        super(name, description, status);
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
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
