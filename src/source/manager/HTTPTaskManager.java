import com.google.gson.*;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class HTTPTaskManager extends FileBacketTaskManager{
    KVTaskClient kvTaskClient;
    Gson gson;
    public final static String TASKMAP_KEY = "taskmap";
    public final static String HISTORY_KEY = "history";


    public HTTPTaskManager(String path, Gson gson) throws Exception {
        super(null);
        kvTaskClient = new KVTaskClient(path, gson);
        this.gson = gson;
        if (RECOVERY) {
            loadData(TASKMAP_KEY);
            loadData(HISTORY_KEY);
        }
    }

    private void loadData(String key) throws IOException, InterruptedException {
        String data = kvTaskClient.load(key);
        if (data.length() > 2) {
            JsonElement jsonElement = JsonParser.parseString(data);
            UnaryOperator<JsonElement> jsObjConverter = (js) -> js.getAsJsonObject().get("taskType");
            switch (key) {
                case TASKMAP_KEY:
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    jsonObject.keySet().stream()
                            .map(str -> jsonObject.get(str))
                            .map(jEl -> ManagerUtil.taskTypeChecker(jsObjConverter, jEl))
                            .forEach(task -> taskMap.put(task.getTaskId(), task));
                    break;
                case HISTORY_KEY:
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    IntStream.range(0, jsonArray.size()).mapToObj(i -> jsonArray.get(i))
                            .map(ob -> ManagerUtil.taskTypeChecker(jsObjConverter, ob))
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
