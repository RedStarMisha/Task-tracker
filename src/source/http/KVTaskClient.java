import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    String baseURI;
    private final String token;
    final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();


    public KVTaskClient(String link) throws Exception {
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
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(requestForSave , HttpResponse.BodyHandlers.ofString());
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

//    private String createTask() throws Exception {
//        TaskManager manager = Managers.getHttpTaskManager("http://localhost:8080", gson);
//        manager.add(new Task("Поменять лампочку на кухне",
//                "Выкрутить старую лампочку, правильно ее утилизировать и вкрутить новую",
//                manager.setIdNumeration(), TaskStatus.NEW, "12-01-2022, 16:00",10));
//        manager.add(new EpicTask("Убраться на кухне",
//                "Необходимо провести полную уборку кухни", manager.setIdNumeration(), TaskStatus.NEW,
//                "15-01-2022, 11:00",20));
//        manager.add(new SubTask("Помыть посуду", "Посуда должна быть чистой",
//                manager.setIdNumeration(), TaskStatus.IN_PROGRESS, "12-01-2022, 17:05",40,
//                2));
//        manager.add(new SubTask("Убрать со стола",
//                "Убрать грязную посуду, стереть со стола", manager.setIdNumeration(), TaskStatus.DONE,
//                "15-01-2022, 11:50",10,2));
//        manager.add(new EpicTask("Убраться в спальне",
//                "Провести быструю уборку в спальной комнате", manager.setIdNumeration(), TaskStatus.NEW));
//        manager.add(new SubTask("Убрать постель",
//                "Убрать одеяла и застелить постель", manager.setIdNumeration(), TaskStatus.NEW,
//                "14-01-2022, 09:00",5, 5));
//        manager.add(new EpicTask("Приготовить ужин",
//                "Приготовить ужин на двоих", manager.setIdNumeration(), TaskStatus.NEW));
//        return gson.toJson(manager);
//    }
}
