package tasks;

import enums.Status;
import enums.Type;
import memory.InMemoryManager;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksIds = new ArrayList<>();
    private Type type;
    private static Instant endTime = Instant.ofEpochSecond(32503669200000L);

    public Epic(String title, String description, Integer id, Status status) {
        super(title, description, id, status);
        this.type = Type.EPIC;
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        this.type = Type.EPIC;
    }

    public Epic(int id, String title, String description, long duration, Instant startTime, Status status) {
        super(id, title, description, duration, startTime, status);
        this.endTime = super.getEndTime();
        this.type = Type.EPIC;
    }

    public Epic() {}

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(int id) {
        subtasksIds.add(id);
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        Epic.endTime = endTime;
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
                ", status='" + getStatus() + ", start time=" + getStartTime().toEpochMilli() +
                ", duration=" + getDuration() + ", end time=" + getEndTime().toEpochMilli() + '\'' + '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getId(), getType(), getTitle(),
                getStatus(), getDescription(), getStartTime(), getDuration(), "");
    }

}
