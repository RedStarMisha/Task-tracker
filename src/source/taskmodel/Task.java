public class Task extends AbstractTask {

    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus,
                String startTime, long duration) throws Exception {
        super(taskName, tastDescription, taskId, tastStatus, startTime, duration);
    }

    public Task(String taskName, String tastDescription, int taskId, TaskStatus taskStatus) {
        super(taskName, tastDescription, taskId, taskStatus);
    }

    public Task(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
    }
}
