package servers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class KVServer {
    public static final int PORT = 8078;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    public static void main(String[] args) throws IOException {
            KVServer kvServer = new KVServer();
            kvServer.start();
            //kvServer.stop();
        }

    private void load(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/load");
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("GET".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/load/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения загрузки пустой. key указывается в пути: /load/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }

                if (data.containsKey(key)) {
                    String value = data.get(key);
                    System.out.printf("Значение value для ключа %s найдено", key);
                    System.out.println(value);
                    sendText(h, value);
                } else {
                    String value = "NO DATA";
                    sendText(h, value, 400);
                }
            } else {
                System.out.printf("/save ждёт POST-запрос, а получил: %s", h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(h)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.printf("Сохранено значение для ключа %s:\n%s", key, value);
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.printf("/save ждёт POST-запрос, а получил: %s\n", h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                System.out.printf("API_TOKEN=%s\n", apiToken);
                sendText(h, apiToken);
            } else {
                System.out.printf("/register ждёт GET-запрос, а получил %s", h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.printf("Запускаем сервер на порту %s\n", PORT);
        System.out.printf("Открой в браузере http://localhost:%s/\n", PORT);
        System.out.printf("API_TOKEN: %s\n", apiToken);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен");
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        System.out.println(rawQuery);
        return rawQuery != null && (rawQuery.contains(String.format("API_TOKEN=%s", apiToken)) || rawQuery.contains("API_TOKEN=DEBUG"));
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
}
