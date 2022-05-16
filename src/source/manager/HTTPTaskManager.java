import com.google.gson.*;

import java.io.IOException;
import java.util.List;

public class HTTPTaskManager extends FileBacketTaskManager{
    KVTaskClient kvTaskClient;
    Gson gson;


    public HTTPTaskManager(HistoryManager historyManager, String path, Gson gson) throws Exception {
        super(historyManager, null);
        kvTaskClient = new KVTaskClient(path, gson);
        this.gson = gson;
        if (RECOVERY) {
            loadTasks();
            loadHistory();
        }
    }

    @Override
    public void save() throws Exception {
        String map = gson.toJson(this.getAllTask());
        kvTaskClient.put("map", map);
        String hist = gson.toJson(history());
        kvTaskClient.put("hist", hist); //был history
    }

    public void loadTasks() throws IOException, InterruptedException {
        String tasks = kvTaskClient.load("map");
        if (!tasks.isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(tasks);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (String key : jsonObject.keySet()) {
                AbstractTask task = taskTypeChecker(jsonObject.get(key));
                this.taskMap.put(Integer.parseInt(key), task);
            }
        }
    }

    public void loadHistory() throws IOException, InterruptedException {
        String history = kvTaskClient.load("history");
        if (!history.isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(history);
            JsonArray jsonObject = jsonElement.getAsJsonArray();
            for (JsonElement task: jsonObject) {
                this.getHistoryManager().addTask(taskTypeChecker(task));
            }
        }
    }

    private AbstractTask taskTypeChecker(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String taskType = jsonObject.get("taskType").getAsString();
        System.out.println(taskType);
        switch (taskType){
            case "TASK":
                Task task = gson.fromJson(jsonElement, Task.class);
                return task;
            case "EPIC":
                return gson.fromJson(jsonElement, EpicTask.class);
            case "SUBTASK":
                return gson.fromJson(jsonElement, SubTask.class);
            default:
                return null;
        }
    }


}
