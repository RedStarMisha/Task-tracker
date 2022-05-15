import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.collections4.map.SingletonMap;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {

    private static final int PORT = 8078;
    //private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final int SUCCESSFUL_CODE = 200;
    private final int ERROR_CODE = 400;
    private static Gson gson = new Gson();
    public TaskManager manager;
    String path;
    Charset windows1251 = Charset.forName("Windows-1251");

    public HttpTaskServer(String path) throws Exception {
        this.path = path;
        HTTPTaskManager.RECOVERY = false;
        manager = Managers.getHttpTaskManager(path, gson);
        HTTPTaskManager.RECOVERY = true;
    }


    public void createServer() throws Exception {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();



        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            SingletonMap<Integer, String> respAndCode = new SingletonMap<>();
            try{
                String method = httpExchange.getRequestMethod();
                String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                String query = httpExchange.getRequestURI().getQuery();
                String path = httpExchange.getRequestURI().getPath().substring(7);

                if (path.isEmpty() && method.equals("GET")) {
                    respAndCode = new SingletonMap<>(SUCCESSFUL_CODE , manager.getSortedTask().toString());
                } else if (query != null) {
                    respAndCode = notNullQuery(method, query);
                } else if (query == null) {
                    respAndCode = nullQuery(method, path, body);
                } else {
                    throw new IOException("Неизвестный запрос");
                }
            } catch (Exception e) {
                System.out.println("Действие не удалось");
                respAndCode = new SingletonMap<>(ERROR_CODE, "Действие не удалось");
            } finally {
                httpExchange.sendResponseHeaders(respAndCode.getKey(), 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(respAndCode.getValue().getBytes(DEFAULT_CHARSET));
                }
            }


        }

        private SingletonMap<Integer, String>  notNullQuery(String method, String query) throws Exception {
            int id = query.charAt(3) - 48;
            if (id < 0 || !manager.getAllTask().containsKey(id)) {
                throw new IllegalArgumentException("Такого id не существует");
            }
            switch (method) {
                case "GET":
                    return new SingletonMap<>(SUCCESSFUL_CODE, manager.getTask(id).toString());
                case "DELETE":
                    manager.deteteTask(id);
                    return new SingletonMap<>(SUCCESSFUL_CODE, "Задача удалена");
                case "POST":
                    if (query.indexOf("status") < 0) {
                        throw new IllegalArgumentException("Отсутсвует информация о новом статусе");
                    }
                    TaskStatus taskStatus = TaskStatus.valueOf(query.substring(7).toUpperCase());
                    manager.updateTaskStatus(id, taskStatus);
                    return new SingletonMap<>(SUCCESSFUL_CODE, "Обновление статуса задачи "
                            + manager.getAllTask().get(id).getTaskName() + " id " + id + " выполнено");
                default:

                    return new SingletonMap<>(ERROR_CODE,  "Запрос не обработан");
            }
        }

        private SingletonMap<Integer, String> nullQuery(String method, String subPath, String body)
                throws Exception {
            switch (method) {
                case "GET":
                    if (subPath.equals("history")){
                        return new SingletonMap<>(SUCCESSFUL_CODE, manager.history().toString());
                    } else if (subPath.equals("load")) {
                        manager = Managers.getHttpTaskManager(path, gson);
                    }
                    return new SingletonMap<>(SUCCESSFUL_CODE, manager.getAllTask().toString());
                case "DELETE":
                    manager.clearTaskMap();
                    return new SingletonMap<>(SUCCESSFUL_CODE, "Список задач очищен");
                case "POST":
                    Class className = Class.forName(taskTypeParser(body));
                    AbstractTask task = (AbstractTask) gson.fromJson(body, className);
                    manager.add(task);
                    return new SingletonMap<>(SUCCESSFUL_CODE, "Задача ");
                default:
                    System.out.println("Такого метода нет");
                    return new SingletonMap<>(ERROR_CODE,  "Запрос не обработан");
            }
        }

        private String taskTypeParser(String body) {
            JsonElement jsonElement = JsonParser.parseString(body);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return Formater.firstLettertoUpperCase(jsonObject.get("taskType").getAsString());
        }
    }


}
