import com.google.gson.Gson;

public class Managers  {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() throws Exception {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TaskManager getHttpTaskManager(String path, Gson gson) throws Exception {
        return new HTTPTaskManager(getDefaultHistory(), path, gson);
    }

    public static InMemoryTaskManager getFileManager() throws Exception {
        FileBacketTaskManager.RECOVERY = true;
        return new FileBacketTaskManager(getDefaultHistory());
    }

    public static InMemoryTaskManager getFileManagerWithoutRecovery() throws Exception {
        FileBacketTaskManager.RECOVERY = false;
        return new FileBacketTaskManager(getDefaultHistory());
    }

}
