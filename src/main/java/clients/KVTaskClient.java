package clients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URI uri;
    private final HttpClient client;
    private String apiToken;

    public KVTaskClient(URI uri) throws URISyntaxException, IOException, InterruptedException {
        this.uri = uri;
        client = HttpClient.newHttpClient();
        register();
    }

    public void register() throws URISyntaxException, IOException, InterruptedException {
        String toDO = "/register";
        URI action = new URI(uri + toDO);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(action)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        apiToken = response.body();
    }

    public void put(String key, String json) throws URISyntaxException, IOException, InterruptedException {
        String toDO = "/save/" + key + "?API_TOKEN=" + apiToken;
        URI action = new URI(uri + toDO);

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(publisher)
                .uri(action)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request, handler);
    }

    public String load(String key) throws URISyntaxException, IOException, InterruptedException {
        String toDO = "/load/" + key + "?API_TOKEN=" + apiToken;
        URI action = new URI(uri + toDO);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(action)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        return response.body();
    }
}


