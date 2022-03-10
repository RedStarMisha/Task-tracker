package taskmodel;

public abstract class AbstractTask {
    protected final String taskName;
    protected final String tastDescription;
    protected final int taskId;
    protected TaskStatus taskStatus;

    public AbstractTask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        this.taskName = taskName;
        this.tastDescription = tastDescription;
        this.taskId = taskId;
        this.taskStatus = tastStatus;
    }

    public AbstractTask(AbstractTask task, TaskStatus tastStatus) {
        this.taskName = task.taskName;
        this.tastDescription = task.tastDescription;
        this.taskId = task.taskId;
        this.taskStatus = tastStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTastDescription() {
        return tastDescription;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
}
