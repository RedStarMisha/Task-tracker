import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ManagerUtil {
    private static Gson gson = new Gson();

    public static boolean existingEpicForSubtask(Map<Integer, AbstractTask> map, AbstractTask subTask) {
        if (subTask instanceof Subtask) {
            Subtask subtaskAfterConvert = (Subtask) subTask;
            return map.containsKey(subtaskAfterConvert.getEpicTaskId());
        }
        return false;
    }

    public static int epicIdOfSubtask (AbstractTask task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return subtask.getEpicTaskId();
        }
        throw new NoSuchElementException("Задача не субтакс");
    }

    public static Class<?> taskTypeParser(String body) throws ClassNotFoundException {
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String className = Formater.firstLettertoUpperCase(jsonObject.get("taskType").getAsString());
        return Class.forName(className);
    }

    public static Map<Integer, AbstractTask> convertToTaskMap(String body) {
        Map<Integer, AbstractTask> map = new HashMap<>();
        JsonElement jsonElement = JsonParser.parseString(body);
        UnaryOperator<JsonElement> jsObjConverter = (js) -> js.getAsJsonObject().get("taskType");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.keySet().stream()
                .map(str -> jsonObject.get(str))
                .map(jEl -> taskTypeChecker(jsObjConverter, jEl))
                .forEach(task -> map.put(task.getTaskId(), task));
        return map;
    }

    public static AbstractTask taskTypeChecker(UnaryOperator<JsonElement> f, JsonElement jsonElement) {
        String taskType = f.apply(jsonElement).getAsString();
        switch (taskType){
            case "TASK":
                return  gson.fromJson(jsonElement, Task.class);
            case "EPICTASK":
                return gson.fromJson(jsonElement, Epictask.class);
            case "SUBTASK":
                return gson.fromJson(jsonElement, Subtask.class);
            default:
                return null;
        }
    }
}
