package managers;

import history.HistoryManager;
import tasks.*;
import org.junit.jupiter.api.Test;
import utilities.Status;
import utilities.Types;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    public T manager;

    @Test
    void createTaskTest() throws URISyntaxException, IOException, InterruptedException {
        assertEquals(0, manager.getTasks().size(), "Список задач не создавался");

        Task task = new Task(null, Types.TASK,"Покупки", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 29, 10,00),
                Duration.ofMinutes(45),"молоко; кофе; яйца");

        manager.createTask(task);
        final int taskId = task.getId();
        assertEquals(1, taskId, "значения id не совпадают.");

        final Task savedTask = manager.getTasks().get(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(1, savedTask.getId(), "Неверный порядок задач.");

        final HashMap<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(taskId), "Задачи не совпадают.");

        Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Задачи не возвращаются.");
        assertEquals(1, prioritizedList.size(), "Неверное количество задач.");
        assertTrue(prioritizedList.contains(task), "Задачи не совпадают.");
    }

    @Test
    void updateTaskTest() throws URISyntaxException, IOException, InterruptedException {
        createTaskTest();

        Task newTask = new Task(null, Types.TASK,"Покупки", Status.DONE,
                LocalDateTime.of(2022, Month.NOVEMBER, 29, 10,00),
                Duration.ofMinutes(60),"молоко; кофе; яйца; хлеб");

        final int idForAction = 1;
        assertEquals(1, idForAction, "Значения id не совпадают.");

        manager.updateTask(newTask, idForAction);
        final int taskId = newTask.getId();

        assertEquals(1, taskId, "Значения id не совпадают.");

        final Task savedTask = manager.getTasks().get(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(newTask, savedTask, "Задачи не совпадают.");
        assertEquals(1, savedTask.getId(), "Неверный порядок задач.");

        final HashMap<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(newTask, tasks.get(taskId), "Задачи не совпадают.");

        Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Задачи не возвращаются.");
        assertEquals(1, prioritizedList.size(), "Неверное количество задач.");
        assertTrue(prioritizedList.contains(newTask), "Задачи не совпадают.");
    }

    @Test
    void printTaskTest() throws URISyntaxException, IOException, InterruptedException {
        createTaskTest();

        final int idForAction = 1;
        assertEquals(1, idForAction, "значения id не совпадают.");

        manager.printTask(idForAction);
    }

    @Test
    void deleteTaskTest() throws URISyntaxException, IOException, InterruptedException {
        createTaskTest();

        final int idForAction = 1;
        assertEquals(1, idForAction, "значения id не совпадают.");

        manager.deleteTask(idForAction);
        assertEquals(0, manager.getTasks().size(), "задача не удалена");
        assertEquals(0, manager.getPrioritizedList().size(), "задача не удалена");
        assertEquals(0, manager.getHistoryManager().getNodeMap().size(), "задача не удалена");
    }

    @Test
    void printTaskListTest() throws URISyntaxException, IOException, InterruptedException {
        createTaskTest();
        manager.printTaskList();
    }

    @Test
    void clearTaskListTest() throws URISyntaxException, IOException, InterruptedException {
        createTaskTest();

        manager.clearTaskList();
        assertEquals(0, manager.getTasks().size(), "задача не удалена");
        assertEquals(0, manager.getPrioritizedList().size(), "задача не удалена");
        assertEquals(0, manager.getHistoryManager().getNodeMap().size(), "задача не удалена");
    }

    @Test
    void createEpicTest1() throws URISyntaxException, IOException, InterruptedException {
        assertEquals(0, manager.getEpics().size(), "Список эпиков не создавался");
        assertEquals(0, manager.getSubtasks().size(), "Список подзадач не создавался");

        Epic epic = new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                null,null, null,"спринт #1; спринт #2", null);

        Subtask subtask1 = new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий");

        Subtask subtask2 = new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию");

        manager.createEpic(epic, subtask1, subtask2);
        final int epicId = epic.getId();
        assertEquals(1, epicId, "значения id не совпадают.");

        final Epic savedEpic = manager.getEpics().get(epicId);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(Status.DONE, epic.getStatus(),"Статус рассчитан неверно.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(epicId), "Эпики не совпадают.");

        final List<Integer> subtaskIds = epic.getSubtaskIds();
        assertEquals(2, subtaskIds.size());
        assertEquals(2, subtaskIds.get(0));
        assertEquals(3, subtaskIds.get(1));

        final int subtaskId1 = subtask1.getId();
        assertEquals(2, subtaskId1,"Значения id не совпадают.");
        assertEquals(1, subtask1.getEpicId(),"Значения epicId не совпадают.");

        final Subtask savedSubtask1 = manager.getSubtasks().get(subtaskId1);
        assertNotNull(savedSubtask1, "Подзачада не найдена.");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают.");

        final int subtaskId2 = subtask2.getId();
        assertEquals(3, subtaskId2,"значения id не совпадают.");
        assertEquals(1, subtask1.getEpicId(),"Значения epicId не совпадают.");

        final Subtask savedSubtask2 = manager.getSubtasks().get(subtaskId2);
        assertNotNull(savedSubtask2, "Подзачада не найдена.");
        assertEquals(subtask2, savedSubtask2, "Подзадачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(subtaskId1), "Подзадачи не совпадают.");
        assertEquals(subtask2, subtasks.get(subtaskId2), "Подзадачи не совпадают.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Подзадачи не возвращаются.");
        assertEquals(2, prioritizedList.size(), "Неверное количество подзадач.");
        assertTrue(prioritizedList.contains(subtask1), "Подзадачи не совпадают.");
        assertTrue(prioritizedList.contains(subtask2), "Подзадачи не совпадают.");
    }

    @Test
    void createEpicTest2() throws URISyntaxException, IOException, InterruptedException {
        assertEquals(0, manager.getEpics().size(), "Список эпиков не создавался");
        assertEquals(0, manager.getSubtasks().size(), "Список подзадач не создавался");

        Epic epic = new Epic(null,Types.EPIC,"Java developer. Модуль #1",null,
                        null,null,null,"спринт #3", null);

        Subtask subtask = new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.IN_PROGRESS,
                        LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать трекер задач");

        manager.createEpic(epic, subtask);
        final int epicId = epic.getId();
        assertEquals(1, epicId, "значения id не совпадают.");

        final Epic savedEpic = manager.getEpics().get(epicId);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(),"Статус рассчитан неверно.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(epicId), "Эпики не совпадают.");

        final List<Integer> subtaskIds = epic.getSubtaskIds();
        assertEquals(1, subtaskIds.size());
        assertEquals(2, subtaskIds.get(0));

        final int subtaskId = subtask.getId();
        assertEquals(2, subtaskId,"значения id не совпадают.");
        assertEquals(1, subtask.getEpicId(),"Значения epicId не совпадают.");

        final Subtask savedSubtask = manager.getSubtasks().get(subtaskId);
        assertNotNull(savedSubtask, "Подзачада не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(subtaskId), "Подзадачи не совпадают.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Подзадачи не возвращаются.");
        assertEquals(1, prioritizedList.size(), "Неверное количество подзадач.");
        assertTrue(prioritizedList.contains(subtask), "Подзадачи не совпадают.");
    }

    @Test
    void updateEpicTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest1();

        Subtask subtask1 = new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.DONE,
                LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                Duration.ofMinutes(7200),"пройти теорию; создать трекер задач");

        Subtask subtask2 = new Subtask(null, null, Types.SUBTASK,"Спринт #4", Status.NEW,
                LocalDateTime.of(2022, Month.DECEMBER, 19, 10, 00),
                Duration.ofMinutes(7200),"пройти теорию; доработать трекер задач");

        final int idForAction = 1;
        assertEquals(1, idForAction, "Значения id не совпадают.");

        Epic epic = manager.getEpics().get(idForAction);
        manager.updateEpic(epic, subtask1, subtask2, idForAction);
        final int epicId = epic.getId();
        assertEquals(1, epicId, "значения id не совпадают.");

        final Epic savedEpic = manager.getEpics().get(epicId);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(),"Статус рассчитан неверно.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(epicId), "Эпики не совпадают.");

        final List<Integer> subtaskIds = epic.getSubtaskIds();
        assertEquals(2, subtaskIds.size());
        assertEquals(4, subtaskIds.get(0));
        assertEquals(5, subtaskIds.get(1));

        final int subtaskId1 = subtask1.getId();
        assertEquals(4, subtaskId1,"Значения id не совпадают.");
        assertEquals(1, subtask1.getEpicId(),"Значения epicId не совпадают.");

        final Subtask savedSubtask1 = manager.getSubtasks().get(subtaskId1);
        assertNotNull(savedSubtask1, "Подзачада не найдена.");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают.");

        final int subtaskId2 = subtask2.getId();
        assertEquals(5, subtaskId2,"значения id не совпадают.");
        assertEquals(1, subtask1.getEpicId(),"Значения epicId не совпадают.");

        final Subtask savedSubtask2 = manager.getSubtasks().get(subtaskId2);
        assertNotNull(savedSubtask2, "Подзачада не найдена.");
        assertEquals(subtask2, savedSubtask2, "Подзадачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(subtaskId1), "Подзадачи не совпадают.");
        assertEquals(subtask2, subtasks.get(subtaskId2), "Подзадачи не совпадают.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Подзадачи не возвращаются.");
        assertEquals(2, prioritizedList.size(), "Неверное количество подзадач.");
        assertTrue(prioritizedList.contains(subtask1), "Подзадачи не совпадают.");
        assertTrue(prioritizedList.contains(subtask2), "Подзадачи не совпадают.");
    }

    @Test
    void printEpicTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();

        final int idForAction = 1;
        assertEquals(1, idForAction, "значения id не совпадают.");

        manager.printEpic(idForAction);
    }

    @Test
    void deleteEpicTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();

        final int idForAction = 1;
        assertEquals(1, idForAction, "Значения id не совпадают.");

        manager.deleteEpic(idForAction);
        assertEquals(0, manager.getHistoryManager().getNodeMap().size(), "Подзадача эпика не удалена");
        assertEquals(0, manager.getSubtasks().size(), "Подзадача эпика не удалена");
        assertEquals(0, manager.getPrioritizedList().size(), "Подзадача эпика не удалена");
        assertEquals(0, manager.getEpics().size(), "Эпик не удален");
    }

    @Test
    void printEpicListTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();
        manager.printEpicList();
    }

    @Test
    void clearEpicListTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();

        manager.clearEpicList();
        assertEquals(0, manager.getSubtasks().size(), "Подзадача эпика не удалена");
        assertEquals(0, manager.getPrioritizedList().size(), "Подзадача эпика не удалена");
        assertEquals(0, manager.getHistoryManager().getNodeMap().size(), "Подзадача эпика не удалена");
        assertEquals(0, manager.getEpics().size(), "Эпик не удалён");
    }

    @Test
    void printEpicSubtaskListTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();

        final int idForAction = 1;
        assertEquals(1, idForAction, "Значения id не совпадают.");

        manager.printEpicSubtaskList(idForAction);
    }

    @Test
    void createSubtaskTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();

        Subtask subtask = new Subtask(null, null, Types.SUBTASK,"Спринт #5", Status.NEW,
                LocalDateTime.of(2022, Month.DECEMBER, 26, 10, 00),
                Duration.ofMinutes(7200),"пройти теорию; снова доработать трекер задач");

        final int idForAction = 1;
        assertEquals(1, idForAction, "Значения id не совпадают.");

        manager.createSubtask(subtask, idForAction);
        final int subtaskId = subtask.getId();
        assertEquals(3, subtaskId,"Значения id не совпадают.");
        assertEquals(1, subtask.getEpicId(),"Значения epicId не совпадают.");

        final Subtask savedSubtask = manager.getSubtasks().get(subtaskId);
        assertNotNull(savedSubtask, "Подзачада не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(subtaskId), "Подзадачи не совпадают.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Подзадачи не возвращаются.");
        assertEquals(2, prioritizedList.size(), "Неверное количество подзадач.");
        assertTrue(prioritizedList.contains(subtask), "Подзадачи не совпадают.");

        Epic epic = manager.getEpics().get(idForAction);
        assertEquals(idForAction, epic.getId(), "Значения id не совпадают.");

        final Epic savedEpic = manager.getEpics().get(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(),"Статус рассчитан неверно.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(epic.getId()), "Эпики не совпадают.");

        final List<Integer> subtaskIds = epic.getSubtaskIds();
        assertEquals(2, subtaskIds.size());
        assertEquals(2, subtaskIds.get(0));
        assertEquals(3, subtaskIds.get(1));
    }

    @Test
    void updateSubtaskTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();

        Subtask subtask = new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.DONE,
                LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                Duration.ofMinutes(7200),"пройти теорию; создать трекер задач");

        final int epicIdForAction = 1;
        assertEquals(1, epicIdForAction, "Значения id не совпадают.");

        final int subtaskIdForAction = 2;
        assertEquals(2, subtaskIdForAction, "Значения id не совпадают.");

        manager.updateSubtask(subtask, epicIdForAction, subtaskIdForAction);
        final int subtaskId = subtask.getId();
        assertEquals(2, subtaskId,"Значения id не совпадают.");
        assertEquals(1, subtask.getEpicId(),"Значения epicId не совпадают.");

        final Subtask savedSubtask = manager.getSubtasks().get(subtaskId);
        assertNotNull(savedSubtask, "Подзачада не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(subtaskId), "Подзадачи не совпадают.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Подзадачи не возвращаются.");
        assertEquals(1, prioritizedList.size(), "Неверное количество подзадач.");
        assertTrue(prioritizedList.contains(subtask), "Подзадачи не совпадают.");

        Epic epic = manager.getEpics().get(epicIdForAction);
        assertEquals(epicIdForAction, epic.getId(), "Значения id не совпадают.");

        final Epic savedEpic = manager.getEpics().get(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(Status.DONE, epic.getStatus(),"Статус рассчитан неверно.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(epic.getId()), "Эпики не совпадают.");

        final List<Integer> subtaskIds = epic.getSubtaskIds();
        assertEquals(1, subtaskIds.size());
        assertEquals(2, subtaskIds.get(0));
    }

    @Test
    void printSubtaskTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();

        final int idForAction = 2;
        assertEquals(2, idForAction, "Значения id не совпадают.");
        assertEquals(2, manager.getSubtasks().get(idForAction).getId(), "Неверный порядок подзадач.");

        manager.printSubtask(idForAction);
    }

    @Test
    void deleteSubtaskTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();

        final int idForAction = 2;
        assertEquals(2, idForAction, "Значения id не совпадают.");

        Subtask deletedSubtask = manager.getSubtasks().get(idForAction);

        manager.deleteSubtask(idForAction);

        Epic epic = manager.getEpics().get(1);
        assertEquals(1, epic.getId(), "значения id не совпадают.");
        assertNotNull(epic, "Эпик не найден.");
        assertEquals(Status.NEW, epic.getStatus(),"Статус рассчитан неверно.");
        assertEquals(0, epic.getSubtaskIds().size(), "Неверное количество подзадач.");
        assertNull(epic.getDuration(), "Неверное количество подзадач.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(epic.getId()), "Эпики не совпадают.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertEquals(0, subtasks.size(), "Подзадача не удалена.");
        assertFalse(subtasks.containsKey(idForAction));

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertEquals(0, prioritizedList.size(), "Подзадача не удалена.");
        assertFalse(prioritizedList.contains(deletedSubtask), "Подзадача не удалена.");
    }

    @Test
    void printSubtaskListTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest1();
        manager.printSubtaskList();
    }

    @Test
    void clearSubtaskListTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest2();
        manager.clearSubtaskList();

        assertEquals(0, manager.getSubtasks().size(), "Подзадача не удалена");
        assertEquals(0, manager.getPrioritizedList().size(), "Подзадача не удалена");
        assertEquals(0, manager.getHistoryManager().getNodeMap().size(), "Подзадача не удалена");

        Epic epic = manager.getEpics().get(1);
        assertEquals(1, epic.getId(), "значения id не совпадают.");
        assertNotNull(epic, "Эпик не найден.");
        assertEquals(Status.NEW, epic.getStatus(),"Статус рассчитан неверно.");
        assertEquals(0, epic.getSubtaskIds().size(), "Неверное количество подзадач.");
        assertNull(epic.getDuration(), "Неверное количество подзадач.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(epic.getId()), "Эпики не совпадают.");
    }

    @Test
    void getHistoryTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest1();

        manager.printSubtask(3);
        assertEquals(1, manager.getHistoryManager().getHistory().size());
        assertEquals(manager.getSubtasks().get(3), manager.getHistoryManager().getHistory().get(0));

        manager.printSubtask(2);
        assertEquals(2, manager.getHistoryManager().getHistory().size());
        assertEquals(manager.getSubtasks().get(2), manager.getHistoryManager().getHistory().get(1));

        manager.printSubtask(3);
        assertEquals(2, manager.getHistoryManager().getHistory().size());
        assertEquals(manager.getSubtasks().get(3), manager.getHistoryManager().getHistory().get(1));

        manager.getHistory();
    }

    @Test
    void getPrioritizedTasksTest() throws URISyntaxException, IOException, InterruptedException {
        createEpicTest1();
        manager.getPrioritizedTasks();
    }

    @Test
    void getTasksTest() throws URISyntaxException, IOException, InterruptedException {
        HashMap<Integer, Task> tasks = manager.getTasks();
        assertEquals(0, tasks.size(), "Неправильный размер истории.");

        createTaskTest();
        assertEquals(1, tasks.size(), "Неправильный размер истории.");
        assertEquals(1, tasks.get(1).getId(), "Неправильный порядок элементов");
    }

    @Test
    void getSubtasksTest() throws URISyntaxException, IOException, InterruptedException {
        HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertEquals(0, subtasks.size(), "Неправильный размер истории.");

        createEpicTest1();
        assertEquals(2, subtasks.size(), "Неправильный размер истории.");
        assertEquals(2, subtasks.get(2).getId(), "Неправильный порядок элементов");
        assertEquals(3, subtasks.get(3).getId(), "Неправильный порядок элементов");
    }

    @Test
    void getEpicsTest() throws URISyntaxException, IOException, InterruptedException {
        HashMap<Integer, Epic> epics = manager.getEpics();
        assertEquals(0, epics.size(), "Неправильный размер истории.");

        createEpicTest1();
        assertEquals(1, epics.size(), "Неправильный размер истории.");
        assertEquals(1, epics.get(1).getId(), "Неправильный порядок элементов");
    }

    @Test
    void getHistoryManagerTest() throws URISyntaxException, IOException, InterruptedException {
        HistoryManager historyManager = manager.getHistoryManager();
        assertEquals(0, historyManager.getNodeMap().size(), "Неправильный размер истории.");
        assertNull(historyManager.getFirst(), "Неправильный размер истории.");
        assertNull(historyManager.getLast(), "Неправильный размер истории.");

        createEpicTest1();
        manager.printEpic(1);
        manager.printSubtask(3);
        manager.printSubtask(2);

        assertEquals(3, historyManager.getNodeMap().size(), "Неправильный размер истории.");
        assertNotNull(historyManager.getFirst(), "Неправильный размер истории.");
        assertNotNull(historyManager.getLast(), "Неправильный размер истории.");
        assertEquals(historyManager.getFirst().getTask(), historyManager.getNodeMap().get(1).getTask(), "Неправильный порядок элементов");
        assertEquals(historyManager.getLast().getTask(), historyManager.getNodeMap().get(2).getTask(), "Неправильный порядок элементов");
    }

    @Test
    void getPrioritizedListTest() throws URISyntaxException, IOException, InterruptedException {
        Set<Task> localPrioritizedList = new HashSet<>(manager.getPrioritizedList());
        assertEquals(0, localPrioritizedList.size(), "Неправильный размер списка.");

        createEpicTest1();
        localPrioritizedList = new HashSet<>(manager.getPrioritizedList());
        assertEquals(2, localPrioritizedList.size(), "Неправильный размер списка.");
        assertNotNull(localPrioritizedList, "Задачи не возвращаются.");
        assertTrue(localPrioritizedList.contains(manager.getSubtasks().get(2)),"Неправильный размер истории.");
        assertTrue(localPrioritizedList.contains(manager.getSubtasks().get(3)),"Неправильный размер истории.");
    }
}