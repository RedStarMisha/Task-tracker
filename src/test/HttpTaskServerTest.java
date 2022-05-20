import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    Gson gson = new Gson();
    HttpTaskServer taskServer;
    KVServer kvServer = new KVServer();
    String link = "http://localhost:8078/tasks";
    HttpClient client;
    HttpResponse<String> response;
    private String simpleTask = "{\n" +
            "\t\"taskName\":\"задача SimpleTask\",\n" +
            "\t\t\"taskDescription\":\"описание\",\n" +
            "\t\t\"taskId\":1,\n" +
            "\t\t\"taskStatus\":\"NEW\",\n" +
            "\t\t\"taskType\":\"TASK\",\n" +
            "\t\t\"duration\":{\n" +
            "\t\t\t\t\t\t\t\t\"seconds\":600,\n" +
            "\t\t\t\t\t\t\t\t\"nanos\":0},\n" +
            "\t\t\"startTime\":{\n" +
            "\t\t\t\"date\":{\"year\":2022,\"month\":1,\"day\":12},\n" +
            "\t\t  \"time\":{\"hour\":16,\"minute\":0,\"second\":0,\"nano\":0}\n" +
            "\t\t}\n" +
            "}";
    private String epicTask = "{\n" +
            "\t\"taskName\":\"задача EpicTask\",\n" +
            "\t\t\"taskDescription\":\"описание\",\n" +
            "\t\t\"taskId\":2,\n" +
            "\t\t\"taskStatus\":\"NEW\",\n" +
            "\t\t\"taskType\":\"EPICTASK\",\n" +
            "\t\t\"duration\":{\n" +
            "\t\t\t\t\t\t\t\t\"seconds\":600,\n" +
            "\t\t\t\t\t\t\t\t\"nanos\":0},\n" +
            "\t\t\"startTime\":{\n" +
            "\t\t\t\"date\":{\"year\":2022,\"month\":1,\"day\":12},\n" +
            "\t\t  \"time\":{\"hour\":16,\"minute\":0,\"second\":0,\"nano\":0}\n" +
            "\t\t},\n" +
            "\t\"subTaskListId\":[]\n" +
            "\t\n" +
            "}";

    private String subTask1 = "{\n" +
            "\t\"taskName\":\"задача Subtask1\",\n" +
            "\t\t\"taskDescription\":\"описание\",\n" +
            "\t\t\"taskId\":3,\n" +
            "\t\t\"taskStatus\":\"NEW\",\n" +
            "\t\t\"taskType\":\"SUBTASK\",\n" +
            "\t\t\"duration\":{\n" +
            "\t\t\t\t\t\t\t\t\"seconds\":600,\n" +
            "\t\t\t\t\t\t\t\t\"nanos\":0},\n" +
            "\t\t\"startTime\":{\n" +
            "\t\t\t\"date\":{\"year\":2022,\"month\":1,\"day\":11},\n" +
            "\t\t  \"time\":{\"hour\":16,\"minute\":0,\"second\":0,\"nano\":0}\n" +
            "\t\t},\n" +
            "\t\"epicTaskId\": 2\n" +
            "}";
    private String subTask2 = "{\n" +
            "\t\"taskName\":\"задача Subtask2\",\n" +
            "\t\t\"taskDescription\":\"описание\",\n" +
            "\t\t\"taskId\":4,\n" +
            "\t\t\"taskStatus\":\"NEW\",\n" +
            "\t\t\"taskType\":\"SUBTASK\",\n" +
            "\t\t\"duration\":{\n" +
            "\t\t\t\t\t\t\t\t\"seconds\":600,\n" +
            "\t\t\t\t\t\t\t\t\"nanos\":0},\n" +
            "\t\t\"startTime\":{\n" +
            "\t\t\t\"date\":{\"year\":2022,\"month\":1,\"day\":15},\n" +
            "\t\t  \"time\":{\"hour\":16,\"minute\":0,\"second\":0,\"nano\":0}\n" +
            "\t\t},\n" +
            "\t\"epicTaskId\": 2\n" +
            "}";

    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    void startServer() throws Exception {
        kvServer.start();
        taskServer = new HttpTaskServer("http://localhost:8080");
        taskServer.createServer();
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    @AfterEach
    void stopServer() {
        taskServer.stop();
        kvServer.stop();

    }

    private HttpRequest addNewTask(String body) {
        URI uri = URI.create(link + "/task");
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(uri)
                .build();
    }

    private HttpRequest updateStatus(String id, String status) {
        URI uri = URI.create(link + "/task?id=" + id + "&status=" + status);
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(uri)
                .build();
    }

    private HttpRequest getTask(int id) {
        URI uri = URI.create(link + "/task?id=" + id);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
    }

    private HttpRequest getAllTask() {
        URI uri = URI.create(link + "/task");
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
    }

    private HttpRequest deleteTask(String id) {
        URI uri = URI.create(link + "/task?id=" + id);
        return HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
    }

    private HttpRequest deleteAllTask() {
        URI uri = URI.create(link + "/task");
        return HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
    }

    private HttpRequest getSortedSet() {
        URI uri = URI.create(link);
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
    }


    @Test
    void shouldAddNewSimpleTaskWhenSendPost() throws IOException, InterruptedException {
        response = client.send(addNewTask(simpleTask), HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals("Новая задача добавлена", response.body());
    }

    @Test
    void shouldThrowErrorWhenAddSubtaskBeforeEpic() throws IOException, InterruptedException {
        response = client.send(addNewTask(subTask1), HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Сначала добавьте эпик", response.body());
    }

    @Test
    void shouldThrowErrorWhenAddTaskWithEmptyBody() throws IOException, InterruptedException {
        response = client.send(addNewTask(""), HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Обновить список задач не получилось", response.body());
    }

    @Test
    void shouldGetSimpleTask() throws IOException, InterruptedException {
        response = client.send(addNewTask(simpleTask), HttpResponse.BodyHandlers.ofString());
        response = client.send(getTask(1), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task task = gson.fromJson(simpleTask, Task.class);
        Task returnedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task, returnedTask);
    }

    @Test
    void shouldGetTaskFromEmptyMap() throws IOException, InterruptedException {
        response = client.send(getTask(1), HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Задачи с таким id нет", response.body());
    }

    @Test
    void shouldGetTaskWithNegativeId() throws IOException, InterruptedException {
        response = client.send(getTask(-3), HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Задачи с таким id нет", response.body());
    }

    @Test
    void shouldGetAllTask() throws IOException, InterruptedException {
        response = client.send(addNewTask(epicTask), HttpResponse.BodyHandlers.ofString());
        response = client.send(addNewTask(subTask1), HttpResponse.BodyHandlers.ofString());
        response = client.send(getAllTask(), HttpResponse.BodyHandlers.ofString());
        Map<Integer, AbstractTask> returnedMap = ManagerUtil.convertToTaskMap(response.body());
        Epictask epictask = gson.fromJson(epicTask, Epictask.class);
        Subtask subtask = gson.fromJson(subTask1, Subtask.class);
        Map<Integer, AbstractTask> localMap = new HashMap<>();
        localMap.put(2, epictask);
        localMap.put(3, subtask);
        assertEquals(localMap, returnedMap);
    }

    @Test
    void shouldGetAllTaskWithEmptyTaskMap() throws IOException, InterruptedException {
        response = client.send(getAllTask(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Список задач пуст", response.body());
    }

    @Test
    void shouldDeleteTask() throws IOException, InterruptedException {
        response = client.send(addNewTask(simpleTask), HttpResponse.BodyHandlers.ofString());
        response = client.send(deleteTask("1"), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Задача с id = 1 удалена", response.body());
        response = client.send(getTask(1), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void shouldDeleteTaskWithNonExistentId() throws IOException, InterruptedException {
        response = client.send(deleteTask("1"), HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Удаление не удалось", response.body());
        response = client.send(deleteTask("-1"), HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Удаление не удалось", response.body());
    }

    @Test
    void shouldDeleteAllTask() throws IOException, InterruptedException {
        response = client.send(addNewTask(simpleTask), HttpResponse.BodyHandlers.ofString());
        response = client.send(addNewTask(epicTask), HttpResponse.BodyHandlers.ofString());
        response = client.send(addNewTask(subTask1), HttpResponse.BodyHandlers.ofString());
        response = client.send(deleteAllTask(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Список задач очищен", response.body());
        response = client.send(getAllTask(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void shouldUpdateStatusForSimpleTask() throws IOException, InterruptedException {
        response = client.send(addNewTask(simpleTask), HttpResponse.BodyHandlers.ofString());
        response = client.send(updateStatus("1", "in_progress"), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Обновление статуса задачи id = 1 выполнено", response.body());
    }

    @Test
    void shouldUpdateStatusWithNonExistentId() throws IOException, InterruptedException {
        response = client.send(updateStatus("1", "in_progress"), HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertEquals("Обновить список задач не получилось", response.body());
    }

    @Test
    void shouldGetSortedSetTask() throws IOException, InterruptedException {
        TreeSet<AbstractTask> sortedTask = new TreeSet<>((o1, o2) ->
                Comparator.comparing(AbstractTask::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(AbstractTask::getTaskId).compare(o1, o2));
        response = client.send(addNewTask(simpleTask), HttpResponse.BodyHandlers.ofString());
        response = client.send(addNewTask(epicTask), HttpResponse.BodyHandlers.ofString());
        response = client.send(addNewTask(subTask1), HttpResponse.BodyHandlers.ofString());
        response = client.send(addNewTask(subTask2), HttpResponse.BodyHandlers.ofString());
        response = client.send(getSortedSet(), HttpResponse.BodyHandlers.ofString());
        TaskSorter.add(sortedTask, ManagerUtil.taskTypeChecker(JsonParser.parseString(simpleTask)));
        TaskSorter.add(sortedTask, ManagerUtil.taskTypeChecker(JsonParser.parseString(epicTask)));
        TaskSorter.add(sortedTask, ManagerUtil.taskTypeChecker(JsonParser.parseString(subTask1)));
        TaskSorter.add(sortedTask, ManagerUtil.taskTypeChecker(JsonParser.parseString(subTask2)));
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(sortedTask), response.body());
    }
}