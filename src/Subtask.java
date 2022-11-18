import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Types type, Integer id, String name, Status status, String description, Integer epicId) {
        super(type, id, name, status, description);
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
                + "description = {" + getDescription() + "}.";
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
}