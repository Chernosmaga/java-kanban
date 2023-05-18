package tasks;

public class Task {

    private String title;
    private String description;
    private Integer id = 0; // если идентификатор не проинициализировать, то NullPointerException
    private Status status;
    private Type type;

    public Task(String title, String description, Integer id, Status status) { // конструктор для менеджера
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = type.TASK;
    }

    public Task(String title, String description, Status status) { // конструктор для пользователя
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type.TASK;
    }

    public Task() {}

    public Type getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    public String toStringFromFile() { // метод для получения данных из файла
        return String.format("%s,%s,%s,%s,%s,%s", id, type, title, status, description, "");
    }

}
