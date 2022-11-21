public class Managers  {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() throws Exception {
        return new InMemoryTaskManager();
    }

    public static TaskManager getSerializableTaskManager(String path) throws Exception {
        FileBacketTaskManager.RECOVERY = true;
        return new HTTPTaskManager(path);
    }

    public static InMemoryTaskManager getSerialisableTaskManagerWithoutRecovery() throws Exception {
        FileBacketTaskManager.RECOVERY = false;
        return new FileBacketTaskManager();
    }

}
