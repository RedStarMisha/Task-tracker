import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    Gson gson = new Gson();
    String baseURI;
    private final String token;
    HttpClient client = HttpClient.newHttpClient();


    public KVTaskClient(String link) throws IOException, InterruptedException {
        baseURI = link;
        token = register(link);
    }

    private String register(String link) throws IOException, InterruptedException {
        URI registrUri = URI.create(link + "/register");
        HttpRequest registerRequest = HttpRequest.newBuilder()
                .GET()
                .uri(registrUri)
                .build();
        HttpResponse<String> response = client.send(registerRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public void put(String key, String data) throws Exception {
        URI postUri = URI.create(baseURI + "/save/" + key + "?API_TOKEN=" + token);
        System.out.println(postUri);
        HttpRequest requestForSave = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .uri(postUri)
                .build();
        HttpResponse<String> response = client.send(requestForSave,HttpResponse.BodyHandlers.ofString());
        System.out.println("HTTP код ответа: " + response.statusCode());
        System.out.println("Ответ в формате XML: " + response.body());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI getUri = URI.create(baseURI + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest requestForLoad = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpResponse response = client.send(requestForLoad, HttpResponse.BodyHandlers.ofString());
        System.out.println("HTTP код ответа: " + response.statusCode());
        return response.body().toString();
    }
}
