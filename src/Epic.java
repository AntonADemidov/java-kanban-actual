import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    public Epic(Types type, Integer id, String name, Status status, String description, ArrayList<Integer> subtaskIds) {
        super(type, id, name, status, description);
        this.subtaskIds = subtaskIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subtaskIds.equals(epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        String result = "Class = '" + getType() + "'," + "\n"
                + "epicId = '" + getId() + "'," + "\n"
                + "name = '" + getName() + "'," + "\n"
                + "status = '" + getStatus() + "'," + "\n"
                + "description = {" + getDescription() + "}," + "\n";

        if (subtaskIds.size() != 0) {
            result = result + "subtaskIds = " + subtaskIds + ".";

        } else {
            result = result + "subtaskIds = [null].";
        }
        return result;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
}