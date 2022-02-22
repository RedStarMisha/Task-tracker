package TaskPackage;

public class Task {
    protected String taskName;
    protected String tastDescription;
    protected int taskId;
    protected TaskStatus taskStatus;

    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        this.taskName = taskName;
        this.tastDescription = tastDescription;
        this.taskId = taskId;
        this.taskStatus = tastStatus;
    }

    public Task(Task task, TaskStatus tastStatus) {
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

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
