package task;

public class Task {

    protected String name; // название задачи
    protected String description; // описание задачи
    protected Integer id = 0; // идентификатор задачи, если его не проинициализировать, то NullPointerException
    protected Status status; // статус задачи
    public Task(String name, String description, Integer id, Status status) { // конструктор для менеджера
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String name, String description, Status status) { // конструктор для пользователя
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
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
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

}
