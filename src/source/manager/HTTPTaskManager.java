import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String history = gson.toJson(this.history());
        kvTaskClient.put("history",history);
    }

    public void loadTasks() throws IOException, InterruptedException, ManagerSaveException, AddEmptyElementException
            , ExceptionTaskIntersection {
        String tasks = kvTaskClient.load("map");
        Map<Integer,AbstractTask> mapForTask = gson.fromJson(tasks, HashMap.class);

        for (AbstractTask task : mapForTask.values()) {
            add(task);
        }
    }

    public void loadHistory() throws IOException, InterruptedException {
        String history = kvTaskClient.load("history");
        List<AbstractTask> historyList = gson.fromJson(history, ArrayList.class);
        for (AbstractTask task: historyList) {
            this.getHistoryManager().addTask(task);
        }
    }
}
