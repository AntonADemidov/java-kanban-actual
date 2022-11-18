import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFIle(File file) throws IOException, ManagerSaveException {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        String csv = Files.readString(Path.of(String.valueOf(file)));
        String[] lines = csv.split("\n");
        List<Integer> history = Collections.emptyList();
        int idCounter = 0;

        for (int i = 1; i < (lines.length - 1); i++) {
            String line = lines[i];

            if (line.isEmpty()) {
                history = TaskManagerCSVFormatter.createHistoryFromString(lines[i + 1]);
            } else {
                Task task = TaskManagerCSVFormatter.fromString(line);
                int id = task.getId();

                if (id > idCounter) {
                    idCounter = id;
                }

                if (task.getType().equals(Types.TASK)) {
                    tasksManager.loadTask(task);
                } else if (task.getType().equals(Types.EPIC)) {
                    tasksManager.loadEpic((Epic) task);
                } else {
                    tasksManager.loadSubtask((Subtask) task);
                }
            }
        }

        for (Map.Entry<Integer, Subtask> entry : tasksManager.getSubtasks().entrySet()) {
            Subtask subtask = entry.getValue();
            Epic epic = tasksManager.getEpics().get(subtask.getEpicId());
            epic.getSubtaskIds().add(subtask.getId());
        }

        for (Integer taskId : history) {
            tasksManager.getHistoryManager().add(tasksManager.findTask(taskId, tasksManager));
            tasksManager.save();
        }

        tasksManager.setIdCounter(idCounter);
        System.out.println("СТАТУС ТРЕКЕРА ВОССТАНОВЛЕН ИЗ ФАЙЛА.");
        return tasksManager;
    }

    private Task findTask (Integer id, FileBackedTasksManager tasksManager) {
        if (tasksManager.getTasks().get(id) != null) {
            return tasksManager.getTasks().get(id);
        } else if (tasksManager.getSubtasks().get(id) != null) {
            return tasksManager.getSubtasks().get(id);
        } else {
            return tasksManager.getEpics().get(id);
        }
    }

    private void save() {
        try {
            if (file == null) {
                throw new ManagerSaveException("Объект класса File для сохранения информации не инициализирован");
            } else {
                try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
                    br.write(TaskManagerCSVFormatter.getHeader() + empty);

                    for (Task task : super.getTasks().values()) {
                        br.write(TaskManagerCSVFormatter.toString(task) + empty);
                    }

                    for (Epic epic : super.getEpics().values()) {
                        br.write(TaskManagerCSVFormatter.toString(epic) + empty);
                    }

                    for (Subtask subtask : super.getSubtasks().values()) {
                        br.write(TaskManagerCSVFormatter.toString(subtask) + empty);
                    }
                    br.write("" + empty);
                    br.write(TaskManagerCSVFormatter.toString(super.getHistoryManager()));
                } catch (IOException e) {
                    System.out.println("Произошла неизвестная проверяемая ошибка ввода-вывода класса IOException");
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    System.out.println("Произошла неизвестная непроверяемая ошибка класса RuntimeException");
                    e.printStackTrace();
                }
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadTask(Task task) {
        super.getTasks().put(task.getId(), task);
        save();
    }

    private void loadEpic(Epic epic) {
        super.getEpics().put(epic.getId(), epic);
        save();
    }

    private void loadSubtask(Subtask subtask) {
        super.getSubtasks().put(subtask.getId(), subtask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void printTask() {
        super.printTask();
        save();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void printTaskList() {
        super.printTaskList();
    }

    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    @Override
    public void createEpic(Epic epic, Subtask subtask1, Subtask subtask2) {
        super.createEpic(epic, subtask1, subtask2);
        save();
    }

    @Override
    public void createEpic(Epic epic, Subtask subtask) {
        super.createEpic(epic, subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic, Subtask subtask1, Subtask subtask2) {
        super.updateEpic(epic, subtask1, subtask2);
        save();
    }

    @Override
    public void printEpic() {
        super.printEpic();
        save();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void printEpicList() {
        super.printEpicList();
    }

    @Override
    public void clearEpicList() {
        super.clearEpicList();
        save();
    }

    @Override
    public void printEpicSubtaskList() {
        super.printEpicSubtaskList();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void printSubtask() {
        super.printSubtask();
        save();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
        save();
    }

    @Override
    public void printSubtaskList() {
        super.printSubtaskList();
    }

    @Override
    public void clearSubtaskList() {
        super.clearSubtaskList();
        save();
    }

    @Override
    public void getHistory() {
        super.getHistory();
    }
}