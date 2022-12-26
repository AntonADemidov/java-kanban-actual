package managers;

import models.Task;
import models.Node;

import java.util.List;
import java.util.Map;

public interface HistoryManager {
    void add(Task task);

    void remove(Integer id);

    List<Task> getHistory();

    Map<Integer, Node> getNodeMap();

    Node getFirst();

    Node getLast();
}