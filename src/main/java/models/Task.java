package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private Integer id;
    private final Types type;
    private String name;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;
    private String description;

    public Task(Integer id, Types type, String name, Status status, LocalDateTime startTime, Duration duration,
                String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.description = description;
    }

    public LocalDateTime getEndTime() {
        LocalDateTime endTime;
        if (this.startTime != null) {
            if (this.duration != null) {
                endTime = this.startTime.plus(this.duration);
            } else {
                endTime = null;
            }
        } else {
            endTime = null;
        }
        return endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Types getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) && type == task.type && name.equals(task.name) && status == task.status &&
                duration.equals(task.duration) && startTime.equals(task.startTime) && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, duration, startTime, description);
    }

    @Override
    public String toString() {
        return "Class = '" + type + "'," + "\n"
                + "id = '" + id + "'," + "\n"
                + "name = '" + name + "'," + "\n"
                + "status = '" + status + "'," + "\n"
                + "start time = '" + startTime + "'," + "\n"
                + "duration = '" + duration + "'," + "\n"
                + "end time = '" + getEndTime() + "'," + "\n"
                + "description = {" + description + "}.";
    }
}