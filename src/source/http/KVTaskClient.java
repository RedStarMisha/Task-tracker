import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    String baseURI;
    //private final String token;
    HttpClient client = HttpClient.newHttpClient();


    public KVTaskClient(String link) throws IOException, InterruptedException {
        baseURI = link;
        //token = register(link);
        register(link);
    }

    private void register(String link) throws IOException, InterruptedException {
        URI registrUri = URI.create(link + "/register");
        HttpRequest registerRequest = HttpRequest.newBuilder()
                .GET()
                .uri(registrUri)
                .build();
        HttpResponse<String> response = client.send(registerRequest, HttpResponse.BodyHandlers.ofString());
        String id = response.body();
        System.out.println(id);
    }
}
