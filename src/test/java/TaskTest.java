import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private static InMemoryTaskManager manager;
    private static InMemoryTaskManager secondManager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();

        manager.createTask(new Task(null, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                Duration.ofMinutes(480),"собрать коробки; упаковать кошку; сказать слова прощания"));
    }

    @Test
    void getEndTimeTest() {
        final Task task = manager.getTasks().get(1);
        LocalDateTime startTime = task.getStartTime();
        Duration duration = task.getDuration();

        LocalDateTime endTime = task.getEndTime();
        assertEquals(startTime.plus(duration), endTime);

        LocalDateTime newStartTime = null;
        task.setStartTime(newStartTime);
        endTime = task.getEndTime();
        assertNull(endTime);

        Duration newDuration = null;
        task.setDuration(newDuration);
        endTime = task.getEndTime();
        assertNull(endTime);

        newStartTime = LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00);
        task.setStartTime(newStartTime);
        endTime = task.getEndTime();
        assertNull(endTime);
    }

    @Test
    void getIdTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(1, task.getId(), "Неправильное значение id");
    }

    @Test
    void setIdTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(1, task.getId(), "Неправильное значение id");
        task.setId(0);
        assertEquals(0, task.getId(), "Неправильное значение id");
        task.setId(2);
        assertEquals(2, task.getId(), "Неправильное значение id");
    }

    @Test
    void getNameTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals("Переезд", task.getName(), "Неправильное значение name");
    }

    @Test
    void setNameTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals("Переезд", task.getName(), "Неправильное значение name");
        task.setName("Отъезд");
        assertEquals("Отъезд", task.getName(), "Неправильное значение name");
    }

    @Test
    void getStatusTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(Status.NEW, task.getStatus(), "Неправильное значение status");
    }

    @Test
    void setStatusTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(Status.NEW, task.getStatus(), "Неправильное значение status");
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus(), "Неправильное значение status");
    }

    @Test
    void getDescriptionTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals("собрать коробки; упаковать кошку; сказать слова прощания", task.getDescription(),
                "Неправильное значение description");
    }

    @Test
    void setDescriptionTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals("собрать коробки; упаковать кошку; сказать слова прощания", task.getDescription(),
                "Неправильное значение description");
        task.setDescription("Пока-пока");
        assertEquals("Пока-пока", task.getDescription(), "Неправильное значение description");
    }

    @Test
    void getTypeTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(Types.TASK, task.getType(), "Неправильное значение type");
    }

    @Test
    void getDurationTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(Duration.ofMinutes(480), task.getDuration(), "Неправильное значение duration");
    }

    @Test
    void setDurationTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(Duration.ofMinutes(480), task.getDuration(), "Неправильное значение duration");
        task.setDuration(Duration.ofMinutes(960));
        assertEquals(Duration.ofMinutes(960), task.getDuration(), "Неправильное значение duration");
    }

    @Test
    void getStartTimeTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00), task.getStartTime(),
                "Неправильное значение startTime");
    }

    @Test
    void setStartTimeTest() {
        final Task task = manager.getTasks().get(1);
        assertEquals(LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00), task.getStartTime(),
                "Неправильное значение startTime");
        task.setStartTime(LocalDateTime.of(2023, Month.NOVEMBER, 28, 10,00));
        assertEquals(LocalDateTime.of(2023, Month.NOVEMBER, 28, 10,00), task.getStartTime(),
                "Неправильное значение startTime");
    }

    @Test
    void equalsTest() {
        final Task task1 = manager.getTasks().get(1);
        secondManager = new InMemoryTaskManager();

        secondManager.createTask(new Task(null, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                Duration.ofMinutes(480),"собрать коробки; упаковать кошку; сказать слова прощания"));

        final Task task2 = secondManager.getTasks().get(1);

        assertEquals(task1, task2, "Объекты не равны");
    }

    @Test
    void hashCodeTest() {
        equalsTest();
        assertEquals(manager.getTasks().get(1).hashCode(), secondManager.getTasks().get(1).hashCode(),
                "HashCode объектов не равны");
    }
}