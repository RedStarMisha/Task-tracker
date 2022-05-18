public class Subtask extends AbstractTask{
    private final int epicTaskId;

    public Subtask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus,
                   String startTime, long duration, int epicTaskId) throws Exception {
        super(taskName, tastDescription, taskId, tastStatus, startTime, duration);
        this.epicTaskId = epicTaskId;
    }

    public Subtask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus, int epicTaskId) {
        super(taskName, tastDescription, taskId, tastStatus);
        this.epicTaskId = epicTaskId;
    }

    public Subtask(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        Subtask subTask = (Subtask) task;
        this.epicTaskId = subTask.getEpicTaskId();
    }

    /**
     В Сабтаске теперь хранится только id Эпика
     */
    public int getEpicTaskId() {
        return epicTaskId;
    }

    @Override
    public String toString() {
        return super.toString() + "." + epicTaskId;
    }
}
