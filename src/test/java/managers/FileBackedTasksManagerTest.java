package managers;

import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file;

    @BeforeEach
    void setUp() {
        this.file = new File("src/test/resources/CurrentTest_" + System.nanoTime() +".csv");
        this.manager = new FileBackedTasksManager(file);
    }

    @AfterEach
    void tearDown() {
        assertTrue(file.delete());
    }

    @Test
    void loadFromFileBasicTest() throws IOException {
        Path sourceFile = Paths.get("src/test/resources/BasicList.csv");
        Path targetFile = Paths.get("src/test/resources/CurrentStatusTest.csv");
        Files.copy(sourceFile, targetFile);

        this.file = new File("src/test/resources/CurrentStatusTest.csv");
        this.manager = FileBackedTasksManager.loadFromFIle(file);

        final HashMap<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(1, tasks.get(1).getId(), "Несовпадение элементов.");
        assertEquals(2, tasks.get(2).getId(), "Несовпадение элементов.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(3, epics.get(3).getId(), "Несовпадение элементов.");
        assertEquals(6, epics.get(6).getId(), "Несовпадение элементов.");

        final List<Integer> subtaskIds1 = manager.getEpics().get(3).getSubtaskIds();
        assertEquals(2, subtaskIds1.size(), "Неверное количество подзадач.");
        assertEquals(4, subtaskIds1.get(0), "Несовпадение элементов.");
        assertEquals(5, subtaskIds1.get(1), "Несовпадение элементов.");

        final List<Integer> subtaskIds2 = manager.getEpics().get(6).getSubtaskIds();
        assertEquals(1, subtaskIds2.size(), "Неверное количество подзадач.");
        assertEquals(7, subtaskIds2.get(0), "Несовпадение элементов.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(4, subtasks.get(4).getId(), "Несовпадение элементов.");
        assertEquals(5, subtasks.get(5).getId(), "Несовпадение элементов.");
        assertEquals(7, subtasks.get(7).getId(), "Несовпадение элементов.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Задачи не возвращаются.");
        assertEquals(5, prioritizedList.size(), "Неверное количество задач.");

        final List<Task> historyList = manager.getHistoryManager().getHistory();
        assertEquals(3, historyList.size(),"Неправильный размер истории.");
        assertEquals(2, historyList.get(0).getId(), "Неправильный порядок элементов");
        assertEquals(3, historyList.get(1).getId(), "Неправильный порядок элементов");
        assertEquals(5, historyList.get(2).getId(), "Неправильный порядок элементов");

        manager.printTaskList();
        manager.printEpicList();
        manager.printSubtaskList();
        manager.getHistory();
        manager.getPrioritizedTasks();
    }

    @Test
    void loadFromFileWithEmptyListTest() throws IOException {
        Path sourceFile = Paths.get("src/test/resources/EmptyList.csv");
        Path targetFile = Paths.get("src/test/resources/CurrentStatusTest.csv");
        Files.copy(sourceFile, targetFile);

        this.file = new File("src/test/resources/CurrentStatusTest.csv");
        this.manager = FileBackedTasksManager.loadFromFIle(file);

        final HashMap<Integer, Task> tasks = manager.getTasks();
        assertEquals(0, tasks.size(), "Неверное количество задач.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertEquals(0, epics.size(), "Неверное количество эпиков.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertEquals(0, subtasks.size(), "Неверное количество подзадач.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertEquals(0, prioritizedList.size(), "Неверное количество задач.");

        final List<Task> historyList = manager.getHistoryManager().getHistory();
        assertEquals(0, historyList.size(),"Неправильный размер истории.");

        manager.printTaskList();
        manager.printEpicList();
        manager.printSubtaskList();
        manager.getHistory();
        manager.getPrioritizedTasks();
    }

    @Test
    void loadFromFileWithNoHistoryListTest() throws IOException {
        Path sourceFile = Paths.get("src/test/resources/NoHistoryList.csv");
        Path targetFile = Paths.get("src/test/resources/CurrentStatusTest.csv");
        Files.copy(sourceFile, targetFile);

        this.file = new File("src/test/resources/CurrentStatusTest.csv");
        this.manager = FileBackedTasksManager.loadFromFIle(file);

        final HashMap<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(1, tasks.get(1).getId(), "Несовпадение элементов.");
        assertEquals(2, tasks.get(2).getId(), "Несовпадение элементов.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(3, epics.get(3).getId(), "Несовпадение элементов.");
        assertEquals(6, epics.get(6).getId(), "Несовпадение элементов.");

        final List<Integer> subtaskIds1 = manager.getEpics().get(3).getSubtaskIds();
        assertEquals(2, subtaskIds1.size(), "Неверное количество подзадач.");
        assertEquals(4, subtaskIds1.get(0), "Несовпадение элементов.");
        assertEquals(5, subtaskIds1.get(1), "Несовпадение элементов.");

        final List<Integer> subtaskIds2 = manager.getEpics().get(6).getSubtaskIds();
        assertEquals(1, subtaskIds2.size(), "Неверное количество подзадач.");
        assertEquals(7, subtaskIds2.get(0), "Несовпадение элементов.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(4, subtasks.get(4).getId(), "Несовпадение элементов.");
        assertEquals(5, subtasks.get(5).getId(), "Несовпадение элементов.");
        assertEquals(7, subtasks.get(7).getId(), "Несовпадение элементов.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Задачи не возвращаются.");
        assertEquals(5, prioritizedList.size(), "Неверное количество задач.");

        final List<Task> historyList = manager.getHistoryManager().getHistory();
        assertEquals(0, historyList.size(),"Неправильный размер истории.");

        manager.printTaskList();
        manager.printEpicList();
        manager.printSubtaskList();
        manager.getHistory();
        manager.getPrioritizedTasks();
    }

    @Test
    void loadFromFileWithEpicWithoutSubtasksListTest() throws IOException {
        Path sourceFile = Paths.get("src/test/resources/EpicWithoutSubtasksList.csv");
        Path targetFile = Paths.get("src/test/resources/CurrentStatusTest.csv");
        Files.copy(sourceFile, targetFile);

        this.file = new File("src/test/resources/CurrentStatusTest.csv");
        this.manager = FileBackedTasksManager.loadFromFIle(file);

        final HashMap<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(1, tasks.get(1).getId(), "Несовпадение элементов.");
        assertEquals(2, tasks.get(2).getId(), "Несовпадение элементов.");

        final HashMap<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(3, epics.get(3).getId(), "Несовпадение элементов.");
        assertEquals(6, epics.get(6).getId(), "Несовпадение элементов.");

        final List<Integer> subtaskIds1 = manager.getEpics().get(3).getSubtaskIds();
        assertEquals(0, subtaskIds1.size(), "Неверное количество подзадач.");

        final List<Integer> subtaskIds2 = manager.getEpics().get(6).getSubtaskIds();
        assertEquals(1, subtaskIds2.size(), "Неверное количество подзадач.");
        assertEquals(7, subtaskIds2.get(0), "Несовпадение элементов.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(7, subtasks.get(7).getId(), "Несовпадение элементов.");

        final Set<Task> prioritizedList = manager.getPrioritizedList();
        assertNotNull(prioritizedList, "Задачи не возвращаются.");
        assertEquals(3, prioritizedList.size(), "Неверное количество задач.");

        final List<Task> historyList = manager.getHistoryManager().getHistory();
        assertEquals(3, historyList.size(),"Неправильный размер истории.");

        manager.printTaskList();
        manager.printEpicList();
        manager.printSubtaskList();
        manager.getHistory();
        manager.getPrioritizedTasks();
    }
}