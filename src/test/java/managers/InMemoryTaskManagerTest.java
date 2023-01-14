package managers;

import tasks.EpicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        this.manager = new InMemoryTaskManager();
    }

    @Test
    void findEpicStatusTest() throws URISyntaxException, IOException, InterruptedException {
        EpicTest test = new EpicTest();
        test.beforeEach();
        test.twoSubtasksWithStatusNewTest();

        test.beforeEach();
        test.twoSubtasksWithStatusDoneTest();

        test.beforeEach();
        test.twoSubtasksWithStatusInProgressTest();

        test.beforeEach();
        test.twoSubtasksWithStatusesNewAndDoneTest();

        test.beforeEach();
        test.twoSubtasksWithStatusesNewAndInProgressTest();

        test.beforeEach();
        test.twoSubtasksWithStatusesDoneAndInProgressTest();

        test.beforeEach();
        test.withSubtaskIdsEmptyListTest();
    }

    @Test
    void getIdCounterTest() {
        Assertions.assertEquals(0, manager.getIdCounter(), "Неправильное значение счетчика");
        manager.setIdCounter(1);
        Assertions.assertEquals(1, manager.getIdCounter(), "Неправильное значение счетчика");
    }

    @Test
    void setIdCounterTest() {
        Assertions.assertEquals(0, manager.getIdCounter(), "Неправильное значение счетчика");
        manager.setIdCounter(1);
        Assertions.assertEquals(1, manager.getIdCounter(), "Неправильное значение счетчика");
        manager.setIdCounter(5);
        Assertions.assertEquals(5, manager.getIdCounter(), "Неправильное значение счетчика");
        manager.setIdCounter(0);
        Assertions.assertEquals(0, manager.getIdCounter(), "Неправильное значение счетчика");
    }
}