package managers;

import org.junit.jupiter.api.Test;
import servers.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utilities.Managers;
import utilities.Status;
import utilities.Types;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;
    URI uri;

    @BeforeEach
    void setUp() throws URISyntaxException, IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();

        uri = URI.create("http://localhost:8078");
        this.manager = Managers.getHttpTaskManager(uri);
    }

    @Test
    void loadTest() throws URISyntaxException, IOException, InterruptedException {
        manager.createTask(new Task(null, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                Duration.ofMinutes(480),"собрать коробки; упаковать кошку; сказать слова прощания"));

        manager.createTask(new Task(null, Types.TASK,"Покупки", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 29, 10,00),
                Duration.ofMinutes(45),"молоко; кофе; яйца"));

        manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                        null,null, null,"спринт #1; спринт #2", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий"),
                new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию"));

        manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1",null,
                        null,null,null,"спринт #3", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #3", Status.IN_PROGRESS,
                        LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать трекер задач"));

        manager.printSubtask(7);
        manager.printTask(2);
        manager.printEpic(3);
        manager.printSubtask(5);
        manager.printEpic(6);
        manager.printTask(1);
        manager.printSubtask(4);

        HttpTaskManager secondManager = new HttpTaskManager(uri);
        secondManager.load();

        final HashMap<Integer, Task> tasks = secondManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество эпиков.");
        assertEquals(1, tasks.get(1).getId(), "Несовпадение элементов.");
        assertEquals(2, tasks.get(2).getId(), "Несовпадение элементов.");
        assertEquals(manager.getTasks().get(1), tasks.get(1), "Несовпадение элементов.");
        assertEquals(manager.getTasks().get(2), tasks.get(2), "Несовпадение элементов.");

        final HashMap<Integer, Epic> epics = secondManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(3, epics.get(3).getId(), "Несовпадение элементов.");
        assertEquals(6, epics.get(6).getId(), "Несовпадение элементов.");
        assertEquals(manager.getEpics().get(3), epics.get(3), "Несовпадение элементов.");
        assertEquals(manager.getEpics().get(6), epics.get(6), "Несовпадение элементов.");

        final HashMap<Integer, Subtask> subtasks = secondManager.getSubtasks();
        assertNotNull(subtasks, "Эпики не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество эпиков.");
        assertEquals(4, subtasks.get(4).getId(), "Несовпадение элементов.");
        assertEquals(5, subtasks.get(5).getId(), "Несовпадение элементов.");
        assertEquals(7, subtasks.get(7).getId(), "Несовпадение элементов.");
        assertEquals(manager.getSubtasks().get(4), subtasks.get(4), "Несовпадение элементов.");
        assertEquals(manager.getSubtasks().get(5), subtasks.get(5), "Несовпадение элементов.");
        assertEquals(manager.getSubtasks().get(7), subtasks.get(7), "Несовпадение элементов.");

        final List<Task> list1 = manager.getHistoryManager().getHistory();
        final List<Task> list2 = secondManager.getHistoryManager().getHistory();
        assertEquals(7, list1.size());
        assertEquals(7, list2.size());
        assertEquals(list1.get(0).getId(), list2.get(0).getId(), "Несовпадение элементов.");
        assertEquals(list1.get(2).getId(), list2.get(2).getId(), "Несовпадение элементов.");
        assertEquals(list1.get(6).getId(), list2.get(6).getId(), "Несовпадение элементов.");

        final Set<Task> pList1 = manager.getPrioritizedList();
        final Set<Task> pList2 = secondManager.getPrioritizedList();
        assertNotNull(pList1, "Задачи не возвращаются.");
        assertNotNull(pList2, "Задачи не возвращаются.");
        assertEquals(5, pList1.size(), "Неверное количество задач.");
        assertEquals(5, pList2.size(), "Неверное количество задач.");
        assertEquals(pList1, pList2, "Несовпадение элементов.");
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }
}