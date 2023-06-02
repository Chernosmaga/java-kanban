package tasks;

import enums.Status;
import enums.Type;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private Epic epic;
    private Type type;
    private Integer epicId;

    public Subtask(String title, String description, Integer id, Status status) {
        super(title, description, id, status);
        this.type = Type.SUBTASK;
    }

    public Subtask(String title, String description, Status status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
        this.type = Type.SUBTASK;
    }

    public Subtask(String title, String description, int id, Status status, Epic epic) {
        super(title, description, id, status);
        this.epic = epic;
        this.type = Type.SUBTASK;
    }

    public Subtask(Integer epicId, String title, String description, long duration, Instant startTime, Status status) {
        super(title, description, duration, startTime, status);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public Subtask() {}

    public Integer getEpicId() {
        return epicId;
    }

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
                ", status='" + getStatus() + ", start time=" + getStartTime().toEpochMilli() +
                ", duration=" + getDuration() + ", end time=" + getEndTime().toEpochMilli() + '\'' + '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", epic.getId(),  getId(), getType(), getTitle(),
                getStatus(), getDescription(), getStartTime(), getDuration());
    }

}
