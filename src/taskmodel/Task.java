package taskmodel;

import java.util.Objects;

public class Task extends AbstractTask {

    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
        taskType = TaskType.Task;
    }

    public Task(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        taskType = TaskType.Task;
    }

}
