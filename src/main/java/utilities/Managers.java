package utilities;

import managers.*;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getNewFileBackedTasksManager(File file) {
        return new FileBackedTasksManager(file);
    }

    public static TaskManager getSavedFileBackedTasksManager(File file) throws IOException {
        return FileBackedTasksManager.loadFromFIle(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}