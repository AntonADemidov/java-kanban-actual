import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, Integer epicId, Types type, String name, Status status, LocalDateTime startTime,
                   Duration duration, String description) {
        super(id, type, name, status, startTime, duration, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId.equals(subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Class = '" + getType() + "'," + "\n"
                + "id = '" + getId() + "'," + "\n"
                + "epicId = '" + epicId + "'," + "\n"
                + "name = '" + getName() + "'," + "\n"
                + "status = '" + getStatus() + "'," + "\n"
                + "start time = '" + getStartTime() + "'," + "\n"
                + "duration = '" + getDuration() + "'," + "\n"
                + "end time = '" + getEndTime() + "'," + "\n"
                + "description = {" + getDescription() + "}.";
    }
}