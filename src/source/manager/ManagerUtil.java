import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ManagerUtil {
    public final static Gson GSON = new Gson();

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
        JsonElement jsonElementTaskMap = JsonParser.parseString(body);
        JsonObject jsonObjectTaskMap = jsonElementTaskMap.getAsJsonObject();
        jsonObjectTaskMap.keySet().stream()
                .map(str -> jsonObjectTaskMap.get(str))
                .map(jEl -> taskTypeChecker(jEl))
                .forEach(task -> map.put(task.getTaskId(), task));
        return map;
    }

    public static AbstractTask taskTypeChecker(JsonElement singleJsonElement) {
        JsonObject jsonObjectTask = singleJsonElement.getAsJsonObject();
        String taskType = jsonObjectTask.get("taskType").getAsString();
        switch (taskType){
            case "TASK":
                return  GSON.fromJson(singleJsonElement, Task.class);
            case "EPICTASK":
                return GSON.fromJson(singleJsonElement, Epictask.class);
            case "SUBTASK":
                return GSON.fromJson(singleJsonElement, Subtask.class);
            default:
                return null;
        }
    }
}
