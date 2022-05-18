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
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
            try {
                String method = httpExchange.getRequestMethod(); //Метод запроса
                String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET); //тело запроса
                String query = httpExchange.getRequestURI().getQuery(); //Параметр в строке запроса
                String[] subPath = httpExchange.getRequestURI().getPath().split("/");

                if (subPath.length < 3 && method.equals("GET")) {
                    respAndCode = new SingletonMap<>(SUCCESSFUL_CODE , manager.getSortedTask().toString());
                }
                switch (subPath[2]) {
                    case "load":
                        respAndCode = loadMethod(method, subPath[3]);
                        break;
                    case "task":
                        respAndCode = taskMethod(query, method, body);
                        break;
                    case "history":
                        respAndCode = historyMethod(method);
                        break;
                    default:
                        respAndCode = new SingletonMap<>(ERROR_CODE, "Действие не удалось");
                }
            } catch (Exception e) {
                System.out.println("Ошибка при обращении к серверу");
            } finally {
                httpExchange.sendResponseHeaders(respAndCode.getKey(), 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(respAndCode.getValue().getBytes(DEFAULT_CHARSET));
                }
            }
        }

        private SingletonMap<Integer, String> taskMethod(String query, String method, String body) throws Exception {
            int id = query == null ? -1 : query.charAt(3) - 48;
            Map<Integer, AbstractTask> localTaskMap = manager.getAllTask();
            switch (method) {
                case "GET":
                    if (id < 0) {
                        return new SingletonMap<>(SUCCESSFUL_CODE, gson.toJson(localTaskMap));
                    }
                    return new SingletonMap<>(SUCCESSFUL_CODE, gson.toJson(manager.getTask(id)));
                case "DELETE":
                    if (id < 0) {
                        manager.clearTaskMap();
                        return new SingletonMap<>(SUCCESSFUL_CODE, "Список задач очищен");
                    }
                    manager.deteteTask(id);
                    return new SingletonMap<>(SUCCESSFUL_CODE, "Задача с id = " + id + " удалена");
                case "POST":
                    if (body.isEmpty() && query.indexOf("status") > 0 && localTaskMap.containsKey(id)) {
                        TaskStatus taskStatus = TaskStatus.valueOf(query.substring(7).toUpperCase());
                        manager.updateTaskStatus(id, taskStatus);
                        return new SingletonMap<>(SUCCESSFUL_CODE, "Обновление статуса задачи "
                                + localTaskMap.get(id).getTaskName() + " id " + id + " выполнено");
                    } else if (id < 0) {
                        Class className = Class.forName(taskTypeParser(body));
                        if (className.equals(Subtask.class)) {
                            Subtask subtask = (Subtask) gson.fromJson(body, className);
                            Predicate<Map<Integer, AbstractTask>> containsId = map ->
                                map.containsKey(subtask.getEpicTaskId());
                            if (!containsId.test(manager.getAllTask())) {
                                return new SingletonMap<>(ERROR_CODE, "Новая задача добавлена");
                            }
                        }
                        AbstractTask task = (AbstractTask) gson.fromJson(body, className);
                        manager.add(task);
                        return new SingletonMap<>(SUCCESSFUL_CODE, "Новая задача добавлена");
                    }
                    return new SingletonMap<>(ERROR_CODE, "Обновить список задач не получилось");
                default:
                    return new SingletonMap<>(ERROR_CODE, "Неизвестный запрос");
            }
        }

        private String taskTypeParser(String body) {
            JsonElement jsonElement = JsonParser.parseString(body);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return Formater.firstLettertoUpperCase(jsonObject.get("taskType").getAsString());
        }

        private SingletonMap<Integer, String> historyMethod(String method) {
            if (method.equals("GET")) {
                return new SingletonMap<>(SUCCESSFUL_CODE, gson.toJson(manager.history()));
            }
            return new SingletonMap<>(ERROR_CODE, "Ошибка при отображении истории вызовов задач");
        }

        private SingletonMap<Integer, String> loadMethod(String method, String key) throws Exception {
                if (key.equals(HTTPTaskManager.TASKMAP_KEY) && method.equals("GET")) {
                    manager = Managers.getHttpTaskManager(path, gson);
                    return new SingletonMap<>(SUCCESSFUL_CODE, "Данные менеджера задач загружены с сервера");
                } else if (key.equals(HTTPTaskManager.HISTORY_KEY) && method.equals("GET")) {
                    manager = Managers.getHttpTaskManager(path, gson);
                    return new SingletonMap<>(SUCCESSFUL_CODE, "Данные менеджера задач загружены с сервера");
                } else {
                    return new SingletonMap<>(ERROR_CODE, "Неизвестная команда");
                }
        }
    }
}
