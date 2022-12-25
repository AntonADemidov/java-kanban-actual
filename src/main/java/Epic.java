import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private LocalDateTime endTime;
    private List<Integer> subtaskIds;

    public Epic(Integer id, Types type, String name, Status status, LocalDateTime startTime, Duration duration,
                LocalDateTime endTime, String description, List<Integer> subtaskIds) {
        super(id, type, name, status, startTime, duration, description);
        this.endTime = endTime;
        this.subtaskIds = subtaskIds;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subtaskIds.equals(epic.subtaskIds) && endTime.equals(epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, endTime);
    }

    @Override
    public String toString() {
        String result = "Class = '" + getType() + "'," + "\n"
                + "epicId = '" + getId() + "'," + "\n"
                + "name = '" + getName() + "'," + "\n"
                + "status = '" + getStatus() + "'," + "\n"
                + "start time = '" + getStartTime() + "'," + "\n"
                + "duration = '" + getDuration() + "'," + "\n"
                + "end time = '" + getEndTime() + "'," + "\n"
                + "description = {" + getDescription() + "}," + "\n";

        if (subtaskIds.size() != 0) {
            result = result + "subtaskIds = " + subtaskIds + ".";

        } else {
            result = result + "subtaskIds = [null].";
        }
        return result;
    }
}