public abstract class UserTask {

    private final String taskName;
    private final String taskDescription;
    protected TaskStatus taskStatus;
    private final TaskType taskType;


    public UserTask(String taskName, String taskDescription, TaskType taskType) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskType = taskType;
        taskStatus = TaskStatus.NEW;
    }
}
