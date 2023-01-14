package managers;

import history.HistoryManager;
import tasks.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

public interface TaskManager {
    boolean createTask(Task task) throws URISyntaxException, IOException, InterruptedException;

    boolean updateTask(Task task, Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    void printTask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    void deleteTask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    void printTaskList();

    void clearTaskList() throws URISyntaxException, IOException, InterruptedException;

    boolean createEpic(Epic epic, Subtask subtask1, Subtask subtask2) throws URISyntaxException, IOException, InterruptedException;

    boolean createEpic(Epic epic, Subtask subtask) throws URISyntaxException, IOException, InterruptedException;

    boolean updateEpic(Epic epic, Subtask subtask1, Subtask subtask2, Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    void printEpic(Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    void deleteEpic(Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    void printEpicList();

    void clearEpicList() throws URISyntaxException, IOException, InterruptedException;

    void printEpicSubtaskList(Integer idForAction);

    boolean createSubtask(Subtask subtask, Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    boolean updateSubtask(Subtask subtask, Integer epicIdForAction, Integer subtaskIdForAction) throws URISyntaxException, IOException, InterruptedException;

    void printSubtask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    void deleteSubtask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException;

    void printSubtaskList();

    void clearSubtaskList() throws URISyntaxException, IOException, InterruptedException;

    void getHistory();

    void getPrioritizedTasks();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Subtask> getSubtasks();

    HashMap<Integer, Epic> getEpics();

    HistoryManager getHistoryManager();

    Set<Task> getPrioritizedList();
}