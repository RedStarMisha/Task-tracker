import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.AbstractMap;
import java.util.Map;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {

    private static final int PORT = 8078;
    private final int SUCCESSFUL_CODE = 200;
    private final int CREATED_CODE = 201;
    private final int ERROR_CODE = 400;
    private Gson gson = ManagerUtil.GSON;
    public TaskManager manager;
    private String path;
    private HttpServer httpServer;

    public HttpTaskServer(String path) throws Exception {
        this.path = path;
        HTTPTaskManager.RECOVERY = false;
        manager = Managers.getSerializableTaskManager(path);
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
            Map.Entry<Integer, String> respAndCode = new AbstractMap.SimpleEntry<>(null, null);
            try {
                String method = httpExchange.getRequestMethod(); //Метод запроса
                String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET); //тело запроса
                String query = httpExchange.getRequestURI().getQuery(); //Параметр в строке запроса
                String[] subPath = httpExchange.getRequestURI().getPath().split("/");

                if (subPath.length < 3 && method.equals("GET")) {
                    respAndCode = new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, gson.toJson(manager.getSortedTask()));
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
                        respAndCode = new AbstractMap.SimpleEntry<>(ERROR_CODE, "Действие не удалось");
                }
            } catch (Exception e) {
                System.out.println("Ошибка при обращении к серверу: " + e.getMessage());
            } finally {
                httpExchange.sendResponseHeaders(respAndCode.getKey(), 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(respAndCode.getValue().getBytes(DEFAULT_CHARSET));
                }
            }
        }

        private Map.Entry<Integer, String> loadMethod(String method, String key) throws Exception {
            if (key.equals(HTTPTaskManager.TASKMAP_KEY) && method.equals("GET")) {
                manager = Managers.getSerializableTaskManager(path);
                return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, "Данные менеджера задач загружены с сервера");
            } else if (key.equals(HTTPTaskManager.HISTORY_KEY) && method.equals("GET")) {
                manager = Managers.getSerializableTaskManager(path);
                return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, "Данные менеджера задач загружены с сервера");
            } else {
                return new AbstractMap.SimpleEntry<>(ERROR_CODE, "Неизвестная команда");
            }
        }

        private AbstractMap.SimpleEntry<Integer, String> taskMethod(String query, String method, String body) throws Exception {
            int id = query == null ? -1 : query.charAt(3) - 48;
            Map<Integer, AbstractTask> localTaskMap = manager.getAllTask();
            switch (method) {
                case "GET":
                    if (query == null) {
                        return requestGetAllTask(localTaskMap);
                    } else if (localTaskMap.containsKey(id)) {
                        return requestGetSingleTask(id);
                    }
                    return new AbstractMap.SimpleEntry<>(ERROR_CODE, "Задачи с таким id нет");
                case "DELETE":
                    if (query == null) {
                        return requestDeleteAllTask();
                    } else if (localTaskMap.containsKey(id)) {
                        return requestDeleteSingleTask(id);
                    }
                    return new AbstractMap.SimpleEntry<>(ERROR_CODE, "Удаление не удалось");
                case "POST":
                    if (query != null && localTaskMap.containsKey(id)) {
                        return requestUpdateTaskStatus(query, id);
                    } else if (id < 0 && !body.isEmpty()) {
                        return requestAddNewTask(body, localTaskMap);
                    }
                    return new AbstractMap.SimpleEntry<>(ERROR_CODE, "Обновить список задач не получилось");
                default:
                    return new AbstractMap.SimpleEntry<>(ERROR_CODE, "Неизвестный запрос");
            }
        }

        private AbstractMap.SimpleEntry<Integer, String> requestAddNewTask(String body, Map<Integer, AbstractTask> localTaskMap)
                throws ManagerSaveException, AddEmptyElementException, ExceptionTaskIntersection {
            JsonElement jsonTask = JsonParser.parseString(body);
            AbstractTask task = ManagerUtil.taskTypeChecker(jsonTask);
            if (task instanceof Subtask && !ManagerUtil.existingEpicForSubtask(localTaskMap, task)) {
                return new AbstractMap.SimpleEntry<>(ERROR_CODE, "Сначала добавьте эпик");
            } else if (task == null) {
                return new AbstractMap.SimpleEntry<>(ERROR_CODE, "Неизвестный тип задач");
            }
            manager.add(task);
            return new AbstractMap.SimpleEntry<>(CREATED_CODE, "Новая задача добавлена");
        }

        private AbstractMap.SimpleEntry<Integer, String> requestUpdateTaskStatus(String query, int id) throws ManagerSaveException {
            int indxStatusInQuery = query.lastIndexOf("=") + 1;
            TaskStatus taskStatus = TaskStatus.valueOf(query.substring(indxStatusInQuery).toUpperCase());
            manager.updateTaskStatus(id, taskStatus);
            return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, "Обновление статуса задачи id = " + id + " выполнено");
        }

        private AbstractMap.SimpleEntry<Integer, String> requestDeleteSingleTask(int id) throws IOException, ManagerSaveException {
            manager.deteteTask(id);
            return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, "Задача с id = " + id + " удалена");
        }

        private AbstractMap.SimpleEntry<Integer, String> requestDeleteAllTask() {
            manager.clearTaskMap();
            return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, "Список задач очищен");
        }

        private AbstractMap.SimpleEntry<Integer, String> requestGetSingleTask(int id) throws Exception {
            return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, gson.toJson(manager.getTask(id)));
        }

        private AbstractMap.SimpleEntry<Integer, String> requestGetAllTask(Map<Integer, AbstractTask> localTaskMap) {
            if (localTaskMap.isEmpty()) {
                return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, "Список задач пуст");
            }
            return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, gson.toJson(localTaskMap));
        }


        private AbstractMap.SimpleEntry<Integer, String> historyMethod(String method) {
            if (method.equals("GET")) {
                return new AbstractMap.SimpleEntry<>(SUCCESSFUL_CODE, gson.toJson(manager.history()));
            }
            return new AbstractMap.SimpleEntry<>(ERROR_CODE, "Ошибка при отображении истории вызовов задач");
        }
    }
}
