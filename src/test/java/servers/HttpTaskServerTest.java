package servers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import managers.HttpTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utilities.Managers;
import utilities.Status;
import utilities.Types;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private HttpClient client;

    Gson gson;
    Task task;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException, InterruptedException {
        gson = Managers.getGson();
        client = HttpClient.newHttpClient();
        File file = new File("src/test/resources/Test1.csv");
        httpTaskServer = new HttpTaskServer(file);

        httpTaskServer.start();

        taskManager = httpTaskServer.getTaskManager();

        task = new Task(null, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                Duration.ofMinutes(480),"собрать коробки; упаковать кошку; сказать слова прощания");

        epic = new Epic(null, Types.EPIC,"Java developer. Модуль #1", null,
                null,null, null,"спринт #1; спринт #2", null);

        subtask1 = new Subtask(null, null, Types.SUBTASK,"Спринт #1", Status.DONE,
                        LocalDateTime.of(2022, Month.NOVEMBER, 30, 10, 00),
                        Duration.ofMinutes(7200),"пройти теорию; создать счетчик калорий");

        subtask2 = new Subtask(null, null, Types.SUBTASK,"Спринт #2", Status.DONE,
                        LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                        Duration.ofMinutes(7200),"пройти теорию; автоматизировать бухгалтерию");

        taskManager.createTask(task);
        taskManager.createEpic(epic, subtask1, subtask2);

        taskManager.printSubtask(4);
        taskManager.printTask(1);
        taskManager.printEpic(2);
        taskManager.printSubtask(3);
    }

    @AfterEach
    void tearDown(){
        httpTaskServer.stop();
    }

    @Test
    void printTaskListTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        HashMap<Integer, Task> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не возвращаются");
        assertEquals(1, object.size(), "Неверное количество объектов");
        assertEquals(task, object.get(1), "Объекты не совпадают");
    }

    @Test
    void printEpicListTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Epic>>() {}.getType();
        HashMap<Integer, Epic> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(1, object.size(), "Неверное количество объектов");
        assertEquals(epic, object.get(2), "Объекты не совпадают");
    }

    @Test
    void printSubtaskListTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Subtask>>() {}.getType();
        HashMap<Integer, Subtask> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(2, object.size(), "Неверное количество объектов");
        assertEquals(subtask1, object.get(3), "Объекты не совпадают");
        assertEquals(subtask2, object.get(4), "Объекты не совпадают");
    }

    @Test
    void printTaskTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<Task>() {}.getType();
        Task object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(task, object, "Объекты не совпадают");
        assertEquals(task, taskManager.getTasks().get(1), "Объекты не совпадают");
    }

    @Test
    void printEpicTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<Epic>() {}.getType();
        Epic object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(epic, object, "Объекты не совпадают");
        assertEquals(epic, taskManager.getEpics().get(2), "Объекты не совпадают");
    }

    @Test
    void printSubtaskTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask/3");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<Subtask>() {}.getType();
        Subtask object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(subtask1, object, "Объекты не совпадают");
        assertEquals(subtask1, taskManager.getSubtasks().get(3), "Объекты не совпадают");
    }

    @Test
    void printEpicSubtaskListTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<List<Subtask>>() {}.getType();
        List<Subtask> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(2, object.size(), "Неверное количество объектов");
        assertEquals(subtask1, object.get(0), "Объекты не совпадают");
        assertEquals(subtask2, object.get(1), "Объекты не совпадают");
    }

    @Test
    void getPrioritizedTaskTest() throws URISyntaxException, IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<Set<Task>>() {}.getType();
        Set<Task> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(3, object.size(), "Неверное количество объектов");
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<List<Task>>() {}.getType();
        List<Task> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(4, object.size(), "Неверное количество объектов");
        assertEquals(task.getId(), object.get(1).getId(), "Объекты не совпадают");
        assertEquals(epic.getId(), object.get(2).getId(), "Объекты не совпадают");
        assertEquals(subtask1.getId(), object.get(3).getId(), "Объекты не совпадают");
        assertEquals(subtask2.getId(), object.get(0).getId(), "Объекты не совпадают");
    }

    @Test
    void clearTaskListTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        HashMap<Integer, Task> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(0, object.size(), "Неверное количество объектов");
    }

    @Test
    void clearEpicListTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Epic>>() {}.getType();
        HashMap<Integer, Epic> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(0, object.size(), "Неверное количество объектов");
    }

    @Test
    void clearSubtaskListTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Subtask>>() {}.getType();
        HashMap<Integer, Subtask> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(0, object.size(), "Неверное количество объектов");
    }

    @Test
    void deleteTaskTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        HashMap<Integer, Task> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(0, object.size(), "Неверное количество объектов");
    }

    @Test
    void deleteEpicTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic/2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Epic>>() {}.getType();
        HashMap<Integer, Epic> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(0, object.size(), "Неверное количество объектов");
    }

    @Test
    void deleteSubtaskTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask/4");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Subtask>>() {}.getType();
        HashMap<Integer, Subtask> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(1, object.size(), "Неверное количество объектов");
        assertEquals(subtask1, object.get(3), "Неверное количество объектов");
    }

    @Test
    void createTaskSuccessfulTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");

        Task task = new Task(null, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2023, Month.JANUARY, 13, 10,00),
                Duration.ofMinutes(480),"переделать кучу дел");

        String json = gson.toJson(task);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            assertEquals(200, response.statusCode());

            Type elementType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
            HashMap<Integer, Task> object = gson.fromJson(response.body(), elementType);

            assertNotNull(object, "Объекты не совпадают");
            assertEquals(2, object.size(), "Неверное количество объектов");
            assertEquals(5, object.get(5).getId(), "Неверное количество объектов");
        } else {
            assertEquals(400, response.statusCode());
            assertEquals(response.body(), "Задача не создана - пересечение по времени c другой задачей или подзадачей.");
        }
    }

    @Test
    void createTaskUnsuccessfulTest() throws IOException, InterruptedException {
        createTaskSuccessfulTest();

        URI uri = URI.create("http://localhost:8080/tasks/task");

        Task task = new Task(null, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2023, Month.JANUARY, 13, 10,00),
                Duration.ofMinutes(480),"переделать кучу дел");

        String requestJson = gson.toJson(task);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(requestJson);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            assertEquals(200, response.statusCode());

            Type elementType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
            HashMap<Integer, Task> object = gson.fromJson(response.body(), elementType);

            assertNotNull(object, "Объекты не совпадают");
            assertEquals(2, object.size(), "Неверное количество объектов");
            assertEquals(5, object.get(5).getId(), "Неверное количество объектов");
        } else {
            assertEquals(400, response.statusCode());
            String responseJson = gson.toJson("Задача не создана - пересечение по времени c другой задачей или подзадачей.");
            assertEquals(responseJson, response.body(),"Объекты не совпадают");
        }
    }

    @Test
    void updateTaskSuccessfulTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");

        Task task = new Task(1, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 28, 10,00),
                Duration.ofMinutes(480),"все сделано");

        String json = gson.toJson(task);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            assertEquals(200, response.statusCode());

            Type elementType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
            HashMap<Integer, Task> object = gson.fromJson(response.body(), elementType);

            assertNotNull(object, "Объекты не совпадают");
            assertEquals(1, object.size(), "Неверное количество объектов");
            assertNotEquals(task.getDescription(), this.task, "Объекты совпадают");
            assertEquals("все сделано", object.get(1).getDescription(), "Объекты не совпадают");
        } else {
            assertEquals(400, response.statusCode());
            String responseJson = gson.toJson("Задача не обновлена - пересечение по времени c другой задачей или подзадачей.");
            assertEquals(responseJson, response.body(),"Объекты не совпадают");
        }
    }

    @Test
    void updateTaskUnsuccessfulTest() throws IOException, InterruptedException {
        updateTaskSuccessfulTest();

        URI uri = URI.create("http://localhost:8080/tasks/task");

        Task task = new Task(1, Types.TASK,"Переезд", Status.NEW,
                LocalDateTime.of(2022, Month.NOVEMBER, 30, 10,00),
                Duration.ofMinutes(480),"все сделано");

        String json = gson.toJson(task);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            assertEquals(200, response.statusCode());

            Type elementType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
            HashMap<Integer, Task> object = gson.fromJson(response.body(), elementType);

            assertNotNull(object, "Объекты не совпадают");
            assertEquals(1, object.size(), "Неверное количество объектов");
            assertNotEquals(task.getDescription(), this.task, "Объекты совпадают");
            assertEquals("все сделано", object.get(1).getDescription(), "Объекты не совпадают");
        } else {
            assertEquals(400, response.statusCode());
            String responseJson = gson.toJson("Задача не обновлена - пересечение по времени c другой задачей или подзадачей.");
            assertEquals(responseJson, response.body(),"Объекты не совпадают");
        }
    }

    @Test
    void createEpicWithTwoSubtasksSuccessfulTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");

        Epic epic = new Epic(null, Types.EPIC,"TestEpicName", null,
                null,null, null,"TestEpicDescription", null);

        Subtask subtask1 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480),"TestSubtaskDescription1");

        Subtask subtask2 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName2", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 13, 10, 01),
                Duration.ofMinutes(520),"TestSubtaskDescription2");

        String epicJson = gson.toJson(epic);
        String subtaskJson1 = gson.toJson(subtask1);
        String subtaskJson2 = gson.toJson(subtask2);

        HashMap<String, String> map = new HashMap<>();
        map.put("epic", epicJson);
        map.put("subtask1", subtaskJson1);
        map.put("subtask2", subtaskJson2);

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(String.valueOf(map));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String responseJson = gson.toJson("Эпик с id # 5 создан.");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
        assertEquals(2, taskManager.getEpics().size(), "Неверное количество объектов");
        assertEquals(4, taskManager.getSubtasks().size(), "Неверное количество объектов");
        assertEquals(epic.getName(), taskManager.getEpics().get(5).getName(),"Объекты не совпадают");
        assertEquals(subtask1.getName(), taskManager.getSubtasks().get(6).getName(),"Объекты не совпадают");
        assertEquals(subtask2.getName(), taskManager.getSubtasks().get(7).getName(),"Объекты не совпадают");
    }

    @Test
    void createEpicWithTwoSubtasksUnsuccessfulTest() throws IOException, InterruptedException {
        createEpicWithTwoSubtasksSuccessfulTest();

        URI uri = URI.create("http://localhost:8080/tasks/epic");

        Epic epic = new Epic(null, Types.EPIC,"TestEpicName", null,
                null,null, null,"TestEpicDescription", null);

        Subtask subtask1 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480),"TestSubtaskDescription1");

        Subtask subtask2 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName2", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 13, 10, 01),
                Duration.ofMinutes(520),"TestSubtaskDescription2");

        String epicJson = gson.toJson(epic);
        String subtaskJson1 = gson.toJson(subtask1);
        String subtaskJson2 = gson.toJson(subtask2);

        HashMap<String, String> map = new HashMap<>();
        map.put("epic", epicJson);
        map.put("subtask1", subtaskJson1);
        map.put("subtask2", subtaskJson2);

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(String.valueOf(map));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        String responseJson = gson.toJson("Эпик не создан - пересечение связанных подзадач по времени с другими задачами" +
                " или подзадачами.");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
        assertEquals(2, taskManager.getEpics().size(), "Неверное количество объектов");
        assertEquals(4, taskManager.getSubtasks().size(), "Неверное количество объектов");
    }

    @Test
    void createEpicWithOneSubtaskSuccessfulTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");

        Epic epic = new Epic(null, Types.EPIC,"TestEpicName", null,
                null,null, null,"TestEpicDescription", null);

        Subtask subtask1 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480),"TestSubtaskDescription1");

        String epicJson = gson.toJson(epic);
        String subtaskJson1 = gson.toJson(subtask1);

        HashMap<String, String> map = new HashMap<>();
        map.put("epic", epicJson);
        map.put("subtask1", subtaskJson1);

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(String.valueOf(map));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String responseJson = gson.toJson("Эпик с id # 5 создан.");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
        assertEquals(2, taskManager.getEpics().size(), "Неверное количество объектов");
        assertEquals(3, taskManager.getSubtasks().size(), "Неверное количество объектов");
        assertEquals(epic.getName(), taskManager.getEpics().get(5).getName(),"Объекты не совпадают");
        assertEquals(subtask1.getName(), taskManager.getSubtasks().get(6).getName(),"Объекты не совпадают");
    }

    @Test
    void createEpicWithOneSubtaskUnsuccessfulTest() throws IOException, InterruptedException {
        createEpicWithOneSubtaskSuccessfulTest();

        URI uri = URI.create("http://localhost:8080/tasks/epic");

        Epic epic = new Epic(null, Types.EPIC,"TestEpicName", null,
                null,null, null,"TestEpicDescription", null);

        Subtask subtask1 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480),"TestSubtaskDescription1");

        String epicJson = gson.toJson(epic);
        String subtaskJson1 = gson.toJson(subtask1);

        HashMap<String, String> map = new HashMap<>();
        map.put("epic", epicJson);
        map.put("subtask1", subtaskJson1);

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(String.valueOf(map));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());

        String responseJson = gson.toJson("Эпик не создан - пересечение связанных подзадач по времени с другими задачами" +
                " или подзадачами.");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
        assertEquals(2, taskManager.getEpics().size(), "Неверное количество объектов");
        assertEquals(3, taskManager.getSubtasks().size(), "Неверное количество объектов");
    }

    @Test
    void updateEpicWithTwoSubtasksSuccessfulTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic");

        Epic epic = new Epic(2, Types.EPIC,"TestEpicName", null,
                null,null, null,"TestEpicDescription", null);

        Subtask subtask1 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480),"TestSubtaskDescription1");

        Subtask subtask2 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName2", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 13, 10, 01),
                Duration.ofMinutes(520),"TestSubtaskDescription2");

        String epicJson = gson.toJson(epic);
        String subtaskJson1 = gson.toJson(subtask1);
        String subtaskJson2 = gson.toJson(subtask2);

        HashMap<String, String> map = new HashMap<>();
        map.put("epic", epicJson);
        map.put("subtask1", subtaskJson1);
        map.put("subtask2", subtaskJson2);

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(String.valueOf(map));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String responseJson = gson.toJson("Эпик с id # 2 обновлен.");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество объектов");
        assertEquals(2, taskManager.getSubtasks().size(), "Неверное количество объектов");
        assertEquals(epic.getName(), taskManager.getEpics().get(2).getName(),"Объекты не совпадают");
        assertEquals(subtask1.getName(), taskManager.getSubtasks().get(5).getName(),"Объекты не совпадают");
        assertEquals(subtask2.getName(), taskManager.getSubtasks().get(6).getName(),"Объекты не совпадают");
    }

    @Test
    void updateEpicWithTwoSubtasksSuccessfulTest2() throws IOException, InterruptedException {
        updateEpicWithTwoSubtasksSuccessfulTest();;

        URI uri = URI.create("http://localhost:8080/tasks/epic");

        Epic epic = new Epic(2, Types.EPIC,"TestEpicName", null,
                null,null, null,"TestEpicDescription", null);

        Subtask subtask1 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480),"TestSubtaskDescription1");

        Subtask subtask2 = new Subtask(null, null, Types.SUBTASK,"TestSubtaskName2", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 13, 10, 01),
                Duration.ofMinutes(520),"TestSubtaskDescription2");

        String epicJson = gson.toJson(epic);
        String subtaskJson1 = gson.toJson(subtask1);
        String subtaskJson2 = gson.toJson(subtask2);

        HashMap<String, String> map = new HashMap<>();
        map.put("epic", epicJson);
        map.put("subtask1", subtaskJson1);
        map.put("subtask2", subtaskJson2);

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(String.valueOf(map));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String responseJson = gson.toJson("Эпик с id # 2 обновлен.");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество объектов");
        assertEquals(2, taskManager.getSubtasks().size(), "Неверное количество объектов");
        assertEquals(epic.getName(), taskManager.getEpics().get(2).getName(),"Объекты не совпадают");
        assertEquals(subtask1.getName(), taskManager.getSubtasks().get(7).getName(),"Объекты не совпадают");
        assertEquals(subtask2.getName(), taskManager.getSubtasks().get(8).getName(),"Объекты не совпадают");
    }

    @Test
    void createSubtaskSuccessfulTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask");

        Subtask subtask1 = new Subtask(null, 2, Types.SUBTASK, "TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480), "TestSubtaskDescription1");

        String json = gson.toJson(subtask1);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Subtask>>() {}.getType();
        HashMap<Integer, Subtask> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(3, object.size(), "Неверное количество объектов");
        assertEquals(5, object.get(5).getId(), "Неверное количество объектов");
    }

    @Test
    void createSubtaskUnsuccessfulTest() throws IOException, InterruptedException {
        createSubtaskSuccessfulTest();

        URI uri = URI.create("http://localhost:8080/tasks/subtask");

        Subtask subtask1 = new Subtask(null, 2, Types.SUBTASK, "TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480), "TestSubtaskDescription1");

        String json = gson.toJson(subtask1);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());

        String responseJson = gson.toJson("Подзадача не создана - пересечение по времени c одной из задач/подзадач.");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
        assertEquals(3, taskManager.getSubtasks().size(), "Неверное количество объектов");
    }

    @Test
    void updateSubtaskSuccessfulTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask");

        Subtask subtask1 = new Subtask(3, 2, Types.SUBTASK, "TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2023, Month.JANUARY, 12, 10, 00),
                Duration.ofMinutes(480), "TestSubtaskDescription1");

        String json = gson.toJson(subtask1);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type elementType = new TypeToken<HashMap<Integer, Subtask>>() {}.getType();
        HashMap<Integer, Subtask> object = gson.fromJson(response.body(), elementType);

        assertNotNull(object, "Объекты не совпадают");
        assertEquals(2, object.size(), "Неверное количество объектов");
        assertEquals(3, object.get(3).getId(), "Неверное количество объектов");
    }

    @Test
    void updateSubtaskUnsuccessfulTest() throws IOException, InterruptedException {
        updateSubtaskSuccessfulTest();

        URI uri = URI.create("http://localhost:8080/tasks/subtask");

        Subtask subtask1 = new Subtask(3, 2, Types.SUBTASK, "TestSubtaskName1", Status.DONE,
                LocalDateTime.of(2022, Month.DECEMBER, 05, 10, 01),
                Duration.ofMinutes(480), "TestSubtaskDescription1");

        String json = gson.toJson(subtask1);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        String responseJson = gson.toJson("Подзадача не обновлена - пересечение по времени c другой задачей/подзадачей.");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
        assertEquals(2, taskManager.getSubtasks().size(), "Неверное количество объектов");
    }

    @Test
    void wrongMethodTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task");

        Task task = new Task(null, Types.TASK, "Переезд", Status.NEW,
                LocalDateTime.of(2023, Month.JANUARY, 13, 10, 00),
                Duration.ofMinutes(480), "переделать кучу дел");

        String json = gson.toJson(task);
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).PUT(publisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());

        String responseJson = gson.toJson("Ждем GET, POST или DELETE запросы, а получили - PUT");
        assertEquals(responseJson, response.body(),"Объекты не совпадают");
    }
}