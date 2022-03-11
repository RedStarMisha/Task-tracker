package taskmodel;

public class SubTask extends AbstractTask{
    private final int epicTaskId;

    public SubTask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus, int epicTaskId) {
        super(taskName, tastDescription, taskId, tastStatus);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        SubTask subTask = (SubTask) task;
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
        return "SubTask эпика id '" + getEpicTaskId() + "'" + "{"
                + "taskName='" + taskName + '\''
                + ", taskStatus='" + taskStatus + '\''
                + '}';
    }

    @Override
    public Class<?> getType() {
        return this.getType();
    }
}
