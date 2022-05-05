public class Task extends AbstractTask {

    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus,
                String startTime, long duration) throws Exception {
        super(taskName, tastDescription, taskId, tastStatus, startTime, duration);
        taskType = TaskType.Task;
    }

    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
        taskType = TaskType.Task;
    }

    public Task(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        taskType = TaskType.Task;
    }

}
