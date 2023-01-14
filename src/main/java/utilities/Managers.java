package utilities;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import managers.HttpTaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    public static HttpTaskManager getHttpTaskManager(URI uri) throws URISyntaxException, IOException, InterruptedException {
        return new HttpTaskManager(uri);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}