import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
import java.util.function.UnaryOperator;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {

    private static final int PORT = 8078;
    private final int SUCCESSFUL_CODE = 200;
    private final int CREATED_CODE = 201;
    private final int ERROR_CODE = 400;
    private static Gson gson = new Gson();
    public TaskManager manager;
    String path;
    private HttpServer httpServer;
    Charset windows1251 = Charset.forName("Windows-1251");

    public HttpTaskServer(String path) throws Exception {
        this.path = path;
        HTTPTaskManager.RECOVERY = false;
        manager = Managers.getHttpTaskManager(path, gson);
        HTTPTaskManager.RECOVERY = true;
    }


    public void createServer() throws Exception {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        System.out.println("Сервер остановлен");
        httpServer.stop(0);
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
                    if (query == null) {
                        return new SingletonMap<>(SUCCESSFUL_CODE, gson.toJson(localTaskMap));
                    } else if (localTaskMap.containsKey(id)) {
                        return new SingletonMap<>(SUCCESSFUL_CODE, gson.toJson(manager.getTask(id)));
                    }
                    return new SingletonMap<>(ERROR_CODE, "Задачи с таким id нет");
                case "DELETE":
                    if (query == null) {
                        manager.clearTaskMap();
                        return new SingletonMap<>(SUCCESSFUL_CODE, "Список задач очищен");
                    } else if (localTaskMap.containsKey(id)) {
                        manager.deteteTask(id);
                        return new SingletonMap<>(SUCCESSFUL_CODE, "Задача с id = " + id + " удалена");
                    }
                    return new SingletonMap<>(ERROR_CODE, "Задачи с таким id нет");
                case "POST":
                    if (query != null && localTaskMap.containsKey(id)) {
                        int indStatusInQuery = query.lastIndexOf("=") + 1;
                        TaskStatus taskStatus = TaskStatus.valueOf(query.substring(indStatusInQuery).toUpperCase());
                        manager.updateTaskStatus(id, taskStatus);
                        return new SingletonMap<>(SUCCESSFUL_CODE, "Обновление статуса задачи id = " + id + " выполнено");
                    } else if (id < 0 && !body.isEmpty() ) {
                        JsonElement jsonElement = JsonParser.parseString(body);                                             //////удалить эту херь
                        UnaryOperator<JsonElement> jsObjConverter = (js) -> js.getAsJsonObject().get("taskType");
                        AbstractTask task = ManagerUtil.taskTypeChecker(jsObjConverter, jsonElement);
                        if (task instanceof Subtask) {
                            if (!ManagerUtil.existingEpicForSubtask(manager.getAllTask(), task)) {
                                return new SingletonMap<>(ERROR_CODE, "Сначала добавьте эпик");
                            }
                        } else if (task == null) {
                            return new SingletonMap<>(ERROR_CODE, "Сначала добавьте эпик");
                        }
                        manager.add(task);
                        return new SingletonMap<>(CREATED_CODE, "Новая задача добавлена");
                    }
                    return new SingletonMap<>(ERROR_CODE, "Обновить список задач не получилось");
                default:
                    return new SingletonMap<>(ERROR_CODE, "Неизвестный запрос");
            }
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
