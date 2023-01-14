package managers;

import history.HistoryManager;
import tasks.*;
import utilities.Managers;
import utilities.Status;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    String empty = "\n";
    private int idCounter = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedList = new TreeSet<>((o1, o2) -> {
        LocalDateTime time1 = o1.getStartTime();
        LocalDateTime time2 = o2.getStartTime();
        int result;

        if ((time1 == null) && (time2 == null)) {
            result = o1.getId().compareTo(o2.getId());
        } else if (time1 == null) {
            result = 1;
        } else if (time2 == null) {
            result = -1;
        } else if (time1.equals(time2)) {
            result = o1.getId().compareTo(o2.getId());
        } else {
            if (time1.isAfter(time2)) {
                result = 1;
            } else {
                result = -1;
            }
        }
        return result;
    });

    @Override
    public boolean createTask(Task task) throws URISyntaxException, IOException, InterruptedException {
        if (createOrNot(task, prioritizedList)) {
            task.setId(++this.idCounter);
            tasks.put(task.getId(), task);
            prioritizedList.add(task);
            System.out.printf("%sЗадача с id # %d создана.%n", empty, task.getId());
            return true;
        } else {
            System.out.println("Задача не создана - пересечение по времени c одной из задач/подзадач в списке.");
            return false;
        }
    }

    @Override
    public boolean updateTask(Task task, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        HashSet<Task> localPrioritizedList = new HashSet<>(prioritizedList);
        localPrioritizedList.remove(tasks.get(idForAction));

        if (createOrNot(task, localPrioritizedList)) {
            task.setId(idForAction);
            Task deletedTask = tasks.remove(idForAction);
            historyManager.remove(idForAction);
            tasks.put(task.getId(), task);
            prioritizedList.remove(deletedTask);
            prioritizedList.add(task);
            System.out.printf("Задача с id # %d обновлена.%n", idForAction);
            return true;
        } else {
            System.out.println("Задача не обновлена - пересечение по времени c одной из задач/подзадач в списке.");
            return false;
        }
    }

    @Override
    public void printTask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        System.out.printf("%sЗАДАЧА С ID # %d:%n", empty, idForAction);
        System.out.println(tasks.get(idForAction));
        historyManager.add(tasks.get(idForAction));
    }

    @Override
    public void deleteTask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        Task deletedTask = tasks.remove(idForAction);
        historyManager.remove(idForAction);
        prioritizedList.remove(deletedTask);
        System.out.printf("Задача с id # %d удалена.%n", idForAction);
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
            System.out.println("Список задач пуст.");
        }
    }

    @Override
    public void clearTaskList() throws URISyntaxException, IOException, InterruptedException {
        if (tasks.size() != 0) {
            for (Task task : tasks.values()) {
                historyManager.remove(task.getId());
                prioritizedList.remove(task);
            }
            tasks.clear();
            System.out.println("Список задач удален.");
        } else {
            System.out.println("Удалять нечего - задачи не создавались.");
        }
    }

    @Override
    public boolean createEpic(Epic epic, Subtask subtask1, Subtask subtask2) throws URISyntaxException, IOException, InterruptedException {
        List<Integer> subtaskIds = new ArrayList<>();
        epic.setId(++this.idCounter);

        if (createOrNot(subtask1, prioritizedList)) {
            addSubtask(subtask1, epic.getId());
            subtaskIds.add(subtask1.getId());
        }

        if (createOrNot(subtask2, prioritizedList)) {
            addSubtask(subtask2, epic.getId());
            subtaskIds.add(subtask2.getId());
        }

        if (subtaskIds.size() != 0) {
            createEpic(epic, subtaskIds);
            System.out.printf("Эпик c id # %d создан.%n", epic.getId());

            for (Integer id : subtaskIds) {
                System.out.printf("Подзадача c id # %d создана в эпике с id # %d.%n", id, epic.getId());
            }
            return true;
        } else {
            System.out.println("Эпик не создан - пересечение связанных подзадач по времени с другими задачами" +
                    " или подзадачами.");
            --this.idCounter;
            return false;
        }
    }

    @Override
    public boolean createEpic(Epic epic, Subtask subtask) throws URISyntaxException, IOException, InterruptedException {
        List<Integer> subtaskIds = new ArrayList<>();
        epic.setId(++this.idCounter);

        if (createOrNot(subtask, prioritizedList)) {
            addSubtask(subtask, epic.getId());
            subtaskIds.add(subtask.getId());
        }

        if (subtaskIds.size() != 0) {
            createEpic(epic, subtaskIds);
            System.out.printf("Эпик c id # %d создан.%n", epic.getId());

            for (Integer id : subtaskIds) {
                System.out.printf("Подзадача c id # %d создана в эпике с id # %d.%n", id, epic.getId());
            }
            return true;
        } else {
            System.out.println("Эпик не создан - пересечение связанных подзадач по времени с другими задачами" +
                    " или подзадачами.");
            --this.idCounter;
            return false;
        }
    }

    @Override
    public boolean updateEpic(Epic epic, Subtask subtask1, Subtask subtask2, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        List<Integer> subtaskIds = epics.get(idForAction).getSubtaskIds();
        int updateCounter = 0;
        Set<Task> localPrioritizedList = new HashSet<>(prioritizedList);
        List<Integer> localSubtaskIds = new ArrayList<>(epics.get(idForAction).getSubtaskIds());

        epic.setId(idForAction);
        Subtask currentDeletion;
        if (localSubtaskIds.size() != 0) {
            for (Integer id : localSubtaskIds) {
                currentDeletion = subtasks.get(id);
                localPrioritizedList.remove(currentDeletion);

                if (createOrNot(subtask1, localPrioritizedList)) {
                    updateEpic(id, idForAction, subtask1, epic, localPrioritizedList);
                    updateCounter++;
                } else {
                    localPrioritizedList.add(currentDeletion);
                }
            }
        }

        localSubtaskIds = new ArrayList<>(epics.get(idForAction).getSubtaskIds());
        if (localSubtaskIds.size() != 0) {
            for (Integer id : localSubtaskIds) {
                currentDeletion = subtasks.get(id);
                localPrioritizedList.remove(currentDeletion);

                if (createOrNot(subtask2, localPrioritizedList)) {
                    updateEpic(id, idForAction, subtask2, epic, localPrioritizedList);
                    updateCounter++;
                } else {
                    localPrioritizedList.add(currentDeletion);
                }
            }
        }


        if (updateCounter != 0) {
            historyManager.remove(epics.get(idForAction).getId());
            epics.remove(idForAction);
            createEpic(epic, subtaskIds);
            System.out.printf("Эпик c id # %d обновлен - новый список подзадач:%n", epic.getId());

            for (Integer id : subtaskIds) {
                System.out.printf("Подзадача c id # %d.%n", id);
            }
            return true;
        } else {
            System.out.println("Эпик не обновлен - пересечение связанных подзадач по времени с другими задачами" +
                    " или подзадачами.");
            return false;
        }
    }

    @Override
    public void printEpic(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        System.out.printf("%sЭПИК С ID # %d:%n", empty, idForAction);
        System.out.println(epics.get(idForAction));
        historyManager.add(epics.get(idForAction));
    }

    @Override
    public void deleteEpic(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        ArrayList<Integer> deletedList = new ArrayList<>();

        for (Integer id : epics.get(idForAction).getSubtaskIds()) {
            historyManager.remove(id);
            deletedList.add(id);
        }

        for (Integer data : deletedList) {
            System.out.printf("Подзадача с id # %d удалена (относится к эпику с id # %d).%n",
                    subtasks.get(data).getId(), idForAction);
            Subtask deletedSubtask = subtasks.remove(data);
            prioritizedList.remove(deletedSubtask);
        }

        historyManager.remove(epics.get(idForAction).getId());
        epics.remove(idForAction);
        System.out.printf("Эпик c id # %d удалён.%n", idForAction);
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
            System.out.println("Список эпиков пуст.");
        }
    }

    @Override
    public void clearEpicList() throws URISyntaxException, IOException, InterruptedException {
        if (epics.size() != 0) {
            for (Epic epic : epics.values()) {
                historyManager.remove(epic.getId());
                for (Integer id : epic.getSubtaskIds()) {
                    historyManager.remove(id);
                    Subtask deletedSubtask = subtasks.remove(id);
                    prioritizedList.remove(deletedSubtask);
                }
            }
            epics.clear();
            System.out.println("Вcе эпики и соответствующие им подзадачи удалены.");
        } else {
            System.out.println("Удалять нечего - эпики не создавались.");
        }
    }

    @Override
    public void printEpicSubtaskList(Integer idForAction) {
        System.out.printf("%sЭПИК С ID # %d. СПИСОК ПОДЗАДАЧ:%n", empty, idForAction);

        for (Integer data : epics.get(idForAction).getSubtaskIds()) {
            System.out.printf("%sПодзадача с id # %d:%n", empty, subtasks.get(data).getId());
            System.out.println(subtasks.get(data));
        }
    }

    @Override
    public boolean createSubtask(Subtask subtask, Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        if (createOrNot(subtask, prioritizedList)) {
            addSubtask(subtask, idForAction);
            epics.get(idForAction).getSubtaskIds().add(subtask.getId());
            addTimeFieldsToEpic(epics.get(idForAction));
            epics.get(idForAction).setStatus(findEpicStatus(epics.get(idForAction)));
            System.out.printf("Эпик c id # %d обновлен.%n", epics.get(idForAction).getId());
            System.out.printf("Подзадача c id # %d создана в эпике с id # %d.%n", subtask.getId(),
                        epics.get(idForAction).getId());
            return true;
        } else {
            System.out.println("Подзадача не создана - пересечение по времени c одной из задач или подзадач в списке.");
            return false;
        }
    }

    @Override
    public boolean updateSubtask(Subtask subtask, Integer epicIdForAction, Integer subtaskIdForAction) throws URISyntaxException, IOException, InterruptedException {
        Subtask deletedSubtask = subtasks.remove(subtaskIdForAction);
        prioritizedList.remove(deletedSubtask);

        if (createOrNot(subtask, prioritizedList)) {
            historyManager.remove(subtaskIdForAction);

            subtask.setEpicId(epicIdForAction);
            subtask.setId(subtaskIdForAction);
            subtasks.put(subtask.getId(), subtask);
            prioritizedList.add(subtask);

            historyManager.remove(epicIdForAction);
            addTimeFieldsToEpic(epics.get(epicIdForAction));
            epics.get(epicIdForAction).setStatus(findEpicStatus(epics.get(epicIdForAction)));

            System.out.printf("Подзадача c id # %d обновлена.%n", subtaskIdForAction);
            System.out.printf("Эпик c id # %d обновлён.%n", epicIdForAction);
            return true;
        } else {
            System.out.printf("Эпик c id # %d не обновлен.%n", epicIdForAction);
            System.out.printf("Подзадача c id # %d не обновлена " +
                    "- пересечение по времени другой задачей или подзадачей.%n", subtaskIdForAction);

            subtasks.put(deletedSubtask.getId(), deletedSubtask);
            prioritizedList.add(deletedSubtask);
            return false;
        }
    }

    @Override
    public void printSubtask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        System.out.printf("%sПОДЗАДАЧА С ID # %d:%n", empty, idForAction);
        System.out.println(subtasks.get(idForAction));
        historyManager.add(subtasks.get(idForAction));
    }

    @Override
    public void deleteSubtask(Integer idForAction) throws URISyntaxException, IOException, InterruptedException {
        Integer key = subtasks.get(idForAction).getEpicId();
        epics.get(key).getSubtaskIds().remove(idForAction);
        historyManager.remove(subtasks.get(idForAction).getId());
        Subtask deletedSubtask = subtasks.remove(idForAction);
        prioritizedList.remove(deletedSubtask);
        addTimeFieldsToEpic(epics.get(key));
        epics.get(key).setStatus(findEpicStatus(epics.get(key)));
        System.out.printf("Задача с id # %d удалена.%n", idForAction);
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
            System.out.println("Список подзадач пуст.");
        }
    }

    @Override
    public void clearSubtaskList() throws URISyntaxException, IOException, InterruptedException {
        if (subtasks.size() != 0) {
            for (Subtask data : subtasks.values()) {
                historyManager.remove(data.getId());
                prioritizedList.remove(data);
                epics.get(data.getEpicId()).getSubtaskIds().remove(data.getId());
            }
            subtasks.clear();

            for (Epic data : epics.values()) {
                addTimeFieldsToEpic(data);
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
            System.out.println(empty);
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

    @Override
    public void getPrioritizedTasks() {
        if (prioritizedList.size() != 0) {
            System.out.println(empty);
            System.out.println("Задачи и подзадачи в порядке приоритета:");
            int taskCounter = 0;
            for (Task task : prioritizedList) {
                ++taskCounter;
                String text = "id =  ";
                System.out.printf("%-5d%-10s%s%2d%20s%n", taskCounter, task.getType(), text, task.getId(), task.getStartTime());
            }
        } else {
            System.out.println("Задачи и подзадачи не создавались.");
        }
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Set<Task> getPrioritizedList() {
        return prioritizedList;
    }

    public Status findEpicStatus(Epic epic) {
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
        } else if (((newStatusCounter > 0) && (doneStatusCounter == 0))
                || ((newStatusCounter == 0) && (inProgressStatusCounter == 0) && (doneStatusCounter == 0))) {
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

    private boolean createOrNot (Task task, Set<Task> prioritizedList) {
        boolean isNoTimeIntersections = true;

        if (prioritizedList.size() == 0) {
            isNoTimeIntersections = true;
        } else {
            for (Task element : prioritizedList) {
                LocalDateTime elementStartTime = element.getStartTime();
                LocalDateTime elementEndTime = element.getEndTime();
                LocalDateTime newTaskStartTime = task.getStartTime();
                LocalDateTime newTaskEndTime = task.getEndTime();

                boolean rule1;
                if ((newTaskStartTime != null) && (elementEndTime != null)) {
                    rule1 = newTaskStartTime.isAfter(elementEndTime);
                } else {
                    if ((newTaskStartTime == null)) {
                        rule1 = true;
                    } else {
                        rule1 = false;
                    }
                }

                boolean rule2;
                if ((newTaskEndTime != null) && (elementStartTime != null)) {
                    rule2 = newTaskEndTime.isBefore(elementStartTime);
                } else {
                    if ((elementStartTime == null)) {
                        rule2 = true;
                    } else {
                        rule2 = false;
                    }
                }

                if (rule1 || rule2) {
                    boolean currentStatus = true;
                    if (isNoTimeIntersections && currentStatus) {
                        isNoTimeIntersections = true;
                    } else {
                        isNoTimeIntersections = false;
                    }
                } else {
                    isNoTimeIntersections = false;
                }
            }
        }
        return isNoTimeIntersections;
    }

    private void addSubtask(Subtask subtask, Integer idForAction) {
        subtask.setEpicId(idForAction);
        subtask.setId(++this.idCounter);
        subtasks.put(subtask.getId(), subtask);
        prioritizedList.add(subtask);
    }

    private void createEpic(Epic epic, List<Integer> subtaskIds) {
        epic.setSubtaskIds(subtaskIds);
        addTimeFieldsToEpic(epic);
        epic.setStatus(findEpicStatus(epic));
        epics.put(epic.getId(), epic);
    }

    private void addTimeFieldsToEpic (Epic epic) {
        LocalDateTime epicStartTime = null;
        Duration epicDuration = null;
        LocalDateTime epicEndTime = null;
        for (int i = 0; i <epic.getSubtaskIds().size(); i++) {
            Integer id = epic.getSubtaskIds().get(i);

            LocalDateTime currentStartTime = subtasks.get(id).getStartTime();
            if (currentStartTime != null) {
                if (epicStartTime == null) {
                    epicStartTime = currentStartTime;
                }
                if (currentStartTime.isBefore(epicStartTime)) {
                    epicStartTime = currentStartTime;
                }
            }

            Duration currentDuration = subtasks.get(id).getDuration();
            if (currentDuration != null) {
                if (epicDuration == null) {
                    epicDuration = currentDuration;
                } else {
                    epicDuration = epicDuration.plus(currentDuration);
                }
            }

            LocalDateTime currentEndTime = subtasks.get(id).getEndTime();
            if (currentEndTime != null) {
                if (epicEndTime == null) {
                    epicEndTime = currentEndTime;
                }
                if (currentEndTime.isAfter(epicEndTime)) {
                    epicEndTime = currentEndTime;
                }
            }
        }
        epic.setStartTime(epicStartTime);
        epic.setDuration(epicDuration);
        epic.setEndTime(epicEndTime);
    }

    private void updateEpic (Integer id, Integer idForAction, Subtask subtask, Epic epic, Set<Task> localPrioritizedList) {
        historyManager.remove(id);
        Subtask deletedSubtask = subtasks.remove(id);

        if (deletedSubtask != null) {
            prioritizedList.remove(deletedSubtask);
            epics.get(idForAction).getSubtaskIds().remove(id);
        }
        addSubtask(subtask, epic.getId());
        localPrioritizedList.add(subtask);
        epics.get(idForAction).getSubtaskIds().add(subtask.getId());
    }
}