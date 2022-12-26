package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import managers.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private static InMemoryTaskManager manager;
    private static InMemoryTaskManager secondManager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();

        manager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                        null,null, null,"cпринт #1; cпринт #2", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий"),
                new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию"));
    }

    @Test
    public void twoSubtasksWithStatusNewTest() {
        for (Subtask subtask : manager.getSubtasks().values()) {
            subtask.setStatus(Status.NEW);
        }
        assertEquals(Status.NEW, manager.findEpicStatus(manager.getEpics().get(1)), "Тест пройден");
    }

    @Test
    public void twoSubtasksWithStatusDoneTest() {
        assertEquals(Status.DONE, manager.findEpicStatus(manager.getEpics().get(1)), "Тест пройден");
    }

    @Test
    public void twoSubtasksWithStatusInProgressTest() {
        for (Subtask subtask : manager.getSubtasks().values()) {
            subtask.setStatus(Status.IN_PROGRESS);
        }
        assertEquals(Status.IN_PROGRESS, manager.findEpicStatus(manager.getEpics().get(1)), "Тест пройден");
    }

    @Test
    public void twoSubtasksWithStatusesNewAndDoneTest() {
        manager.getSubtasks().get(2).setStatus(Status.NEW);
        assertEquals(Status.IN_PROGRESS, manager.findEpicStatus(manager.getEpics().get(1)), "Тест пройден");
    }

    @Test
    public void twoSubtasksWithStatusesNewAndInProgressTest() {
        manager.getSubtasks().get(2).setStatus(Status.NEW);
        manager.getSubtasks().get(3).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.findEpicStatus(manager.getEpics().get(1)), "Тест пройден");
    }

    @Test
    public void twoSubtasksWithStatusesDoneAndInProgressTest() {
        manager.getSubtasks().get(3).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.findEpicStatus(manager.getEpics().get(1)), "Тест пройден");
    }

    @Test
    public void withSubtaskIdsEmptyListTest() {
        manager.getSubtasks().remove(2);
        manager.getSubtasks().remove(3);
        assertEquals(Status.NEW, manager.findEpicStatus(manager.getEpics().get(1)), "Тест пройден");
    }

    @Test
    void getSubtaskIdsTest() {
        final Epic epic = manager.getEpics().get(1);
        List<Integer> subtaskIds = epic.getSubtaskIds();
        assertEquals(2, subtaskIds.size(), "Неверный размер списка");
        assertEquals(manager.getSubtasks().get(2).getId(), subtaskIds.get(0),"Несовпадение элементов списка");
        assertEquals(manager.getSubtasks().get(3).getId(), subtaskIds.get(1),"Несовпадение элементов списка");
    }

    @Test
    void setSubtaskIdsTest() {
        final Epic epic = manager.getEpics().get(1);
        List<Integer> subtaskIds = new ArrayList<>();

        epic.setSubtaskIds(subtaskIds);
        assertEquals(0, subtaskIds.size(), "Неверный размер списка");
    }


    @Test
    void getEndTimeTest() {
        final Epic epic = manager.getEpics().get(1);
        final Subtask subtask2 = manager.getSubtasks().get(3);

        assertEquals(subtask2.getEndTime(), epic.getEndTime(), "Неправильное значение endTime");
    }

    @Test
    void setEndTimeTest() {
        final Epic epic = manager.getEpics().get(1);
        final Subtask subtask2 = manager.getSubtasks().get(3);

        assertEquals(subtask2.getEndTime(), epic.getEndTime(), "Неправильное значение endTime");

        LocalDateTime newEndTime = epic.getEndTime().plusMinutes(30);
        epic.setEndTime(newEndTime);
        assertEquals(newEndTime, epic.getEndTime(), "Неправильное значение endTime");
    }

    @Test
    void equalsTest() {
        final Epic epic1 = manager.getEpics().get(1);
        secondManager = new InMemoryTaskManager();

        secondManager.createEpic(new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                        null,null, null,"cпринт #1; cпринт #2", null),
                new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий"),
                new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию"));

        final Epic epic2 = secondManager.getEpics().get(1);

        assertTrue(epic1.equals(epic2),"Объекты не равны");
    }

    @Test
    void hashCodeTest() {
        equalsTest();
        assertEquals(manager.getEpics().get(1).hashCode(), secondManager.getEpics().get(1).hashCode(),
                "HashCode объектов не равны");
    }
}