package managers;

import clients.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.*;
import utilities.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    Gson gson = Managers.getGson();

    public HttpTaskManager(URI uri) throws URISyntaxException, IOException, InterruptedException {
        kvTaskClient = new KVTaskClient(uri);
    }

    private void save() throws URISyntaxException, IOException, InterruptedException {
        kvTaskClient.put("tasks", gson.toJson(getTasks()));
        kvTaskClient.put("subtasks", gson.toJson(getSubtasks()));
        kvTaskClient.put("epics", gson.toJson(getEpics()));
        kvTaskClient.put("historyList", gson.toJson(getHistoryManager().getHistory()));
        kvTaskClient.put("prioritizedList", gson.toJson(getPrioritizedList()));
    }

    public void load() throws URISyntaxException, IOException, InterruptedException {
        Type type;
        String json = kvTaskClient.load("tasks");

        if (json.equals("NO DATA")) {
            System.out.println("Задачи типа TASK не сохранялись на KVServer");
        } else {
            type = new TypeToken<HashMap<Integer, Task>>() {}.getType();
            HashMap<Integer, Task> tasks = gson.fromJson(json, type);

            for (Task task : tasks.values()) {
                this.getTasks().put(task.getId(), task);
                this.getPrioritizedList().add(task);
            }
            System.out.println("Задачи типа TASK восстановлены с KVServer");
        }

        json = kvTaskClient.load("epics");
        if (json.equals("NO DATA")) {
            System.out.println("Задачи типа EPIC не сохранялись на KVServer");
        } else {
            type = new TypeToken<HashMap<Integer, Epic>>() {}.getType();
            HashMap<Integer, Epic> epics = gson.fromJson(json, type);

            for (Epic epic : epics.values()) {
                this.getEpics().put(epic.getId(), epic);
            }
            System.out.println("Задачи типа EPIC восстановлены с KVServer");
        }

        json = kvTaskClient.load("subtasks");
        if (json.equals("NO DATA")) {
            System.out.println("Задачи типа SUBTASK не сохранялись на KVServer");
        } else {
            type = new TypeToken<HashMap<Integer, Subtask>>() {}.getType();
            HashMap<Integer, Subtask> subtasks = gson.fromJson(json, type);

            for (Subtask subtask : subtasks.values()) {
                this.getSubtasks().put(subtask.getId(), subtask);
                this.getPrioritizedList().add(subtask);
            }
            System.out.println("Задачи типа SUBTASK восстановлены с KVServer");
        }

        json = kvTaskClient.load("historyList");

        if (json.equals("NO DATA")) {
            System.out.println("История просмотров не сохранялась на KVServer");
        } else {
            type = new TypeToken<List<Task>>() {}.getType();
            List<Task> historyList = gson.fromJson(json, type);

            for (Task task : historyList) {
                this.getHistoryManager().add(task);
            }
            System.out.println("История просмотров восстановлена с KVServer");
        }
    }

    @Override
    public boolean createTask(Task task) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.createTask(task);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public boolean updateTask(Task task, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.updateTask(task, idForAction);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public void printTask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.printTask(idForAction);
        save();
    }

    @Override
    public void deleteTask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.deleteTask(idForAction);
        save();
    }

    @Override
    public void printTaskList() {
        super.printTaskList();
    }

    @Override
    public void clearTaskList() throws URISyntaxException, IOException, InterruptedException {
        super.clearTaskList();
        save();
    }

    @Override
    public boolean createEpic(Epic epic, Subtask subtask1, Subtask subtask2) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.createEpic(epic, subtask1, subtask2);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public boolean createEpic(Epic epic, Subtask subtask) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.createEpic(epic, subtask);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public boolean updateEpic(Epic epic, Subtask subtask1, Subtask subtask2, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.updateEpic(epic, subtask1, subtask2, idForAction);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public void printEpic(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.printEpic(idForAction);
        save();
    }

    @Override
    public void deleteEpic(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.deleteEpic(idForAction);
        save();
    }

    @Override
    public void printEpicList() {
        super.printEpicList();
    }

    @Override
    public void clearEpicList() throws URISyntaxException, IOException, InterruptedException {
        super.clearEpicList();
        save();
    }

    @Override
    public void printEpicSubtaskList(Integer idForAction) {
        super.printEpicSubtaskList(idForAction);
    }

    @Override
    public boolean createSubtask(Subtask subtask, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.createSubtask(subtask, idForAction);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public boolean updateSubtask(Subtask subtask, Integer epicIdForAction, Integer subtaskIdForAction) throws URISyntaxException, IOException, InterruptedException {
        boolean successCreation = super.updateSubtask(subtask, epicIdForAction, subtaskIdForAction);
        if (successCreation) {
            save();
        }
        return successCreation;
    }

    @Override
    public void printSubtask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.printSubtask(idForAction);
        save();
    }

    @Override
    public void deleteSubtask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        super.deleteSubtask(idForAction);
        save();
    }

    @Override
    public void printSubtaskList() {
        super.printSubtaskList();
    }

    @Override
    public void clearSubtaskList() throws URISyntaxException, IOException, InterruptedException {
        super.clearSubtaskList();
        save();
    }

    @Override
    public void getHistory() {
        super.getHistory();
    }

    @Override
    public void getPrioritizedTasks() {
        super.getPrioritizedTasks();
    }
}
