package servers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;
import tasks.*;
import utilities.Managers;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;
    String empty = "\n";

    public HttpTaskServer() throws IOException {
        Path sourceFile = Paths.get("src/main/resources/CurrentStatus.csv");
        Path targetFile = Paths.get("src/main/resources/CurrentStatusForApi.csv");
        Files.copy(sourceFile, targetFile);

        File file = new File("src/main/resources/CurrentStatusForApi.csv");
        taskManager = Managers.getSavedFileBackedTasksManager(file);
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
        //taskServer.stop();
    }

    public HttpTaskServer(File file) throws IOException {
        taskManager = Managers.getNewFileBackedTasksManager(file);
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            String[] pathParts = path.split("/");
            String response;

            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        taskManager.getPrioritizedTasks();
                        response = gson.toJson(taskManager.getPrioritizedList());
                        sendText(httpExchange, response);
                        break;
                    }

                    if (pathParts.length == 3) {
                        if (pathParts[2].equals("task")) {
                            taskManager.printTaskList();
                            response = gson.toJson(taskManager.getTasks());
                        } else if (pathParts[2].equals("epic")) {
                            taskManager.printEpicList();
                            response = gson.toJson(taskManager.getEpics());
                        } else if (pathParts[2].equals("subtask")) {
                            taskManager.printSubtaskList();
                            response = gson.toJson(taskManager.getSubtasks());
                        } else {
                            taskManager.getHistory();
                            response = gson.toJson(taskManager.getHistoryManager().getHistory());
                        }
                        sendText(httpExchange, response);
                        break;
                    }

                    if (pathParts.length == 4) {
                        String pathId = pathParts[3];
                        int id = parsePathId(pathId);

                        if (id != -1) {
                            if (pathParts[2].equals("task")) {
                                taskManager.printTask(id);
                                response = gson.toJson(taskManager.getTasks().get(id));
                                System.out.println(response);
                            } else if (pathParts[2].equals("epic")) {
                                taskManager.printEpic(id);
                                response = gson.toJson(taskManager.getEpics().get(id));
                            } else {
                                taskManager.printSubtask(id);
                                response = gson.toJson(taskManager.getSubtasks().get(id));
                            }
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен некорректный id = " + id);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }

                    if (pathParts.length == 5) {
                        String pathId = pathParts[4];
                        int id = parsePathId(pathId);
                        taskManager.printEpicSubtaskList(id);
                        List<Subtask> subtasks = new ArrayList<>();

                        for (Integer data : taskManager.getEpics().get(id).getSubtaskIds()) {
                            subtasks.add(taskManager.getSubtasks().get(data));
                        }

                        response = gson.toJson(subtasks);
                        sendText(httpExchange, response);
                        break;
                    }

                case "POST":
                    System.out.println("Началась обработка POST запроса от клиента.");
                    boolean successAction;
                    String phrase;

                    if (pathParts[2].equals("task")) {
                        String requestBody = readText(httpExchange);
                        Task task = gson.fromJson(requestBody, Task.class);

                        if (task.getId() == null) {
                            successAction = taskManager.createTask(task);

                            if (successAction) {
                                response = gson.toJson(taskManager.getTasks());
                                sendText(httpExchange, response);
                            } else {
                                phrase = "Задача не создана - пересечение по времени c другой задачей или подзадачей.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response, 400); // ???
                            }

                        } else {
                            int id = task.getId();
                            task.setId(null);
                            successAction = taskManager.updateTask(task, id);

                            if (successAction) {
                                response = gson.toJson(taskManager.getTasks());
                                sendText(httpExchange, response);
                            } else {
                                phrase = "Задача не обновлена - пересечение по времени c другой задачей или подзадачей.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response, 400); // ???
                            }
                        }
                        break;
                    }

                    if (pathParts[2].equals("epic")) {
                        String requestBody = readText(httpExchange);

                        JsonElement jsonElement = JsonParser.parseString(requestBody);

                        if(!jsonElement.isJsonObject()) {
                            System.out.println("Ответ от сервера не соответствует ожидаемому.");
                            return;
                        }

                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        JsonObject epicJson = jsonObject.get("epic").getAsJsonObject();
                        Epic epic = gson.fromJson(epicJson, Epic.class);

                        boolean quantity = jsonObject.has("subtask2");

                        Subtask subtask1;
                        Subtask subtask2;

                        if ((epic.getId() == null) && (!quantity)) {
                            JsonObject subtaskJson = jsonObject.get("subtask1").getAsJsonObject();
                            subtask1 = gson.fromJson(subtaskJson, Subtask.class);

                            successAction = taskManager.createEpic(epic, subtask1);

                            if (successAction) {
                                phrase = "Эпик с id # " + epic.getId() + " создан.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response);
                            } else {
                                phrase = "Эпик не создан - пересечение связанных подзадач по времени с другими задачами" +
                                        " или подзадачами.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response, 400); // ???
                            }

                        } else if ((epic.getId() == null) && (quantity)) {
                            JsonObject subtaskJson1 = jsonObject.get("subtask1").getAsJsonObject();
                            subtask1 = gson.fromJson(subtaskJson1, Subtask.class);
                            JsonObject subtaskJson2 = jsonObject.get("subtask2").getAsJsonObject();
                            subtask2 = gson.fromJson(subtaskJson2, Subtask.class);

                            successAction = taskManager.createEpic(epic, subtask1, subtask2);

                            if (successAction) {
                                phrase = "Эпик с id # " + epic.getId() + " создан.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response);
                            } else {
                                phrase = "Эпик не создан - пересечение связанных подзадач по времени с другими задачами" +
                                        " или подзадачами.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response, 400); // ???
                            }

                        } else {
                            JsonObject subtaskJson1 = jsonObject.get("subtask1").getAsJsonObject();
                            subtask1 = gson.fromJson(subtaskJson1, Subtask.class);
                            JsonObject subtaskJson2 = jsonObject.get("subtask2").getAsJsonObject();
                            subtask2 = gson.fromJson(subtaskJson2, Subtask.class);
                            int id = epic.getId();

                            epic.setId(null);
                            successAction = taskManager.updateEpic(epic, subtask1, subtask2, id);

                            if (successAction) {
                                phrase = "Эпик с id # " + epic.getId() + " обновлен.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response);
                            } else {
                                phrase = "Эпик не обновлен - пересечение связанных подзадач по времени с другими задачами" +
                                        " или подзадачами.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response, 400); // ???
                            }
                        }
                        break;
                    }

                    if (pathParts[2].equals("subtask")) {
                        String requestBody = readText(httpExchange);
                        Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                        Integer epicId = subtask.getEpicId();
                        Integer subtaskId = subtask.getId();

                        if (subtaskId == null) {
                            successAction = taskManager.createSubtask(subtask, epicId);

                            if (successAction) {
                                response = gson.toJson(taskManager.getSubtasks());
                                sendText(httpExchange, response);

                            } else {
                                phrase = "Подзадача не создана - пересечение по времени c одной из задач/подзадач.";
                                System.out.println(phrase);
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response, 400); // ???
                            }
                        } else {
                            successAction = taskManager.updateSubtask(subtask, epicId, subtaskId);
                            if (successAction) {
                                response = gson.toJson(taskManager.getSubtasks());
                                sendText(httpExchange, response);

                            } else {
                                phrase = "Подзадача не обновлена - пересечение по времени c другой задачей/подзадачей.";
                                response = gson.toJson(phrase);
                                sendText(httpExchange, response, 400); // ???
                            }
                        }
                        break;
                    }

                case "DELETE":
                    if (pathParts.length == 3) {
                        if (pathParts[2].equals("task")) {
                            taskManager.clearTaskList();
                            response = gson.toJson(taskManager.getTasks());
                        } else if (pathParts[2].equals("epic")) {
                            taskManager.clearEpicList();
                            response = gson.toJson(taskManager.getEpics());
                        } else {
                            taskManager.clearSubtaskList();
                            response = gson.toJson(taskManager.getSubtasks());
                        }
                        sendText(httpExchange, response);
                        break;
                    }

                    if (pathParts.length == 4) {
                        String pathId = pathParts[3];
                        int id = parsePathId(pathId);

                        if (id != -1) {
                            if (pathParts[2].equals("task")) {
                                taskManager.deleteTask(id);
                                response = gson.toJson(taskManager.getTasks());
                            } else if (pathParts[2].equals("epic")) {
                                taskManager.deleteEpic(id);
                                response = gson.toJson(taskManager.getEpics());
                            } else {
                                taskManager.deleteSubtask(id);
                                response = gson.toJson(taskManager.getSubtasks());
                            }
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен некорректный id = " + id);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }

                default:
                    phrase = "Ждем GET, POST или DELETE запросы, а получили - " + method;
                    System.out.println(empty + phrase);
                    response = gson.toJson(phrase);
                    sendText(httpExchange, response, 405);
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    protected void sendText(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(rCode, resp.length);
        h.getResponseBody().write(resp);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
}
