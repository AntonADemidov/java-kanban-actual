package managers;

import managers.HistoryManager;
import models.*;

import java.util.HashMap;
import java.util.Set;

public interface TaskManager {
    int createTask(Task task);

    int updateTask(Task task, Integer idForAction);

    void printTask(Integer idForAction);

    void deleteTask(Integer idForAction);

    void printTaskList();

    void clearTaskList();

    int createEpic(Epic epic, Subtask subtask1, Subtask subtask2);

    int createEpic(Epic epic, Subtask subtask);

    int updateEpic(Epic epic, Subtask subtask1, Subtask subtask2, Integer idForAction);

    void printEpic(Integer idForAction);

    void deleteEpic(Integer idForAction);

    void printEpicList();

    void clearEpicList();

    void printEpicSubtaskList(Integer idForAction);

    int createSubtask(Subtask subtask, Integer idForAction);

    int updateSubtask(Subtask subtask, Integer epicIdForAction, Integer subtaskIdForAction);

    void printSubtask(Integer idForAction);

    void deleteSubtask(Integer idForAction);

    void printSubtaskList();

    void clearSubtaskList();

    void getHistory();

    void getPrioritizedTasks();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Subtask> getSubtasks();

    public HashMap<Integer, Epic> getEpics();

    HistoryManager getHistoryManager();

    Set<Task> getPrioritizedList();
}