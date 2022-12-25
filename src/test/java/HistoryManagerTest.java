import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class HistoryManagerTest<T extends HistoryManager> {
    T historyManager;

    @Test
    void addTest() {
        final Task task1 = new Task(1, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                Duration.ofMinutes(480),"собрать коробки; упаковать кошку; сказать слова прощания");

        final Task task2 = new Task(2, Types.TASK,"Покупки", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 29, 10,00),
                Duration.ofMinutes(45),"молоко; кофе; яйца");

        final Task task3 = new Task(3, Types.TASK,"Покупки", Status.DONE,
                LocalDateTime.of(2022, Month.NOVEMBER, 30, 10,00),
                Duration.ofMinutes(60),"молоко; кофе; яйца; хлеб");

        final Subtask subtask = new Subtask(5, 4, Types.SUBTASK,"Спринт #3", Status.IN_PROGRESS,
                LocalDateTime.of(2022, Month.DECEMBER, 12, 10, 00),
                Duration.ofMinutes(7200),"пройти теорию; создать трекер задач");

        final Epic epic = new Epic(4,Types.EPIC,"Java developer. Модуль #1",Status.IN_PROGRESS,
                subtask.getStartTime(), subtask.getDuration(), subtask.getEndTime(),
                "спринт #3", Collections.singletonList(5));

        Map<Integer, Node> nodeMap = historyManager.getNodeMap();
        assertEquals(0, nodeMap.size(), "Неправильный размер истории.");
        assertNull(historyManager.getFirst(), "Неправильный размер истории.");
        assertNull(historyManager.getLast(), "Неправильный размер истории.");

        historyManager.add(subtask);
        assertEquals(1, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(subtask, nodeMap.get(subtask.getId()).getTask(), "Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getFirst().getTask(),"Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getLast().getTask(),"Неправильный порядок элементов");

        historyManager.add(task2);
        assertEquals(2, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(task2, nodeMap.get(task2.getId()).getTask(), "Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getFirst().getTask(),"Неправильный порядок элементов");
        assertEquals(task2, historyManager.getLast().getTask(),"Неправильный порядок элементов");

        historyManager.add(epic);
        assertEquals(3, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(epic, nodeMap.get(epic.getId()).getTask(), "Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getFirst().getTask(),"Неправильный порядок элементов");
        assertEquals(epic, historyManager.getLast().getTask(),"Неправильный порядок элементов");

        historyManager.add(task1);
        assertEquals(4, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(task1, nodeMap.get(task1.getId()).getTask(), "Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getFirst().getTask(),"Неправильный порядок элементов");
        assertEquals(task1, historyManager.getLast().getTask(),"Неправильный порядок элементов");

        historyManager.add(task3);
        assertEquals(5, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(task3, nodeMap.get(task3.getId()).getTask(), "Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getFirst().getTask(),"Неправильный порядок элементов");
        assertEquals(task3, historyManager.getLast().getTask(),"Неправильный порядок элементов");

        historyManager.add(subtask);
        assertEquals(5, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(subtask, nodeMap.get(subtask.getId()).getTask(), "Неправильный порядок элементов");
        assertEquals(task2, historyManager.getFirst().getTask(),"Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getLast().getTask(),"Неправильный порядок элементов");
    }

    @Test
    void removeTest() {
        addTest();

        final Map<Integer, Node> nodeMap = historyManager.getNodeMap();
        final Task task1 = nodeMap.get(1).getTask();
        final Task task2 = nodeMap.get(2).getTask();
        final Task task3 = nodeMap.get(3).getTask();
        final Epic epic = (Epic) nodeMap.get(4).getTask();
        final Subtask subtask = (Subtask) nodeMap.get(5).getTask();

        assertEquals(5, nodeMap.size(), "Неправильный размер истории.");
        assertNotNull(historyManager.getFirst(), "Неправильный размер истории.");
        assertEquals(task2, historyManager.getFirst().getTask(), "Неправильный порядок элементов");
        assertNotNull(historyManager.getLast(), "Неправильный размер истории.");
        assertEquals(subtask, historyManager.getLast().getTask(), "Неправильный порядок элементов");

        historyManager.remove(task2.getId());
        assertEquals(4, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(epic, historyManager.getFirst().getTask(), "Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getLast().getTask(), "Неправильный порядок элементов");

        historyManager.remove(task1.getId());
        assertEquals(3, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(epic, historyManager.getFirst().getTask(), "Неправильный порядок элементов");
        assertEquals(subtask, historyManager.getLast().getTask(), "Неправильный порядок элементов");

        historyManager.remove(subtask.getId());
        assertEquals(2, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(epic, historyManager.getFirst().getTask(), "Неправильный порядок элементов");
        assertEquals(task3, historyManager.getLast().getTask(), "Неправильный порядок элементов");

        historyManager.remove(task3.getId());
        assertEquals(1, nodeMap.size(), "Неправильный размер истории.");
        assertEquals(epic, historyManager.getFirst().getTask(), "Неправильный порядок элементов");
        assertEquals(epic, historyManager.getLast().getTask(), "Неправильный порядок элементов");

        historyManager.remove(epic.getId());
        assertEquals(0, nodeMap.size(), "Неправильный размер истории.");
        assertNull(historyManager.getFirst(), "Неправильный порядок элементов");
        assertNull(historyManager.getLast(), "Неправильный порядок элементов");
    }

    @Test
    void getHistoryTest() {
        List<Task> historyList = historyManager.getHistory();
        assertEquals(0, historyList.size());

        addTest();
        historyList = historyManager.getHistory();
        assertEquals(5, historyList.size(),"Неправильный размер истории.");
        assertEquals(2, historyList.get(0).getId(), "Неправильный порядок элементов");
        assertEquals(4, historyList.get(1).getId(), "Неправильный порядок элементов");
        assertEquals(1, historyList.get(2).getId(), "Неправильный порядок элементов");
        assertEquals(3, historyList.get(3).getId(), "Неправильный порядок элементов");
        assertEquals(5, historyList.get(4).getId(), "Неправильный порядок элементов");
    }

    @Test
    void getNodeMapTest() {
        Map<Integer, Node> nodeMap = historyManager.getNodeMap();
        assertEquals(0, nodeMap.size());

        addTest();
        assertEquals(5, nodeMap.size(),"Неправильный размер истории.");
        assertEquals(2, nodeMap.get(2).getTask().getId(), "Неправильный порядок элементов");
        assertEquals(4, nodeMap.get(4).getTask().getId(), "Неправильный порядок элементов");
        assertEquals(1, nodeMap.get(1).getTask().getId(), "Неправильный порядок элементов");
        assertEquals(3, nodeMap.get(3).getTask().getId(), "Неправильный порядок элементов");
        assertEquals(5, nodeMap.get(5).getTask().getId(), "Неправильный порядок элементов");
        assertEquals(2, historyManager.getFirst().getTask().getId(), "Неправильный порядок элементов");
        assertEquals(5, historyManager.getLast().getTask().getId(), "Неправильный порядок элементов");
    }

    @Test
    void getFirstTest() {
        Map<Integer, Node> nodeMap = historyManager.getNodeMap();
        assertEquals(0, nodeMap.size());

        addTest();
        assertEquals(5, nodeMap.size());
        assertEquals(2, historyManager.getFirst().getTask().getId(), "Неправильный порядок элементов");
    }

    @Test
    void getLastTest() {
        Map<Integer, Node> nodeMap = historyManager.getNodeMap();
        assertEquals(0, nodeMap.size());

        addTest();
        assertEquals(5, nodeMap.size());
        assertEquals(5, historyManager.getLast().getTask().getId(), "Неправильный порядок элементов");
    }
}