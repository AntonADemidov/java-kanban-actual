public interface TaskManager {
    void createTask(Task task);

    void updateTask(Task task);

    void printTask();

    void deleteTask();

    void printTaskList();

    void clearTaskList();

    void createEpic(Epic epic, Subtask subtask1, Subtask subtask2);

    void createEpic(Epic epic, Subtask subtask);

    void updateEpic(Epic epic, Subtask subtask1, Subtask subtask2);

    void printEpic();

    void deleteEpic();

    void printEpicList();

    void clearEpicList();

    void printEpicSubtaskList();

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void printSubtask();

    void deleteSubtask();

    void printSubtaskList();

    void clearSubtaskList();

    void getHistory();
}