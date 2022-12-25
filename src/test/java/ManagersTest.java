import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultTest() {
        TaskManager manager = Managers.getDefault();
        assertEquals(manager.getClass(), InMemoryTaskManager.class, "Реализация не совпадает");
    }

    @Test
    void getNewFileBackedTasksManagerTest() {
        File file = null;
        assertNull(file, "Файл инициализирован");
        file = new File("src/test/resources/BasicList.csv");
        assertNotNull(file, "Файл не инициализирован");
        TaskManager manager = Managers.getNewFileBackedTasksManager(file);
        assertEquals(manager.getClass(), FileBackedTasksManager.class, "Реализация не совпадает");
    }

    @Test
    void getSavedFileBackedTasksManagerTest() throws IOException {
        File file = new File("src/test/resources/BasicList.csv");
        assertNotNull(file, "Файл не инициализирован");
        TaskManager manager = Managers.getSavedFileBackedTasksManager(file);
        assertEquals(manager.getClass(), FileBackedTasksManager.class, "Реализация не совпадает");

    }

    @Test
    void getDefaultHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertEquals(historyManager.getClass(), InMemoryHistoryManager.class, "Реализация не совпадает");
    }
}