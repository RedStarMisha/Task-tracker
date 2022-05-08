public class Task extends AbstractTask {

    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus,
                String startTime, long duration) throws Exception {
        super(taskName, tastDescription, taskId, tastStatus, startTime, duration);
    }

    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
    }

    public Task(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
    }

}
