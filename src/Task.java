import java.util.Objects;

public class Task {
    private Types type;
    private Integer id;
    private String name;
    private Status status;
    private String description;

    public Task(Types type, Integer id, String name, Status status, String description) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) && name.equals(task.name) && status.equals(task.status)
                && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description);
    }

    @Override
    public String toString() {
        return "Class = '" + type + "'," + "\n"
                + "id = '" + id + "'," + "\n"
                + "name = '" + name + "'," + "\n"
                + "status = '" + status + "'," + "\n"
                + "description = {" + description + "}.";
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}