import com.google.gson.*;

import java.io.IOException;
import java.util.stream.IntStream;

public class HTTPTaskManager extends FileBacketTaskManager{
    KVTaskClient kvTaskClient;
    Gson gson;
    public final static String TASKMAP_KEY = "taskmap";
    public final static String HISTORY_KEY = "history";


    public HTTPTaskManager(String path) throws Exception {
        super(null);
        this.gson = ManagerUtil.GSON;
        kvTaskClient = new KVTaskClient(path);
        if (RECOVERY) {
            loadData(TASKMAP_KEY);
            loadData(HISTORY_KEY);
        }
    }

    private void loadData(String key) throws IOException, InterruptedException {
        String data = kvTaskClient.load(key);
        if (data.length() > 2) {
            JsonElement jsonElementTaskMap = JsonParser.parseString(data);
            switch (key) {
                case TASKMAP_KEY:
                    JsonObject jsonObjectTaskMap = jsonElementTaskMap.getAsJsonObject();
                    jsonObjectTaskMap.keySet().stream()
                            .map(str -> jsonObjectTaskMap.get(str))
                            .map(jEl -> ManagerUtil.taskTypeChecker(jEl))
                            .forEach(task -> taskMap.put(task.getTaskId(), task));
                    break;
                case HISTORY_KEY:
                    JsonArray jsonArray = jsonElementTaskMap.getAsJsonArray();
                    IntStream.range(0, jsonArray.size()).mapToObj(i -> jsonArray.get(i))
                            .map(ob -> ManagerUtil.taskTypeChecker(ob))
                            .forEach(task -> getHistoryManager().addTask(task));
                    break;
            }
        }
    }

    @Override
    public void save() throws Exception {
        String map = gson.toJson(getAllTask());
        kvTaskClient.put(TASKMAP_KEY, map);
        String hist = gson.toJson(history());
        kvTaskClient.put(HISTORY_KEY, hist);
    }
}
