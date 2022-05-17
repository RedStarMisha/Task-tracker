import com.google.gson.*;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class HTTPTaskManager extends FileBacketTaskManager{
    KVTaskClient kvTaskClient;
    Gson gson;


    public HTTPTaskManager(HistoryManager historyManager, String path, Gson gson) throws Exception {
        super(historyManager, null);
        kvTaskClient = new KVTaskClient(path, gson);
        this.gson = gson;
        if (RECOVERY) {
            loadData("map");
            loadData("history");
        }
    }

    private void loadData(String key) throws IOException, InterruptedException {
        String data = kvTaskClient.load(key);
        if (!data.isEmpty()) {
            JsonElement jsonElement = JsonParser.parseString(data);
            UnaryOperator<JsonElement> jsObjConverter = (js) -> js.getAsJsonObject().get("taskType");
            switch (data) {
                case "map":
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    jsonObject.keySet().stream()
                            .map(str -> jsonObject.get(str))
                            .map(jEl -> taskTypeChecker(jsObjConverter, jEl))
                            .forEach(task -> taskMap.put(Integer.parseInt(key), task));
                    break;
                case "history":
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    IntStream.range(0, jsonArray.size()).mapToObj(i -> jsonArray.get(i))
                            .map(ob -> taskTypeChecker(jsObjConverter, ob))
                            .forEach(task -> getHistoryManager().addTask(task));
            }
        }
    }

    @Override
    public void save() throws Exception {
        String map = gson.toJson(getAllTask());
        kvTaskClient.put("map", map);
        String hist = gson.toJson(history());
        kvTaskClient.put("history", hist);
    }

    private AbstractTask taskTypeChecker(UnaryOperator<JsonElement> f, JsonElement jsonElement) {
        String taskType = f.apply(jsonElement).getAsString();
        switch (taskType){
            case "TASK":
                return  gson.fromJson(jsonElement, Task.class);
            case "EPIC":
                return gson.fromJson(jsonElement, EpicTask.class);
            case "SUBTASK":
                return gson.fromJson(jsonElement, SubTask.class);
            default:
                return null;
        }
    }


}
