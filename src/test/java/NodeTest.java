import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    private static InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();

        manager.createTask(new Task(null, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                Duration.ofMinutes(480),"собрать коробки; упаковать кошку; сказать слова прощания"));

        manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                        null,null, null,"спринт #1; спринт #2", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий"),
                new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию"));

        manager.printTask(1);
        manager.printSubtask(4);
        manager.printEpic(2);
        manager.printSubtask(3);
    }

    @Test
    void getTask() {
        Node node = manager.getHistoryManager().getNodeMap().get(1);
        assertEquals(node.getTask(), manager.getTasks().get(1));

        node = manager.getHistoryManager().getNodeMap().get(4);
        assertEquals(node.getTask(), manager.getSubtasks().get(4));

        node = manager.getHistoryManager().getNodeMap().get(2);
        assertEquals(node.getTask(), manager.getEpics().get(2));
    }

    @Test
    void getPrev() {
        Node node = manager.getHistoryManager().getNodeMap().get(1);
        assertNull(node.getPrev());

        node = manager.getHistoryManager().getNodeMap().get(2);
        assertEquals(manager.getSubtasks().get(4), node.getPrev().getTask());

        node = manager.getHistoryManager().getNodeMap().get(4);
        assertEquals(manager.getTasks().get(1), node.getPrev().getTask());
    }

    @Test
    void setPrev() {
        Node node = manager.getHistoryManager().getNodeMap().get(1);
        assertNull(node.getPrev());

        Node newPrev = manager.getHistoryManager().getNodeMap().get(3);
        node.setPrev(newPrev);
        assertEquals(newPrev.getTask(), node.getPrev().getTask());

        newPrev = null;
        node.setPrev(newPrev);
        assertNull(node.getPrev());
    }

    @Test
    void getNext() {
        Node node = manager.getHistoryManager().getNodeMap().get(1);
        assertEquals(manager.getSubtasks().get(4), node.getNext().getTask());

        node = manager.getHistoryManager().getNodeMap().get(3);
        assertNull(node.getNext());

        node = manager.getHistoryManager().getNodeMap().get(4);
        assertEquals(manager.getEpics().get(2), node.getNext().getTask());
    }

    @Test
    void setNext() {
        Node node = manager.getHistoryManager().getNodeMap().get(3);
        assertNull(node.getNext());

        Node newNext = manager.getHistoryManager().getNodeMap().get(1);
        node.setNext(newNext);
        assertEquals(newNext.getTask(), node.getNext().getTask());

        newNext = null;
        node.setNext(newNext);
        assertNull(node.getNext());
    }
}