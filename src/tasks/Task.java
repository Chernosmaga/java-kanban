package tasks;

import enums.Status;
import enums.Type;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

public class Task {
    private String title;
    private String description;
    private int id;
    private Status status;
    private Type type;
    private Instant startTime = Instant.now();
    private long duration;

    public Task(String title, String description, Integer id, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = Type.TASK;
    }

    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
    }

    public Task(int id, String title, String description, long duration, Instant startTime, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.status = status;
        this.type = Type.TASK;
    }

    public Task() {}

    public Instant getEndTime() {
        long seconds = 60L;
        return startTime.plusSeconds(duration * seconds);
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

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
                ", id=" + getId() +
                ", status='" + status + ", start time=" + startTime.toEpochMilli() +
                ", duration=" + duration + ", end time=" + getEndTime().toEpochMilli() + '\'' + '}';
    }

    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                id, type, title, status, description, startTime, duration, "");
    }

}
