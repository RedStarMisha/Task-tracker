package TaskPackage;

public class Task {
    public String taskName;
    public String tastDescription;
    public int taskId;
    public TaskStatus taskStatus;

    public Task(String taskName, String tastDescription, int taskId) {
        this.taskName = taskName;
        this.tastDescription = tastDescription;
        this.taskId = taskId;
        taskStatus = TaskStatus.NEW;
    }


}
