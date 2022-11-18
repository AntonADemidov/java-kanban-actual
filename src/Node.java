import java.util.Objects;

public class Node {
    private final Task task;
    private Node prev;
    private Node next;

    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

    public Task getTask() {
        return task;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return task.equals(node.task) && prev.equals(node.prev) && next.equals(node.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, prev, next);
    }
}