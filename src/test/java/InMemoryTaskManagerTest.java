import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        this.manager = new InMemoryTaskManager();
    }

    @Test
    void findEpicStatusTest(){
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
        assertEquals(0, manager.getIdCounter(), "Неправильное значение счетчика");
        manager.setIdCounter(1);
        assertEquals(1, manager.getIdCounter(), "Неправильное значение счетчика");
    }

    @Test
    void setIdCounterTest() {
        assertEquals(0, manager.getIdCounter(), "Неправильное значение счетчика");
        manager.setIdCounter(1);
        assertEquals(1, manager.getIdCounter(), "Неправильное значение счетчика");
        manager.setIdCounter(5);
        assertEquals(5, manager.getIdCounter(), "Неправильное значение счетчика");
        manager.setIdCounter(0);
        assertEquals(0, manager.getIdCounter(), "Неправильное значение счетчика");
    }
}