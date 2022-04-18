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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }




}
