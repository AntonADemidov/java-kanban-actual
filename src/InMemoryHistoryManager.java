import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        if (nodeMap.get(task.getId()) != null) {
            removeNode(nodeMap.get(task.getId()));
        }
        Node newNode = linkLast(task);
        nodeMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(Integer id) {
        removeNode(nodeMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
            if (node.getNext() == null) {
                last = node.getPrev();
            } else {
                node.getNext().setPrev(node.getPrev());
            }
        } else {
            first = node.getNext();
            if (first == null) {
                last = null;
            } else {
                first.setPrev(null);
            }
        }
    }

    private Node linkLast(Task task) {
        Node node = new Node(task, last,null);
        if (first == null) {
            first = node;
        } else {
            last.setNext(node);
        }
        last = node;
        return last;
    }

    private List<Task> getTasks() {
        Node currentNode = first;
        List<Task> historyList = new ArrayList<>();
        while (currentNode != null) {
            historyList.add(currentNode.getTask());
            currentNode = currentNode.getNext();
        }
        return historyList;
    }
}