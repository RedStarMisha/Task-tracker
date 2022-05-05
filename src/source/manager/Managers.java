public class Managers  {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() throws Exception {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static InMemoryTaskManager getFileManager(boolean shouldRecovery) throws Exception {
        return new FileBacketTaskManager(getDefaultHistory(), shouldRecovery);
    }

}
