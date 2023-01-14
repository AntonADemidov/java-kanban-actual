package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import managers.InMemoryTaskManager;
import utilities.Status;
import utilities.Types;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static InMemoryTaskManager manager;
    private static InMemoryTaskManager secondManager;

    @BeforeEach
    public void beforeEach() throws URISyntaxException, IOException, InterruptedException {
        manager = new InMemoryTaskManager();

        manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                        null,null, null,"спринт #1; спринт #2", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий"),
                new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию"));
    }

    @Test
    void equalsTest() throws URISyntaxException, IOException, InterruptedException {
        final Subtask subtask1 = manager.getSubtasks().get(2);
        secondManager = new InMemoryTaskManager();

        secondManager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                        null,null, null,"спринт #1; спринт #2", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий"),
                new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию"));

        final Subtask subtask2 = secondManager.getSubtasks().get(2);

        assertTrue(subtask1.equals(subtask2),"Объекты не равны");
    }

    @Test
    void hashCodeTest() throws URISyntaxException, IOException, InterruptedException {
        equalsTest();
        assertEquals(manager.getSubtasks().get(2).hashCode(), secondManager.getSubtasks().get(2).hashCode(),
                "HashCode объектов не равны");
    }

    @Test
    void getEpicIdTest() {
        final Subtask subtask = manager.getSubtasks().get(2);
        assertEquals(1, subtask.getEpicId(), "Неправильное значение id");
    }

    @Test
    void setEpicIdTest() {
        final Subtask subtask = manager.getSubtasks().get(2);
        assertEquals(1, subtask.getEpicId(), "Неправильное значение id");

        subtask.setEpicId(0);
        assertEquals(0, subtask.getEpicId(), "Неправильное значение id");
        subtask.setEpicId(2);
        assertEquals(2, subtask.getEpicId(), "Неправильное значение id");
    }
}