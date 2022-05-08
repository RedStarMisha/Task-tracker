public class Managers  {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() throws Exception {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static InMemoryTaskManager getFileManager() throws Exception {
        FileBacketTaskManager.RECOVER_FROM_FILE = true;
        return new FileBacketTaskManager(getDefaultHistory());
    }

    public static InMemoryTaskManager getFileManagerWithoutRecovery() throws Exception {
        FileBacketTaskManager.RECOVER_FROM_FILE = false;
        return new FileBacketTaskManager(getDefaultHistory());
    }

}
