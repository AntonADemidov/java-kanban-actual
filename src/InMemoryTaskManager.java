import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    Scanner scanner = new Scanner(System.in);
    String empty = "\n";

    @Override
    public void createTask(Task task) {
        task.setId(++this.idCounter);
        tasks.put(task.getId(), task);
        System.out.printf("Задача с id # %d создана.%n", task.getId());
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.size() != 0) {
            System.out.print("Введите id задачи для обновления: ");
            Integer idForAction = Integer.parseInt(scanner.next());

            if (tasks.containsKey(idForAction) && tasks.get(idForAction) != null) {
                task.setId(idForAction);
                historyManager.remove(idForAction);
                tasks.remove(idForAction);
                tasks.put(task.getId(), task);
                System.out.printf("Задача с id # %d обновлена.%n", idForAction);
            } else {
                System.out.printf("Задача с id # %d не существует.%n", idForAction);
            }
        } else {
            System.out.println("Список задач пуст => нечего обновлять.");
        }
    }

    @Override
    public void printTask() {
        System.out.print("Введите id задачи для отображения: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (tasks.containsKey(idForAction)) {
            System.out.printf("%sЗАДАЧА С ID # %d:%n", empty, idForAction);
            System.out.println(tasks.get(idForAction));
            historyManager.add(tasks.get(idForAction));
        } else {
            System.out.printf("Задача с id # %d не существует.%n", idForAction);
        }
    }

    @Override
    public void deleteTask() {
        System.out.print("Введите id задачи для удаления: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (tasks.containsKey(idForAction)) {
            tasks.remove(idForAction);
            historyManager.remove(idForAction);
            System.out.printf("Задача с id # %d удалена.%n", idForAction);
        } else {
            System.out.printf("Задача с id # %d не существует.%n", idForAction);
        }
    }

    @Override
    public void printTaskList() {
        if (tasks.size() != 0) {
            System.out.printf("%sСПИСОК ЗАДАЧ:%n",empty);

            for (Task data : tasks.values()) {
                System.out.printf("%sЗАДАЧА С ID # %d:%n", empty, data.getId());
                System.out.println(data);
            }
        } else {
            System.out.println("Спиcок задач пуст.");
        }
    }

    @Override
    public void clearTaskList() {
        if (tasks.size() != 0) {
            for (Task task : tasks.values()) {
                historyManager.remove(task.getId());
            }
            tasks.clear();
            System.out.println("Спиcок задач удален.");
        } else {
            System.out.println("Удалять нечего - задачи не создавались.");
        }
    }

    @Override
    public void createEpic(Epic epic, Subtask subtask1, Subtask subtask2) {
        epic.setId(++this.idCounter);

        subtask1.setEpicId(epic.getId());
        subtask1.setId(++this.idCounter);
        subtasks.put(subtask1.getId(), subtask1);

        subtask2.setEpicId(epic.getId());
        subtask2.setId(++this.idCounter);
        subtasks.put(subtask2.getId(), subtask2);

        epic.getSubtaskIds().add(subtask1.getId());
        epic.getSubtaskIds().add(subtask2.getId());

        epic.setStatus(findEpicStatus(epic));
        epics.put(epic.getId(), epic);

        System.out.printf("Эпик c id # %d создан.%n", epic.getId());

        System.out.printf("Подзадача c id # %d создана.%n", subtask1.getId());
        System.out.printf("Подзадача c id # %d создана.%n", subtask2.getId());
    }

    @Override
    public void createEpic(Epic epic, Subtask subtask) {
        epic.setId(++this.idCounter);

        subtask.setEpicId(epic.getId());
        subtask.setId(++this.idCounter);
        subtasks.put(subtask.getId(), subtask);

        epic.getSubtaskIds().add(subtask.getId());

        epic.setStatus(findEpicStatus(epic));
        epics.put(epic.getId(), epic);

        System.out.printf("Эпик c id # %d создан.%n", epic.getId());
        System.out.printf("Подзадача c id # %d создана.%n", subtask.getId());
    }

    @Override
    public void updateEpic(Epic epic, Subtask subtask1, Subtask subtask2) {
        System.out.print("Введите id эпика для обновления: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (epics.containsKey(idForAction)) {
            for (Integer id : epics.get(idForAction).getSubtaskIds()) {
                historyManager.remove(id);
                subtasks.remove(id);
            }

            historyManager.remove(epics.get(idForAction).getId());
            epics.remove(idForAction);
            epic.setId(idForAction);

            subtask1.setEpicId(epic.getId());
            subtask1.setId(++this.idCounter);
            subtasks.put(subtask1.getId(), subtask1);

            subtask2.setEpicId(epic.getId());
            subtask2.setId(++this.idCounter);
            subtasks.put(subtask2.getId(), subtask2);

            epic.getSubtaskIds().add(subtask1.getId());
            epic.getSubtaskIds().add(subtask2.getId());

            epic.setStatus(findEpicStatus(epic));

            epics.put(epic.getId(), epic);

            System.out.printf("Эпик c id # %d обновлён.%n", idForAction);
            System.out.printf("Созданы новые подзадачи к эпику c id # %d.%n", idForAction);
        } else {
            System.out.printf("Эпик c id # %d не существует.%n", idForAction);
        }
    }

    @Override
    public void printEpic() {
        System.out.print("Введите id эпика для отображения: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (epics.containsKey(idForAction)) {
            System.out.printf("%sЭПИК С ID # %d:%n", empty, idForAction);
            System.out.println(epics.get(idForAction));
            historyManager.add(epics.get(idForAction));
        } else {
            System.out.printf("Эпик с id # %d не существует.%n", idForAction);
        }
    }

    @Override
    public void deleteEpic() {
        ArrayList<Integer> deletion = new ArrayList<>();
        System.out.print("Введите id эпика для удаления: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (epics.containsKey(idForAction)) {
            for (Integer id : epics.get(idForAction).getSubtaskIds()) {
                historyManager.remove(id);
                deletion.add(id);
            }

            for (Integer data : deletion) {
                System.out.printf("Подзадача с id # %d удалена (относится к эпику с id # %d).%n",
                        subtasks.get(data).getId(), idForAction);
                subtasks.remove(data);
            }

            historyManager.remove(epics.get(idForAction).getId());
            epics.remove(idForAction);
            System.out.printf("Эпик c id # %d удалён.%n", idForAction);
        } else {
            System.out.printf("Эпик c id # %d не существует.%n", idForAction);
        }
    }

    @Override
    public void printEpicList() {
        if (epics.size() != 0) {
            System.out.printf("%sСПИСОК ЭПИКОВ:%n", empty);
            for (Epic data : epics.values()) {
                System.out.printf("%sЭПИК С ID # %d:%n", empty, data.getId());
                System.out.println(data);
            }
        } else {
            System.out.println("Спиcок эпиков пуст.");
        }
    }

    @Override
    public void clearEpicList() {
        if (epics.size() != 0) {
            for (Epic epic : epics.values()) {
                historyManager.remove(epic.getId());
                for (Integer id : epic.getSubtaskIds()) {
                    historyManager.remove(id);
                }
            }
            epics.clear();
            subtasks.clear();
            System.out.println("Вcе эпики и соответствующие им подзадачи удалены.");
        } else {
            System.out.println("Удалять нечего - эпики не создавались.");
        }
    }

    @Override
    public void printEpicSubtaskList() {
        System.out.print("Введите id эпика: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if ((epics.get(idForAction) != null) && (epics.get(idForAction).getSubtaskIds()) != null) {
            System.out.printf("%sЭПИК С ID # %d. CПИСОК ПОДЗАДАЧ:%n", empty, idForAction);
            for (Integer data : epics.get(idForAction).getSubtaskIds()) {
                System.out.printf("%sПодзадача с id # %d:%n", empty, subtasks.get(data).getId());
                System.out.println(subtasks.get(data));
            }
        } else {
            System.out.println("Спиcок подзадач пуст.");
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        System.out.print("Введите id эпика для добавления новой подзадачи: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (epics.get(idForAction)!= null) {
            subtask.setEpicId(idForAction);
            subtask.setId(++this.idCounter);

            subtasks.put(subtask.getId(), subtask);
            epics.get(idForAction).getSubtaskIds().add(subtask.getId());
            epics.get(idForAction).setStatus(findEpicStatus(epics.get(idForAction)));
            System.out.printf("В эпик с id # %d добавлена подзадача с id # %d.%n", idForAction, subtask.getId());
        } else {
            System.out.printf("Эпик с id # %d не существует.%n", idForAction);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        System.out.print("Введите id эпика: ");
        Integer epicIdForAction = Integer.parseInt(scanner.next());
        System.out.print("Введите id задачи для обновления: ");
        Integer subtaskIdForAction = Integer.parseInt(scanner.next());

        if ((epics.get(epicIdForAction) != null) && (subtasks.get(subtaskIdForAction) != null)) {
            historyManager.remove(subtaskIdForAction);
            subtasks.remove(subtaskIdForAction);
            subtask.setEpicId(epicIdForAction);
            subtask.setId(subtaskIdForAction);
            subtasks.put(subtask.getId(), subtask);

            historyManager.remove(epicIdForAction);
            epics.get(epicIdForAction).setStatus(findEpicStatus(epics.get(epicIdForAction)));

            System.out.printf("Подзадача c id # %d обновлена.%n", subtaskIdForAction);
            System.out.printf("Эпик c id # %d обновлён.%n", epicIdForAction);
        } else {
            System.out.println("Указанные эпик или подзадача не существуют.");
        }
    }

    @Override
    public void printSubtask() {
        System.out.print("Введите id позадачи для отображения: ");
        Integer idForAction = Integer.parseInt(scanner.next());
        if (subtasks.containsKey(idForAction)) {
            System.out.printf("%sПОДЗАДАЧА С ID # %d:%n", empty, idForAction);
            System.out.println(subtasks.get(idForAction));
            historyManager.add(subtasks.get(idForAction));
        } else {
            System.out.printf("Задача с id # %d не существует.%n", idForAction);
        }
    }

    @Override
    public void deleteSubtask() {
        System.out.print("Введите id задачи для удаления: ");
        Integer idForAction = Integer.parseInt(scanner.next());

        if (subtasks.containsKey(idForAction)) {
            Integer key = subtasks.get(idForAction).getEpicId();
            epics.get(key).getSubtaskIds().remove(idForAction);
            historyManager.remove(subtasks.get(idForAction).getId());
            subtasks.remove(idForAction);
            epics.get(key).setStatus(findEpicStatus(epics.get(key)));
            System.out.printf("Задача с id # %d удалена.%n", idForAction);
        } else {
            System.out.printf("Задача с id # %d не существует.%n", idForAction);
        }
    }

    @Override
    public void printSubtaskList() {
        if (subtasks.size() != 0) {
            System.out.printf("%sОБЩИЙ СПИСОК ПОДЗАДАЧ:%n", empty);
            for (Subtask data : subtasks.values()) {
                System.out.printf("%sПОДЗАДАЧА С ID # %d:%n", empty, data.getId());
                System.out.println(data);
            }
        } else {
            System.out.println("Спиcок подзадач пуст.");
        }
    }

    @Override
    public void clearSubtaskList() { // проверка на пустоту
        if (subtasks.size() != 0) {
            for (Subtask data : subtasks.values()) {
                historyManager.remove(data.getId());
                epics.get(data.getEpicId()).getSubtaskIds().remove(data.getId());
            }
            subtasks.clear();

            for (Epic data : epics.values()) {
                data.setStatus(findEpicStatus(data));
            }
            System.out.println("Список подзадач удалён, соответствующие задачам эпики обновлены.");
        } else {
            System.out.println("Удалять нечего - подзадачи не создавались.");
        }
    }

    @Override
    public void getHistory() {
        List<Task> historyList = historyManager.getHistory();
        if (historyList.size() != 0) {
            int historyCounter = 0;
            System.out.println("История просмотров:");
            for (Task data : historyList) {
                ++historyCounter;
                String text = "id = ";
                System.out.printf("%-5d%-10s%s%5d%n", historyCounter, data.getType(), text, data.getId());
            }
        } else {
            System.out.println("История просмотров пуста.");
        }
    }

    private Status findEpicStatus(Epic epic) {
        Status status;
        int newStatusCounter = 0;
        int inProgressStatusCounter = 0;
        int doneStatusCounter = 0;

        for (Subtask data : subtasks.values()) {
            if (data.getEpicId() == epic.getId()) {
                status = data.getStatus();

                if (status.equals(Status.NEW)) {
                    newStatusCounter++;
                } else if (status.equals(Status.IN_PROGRESS)) {
                    inProgressStatusCounter++;
                } else {
                    doneStatusCounter++;
                }
            }
        }

        if ((inProgressStatusCounter > 0) || ((newStatusCounter > 0) && (doneStatusCounter > 0))) {
            status = Status.IN_PROGRESS;
        } else if ((newStatusCounter > 0) && (doneStatusCounter == 0)) {
            status = Status.NEW;
        } else {
            status = Status.DONE;
        }
        return status;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}