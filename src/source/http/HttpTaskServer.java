import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {

    private static final int PORT = 8080;
    //private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new Gson();
    public static InMemoryTaskManager manager;
    Charset windows1251 = Charset.forName("Windows-1251");


    public void createServer() throws Exception {

        HttpServer httpServer = HttpServer.create();
        manager = Managers.getFileManagerWithoutRecovery();
        try {
            manager.add(new Task("Убрать со стола",
                    "Убрать грязную посуду, стереть со стола", manager.setIdNumeration(), TaskStatus.NEW,
                    "15-01-2022, 11:50",10));
            manager.add(new Task("Убрать жолпа",
                    "Мент", manager.setIdNumeration(), TaskStatus.NEW,
                    "15-01-2022, 11:50",10));
            manager.add(new Task("Убрать со стола",
                    "Грязь", manager.setIdNumeration(), TaskStatus.NEW,
                    "15-01-2022, 11:50",10));
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            int id = 0;
            String status = null;
            int reactionCode = 200;
            String response = "Не удалось выполнить действие";

            try{

                String method = httpExchange.getRequestMethod();
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

                //для понимания запрашивается ли history или нет
                String [] arrayPath = httpExchange.getRequestURI().getPath().split("/");
                String query = httpExchange.getRequestURI().getQuery();

                if (query != null) {
                    id = (query.indexOf("id")>= 0)? query.charAt(query.indexOf("id") + 3) - 48: -1;
                    System.out.println(id);
                    status = query.indexOf("status")>=0? query.substring(query.indexOf("status") + 7): null;
                }
                if (arrayPath.length < 3 && method.equals("GET")) {
                    response = manager.getSortedTask().toString();
                } else {
                    switch (method) {
                        case "GET":
                            if (query == null) {
                                response = manager.getAllTask().toString();
                                System.out.println("с URI");
                                break;
                            }
                            response = manager.getTask(id).toString();
                            break;
                        case "DELETE":
                            if (query == null) {
                                manager.clearTaskMap();
                                response = "Спикок задач очищен";
                                break;
                            }
                            manager.deteteTask(id);
                            response = "Задача удалена";
                            break;
                        case "POST":
                            if (status != null) {
                                TaskStatus newStatus = gson.fromJson(status.toUpperCase(),TaskStatus.class);
                                System.out.println(newStatus);
                                manager.updateTaskStatus(id,newStatus);
                                response = "Обновление статуса задачи " + manager.getAllTask().get(id).getTaskName()
                                        + " id " + id + " выполнено";
                            }
                    }
            }

            } catch (Exception e) {
                System.out.println("Действие не удалось");
            }








            httpExchange.sendResponseHeaders(reactionCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes(DEFAULT_CHARSET));
            }
            }
        }
    }
