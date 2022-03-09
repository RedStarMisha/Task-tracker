package taskmodel;

import java.util.Objects;

public class Task extends AbstractTask<Task> {

    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
    }

    public Task(Task task, TaskStatus tastStatus) {
        super(task, tastStatus);
    }



    //    protected String taskName;
//    protected String tastDescription;
//    protected int taskId;
//    protected TaskStatus taskStatus;
//
//    public Task(String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
//        this.taskName = taskName;
//        this.tastDescription = tastDescription;
//        this.taskId = taskId;
//        this.taskStatus = tastStatus;
//    }
//
//    public Task(Task task, TaskStatus tastStatus) {
//        this.taskName = task.taskName;
//        this.tastDescription = task.tastDescription;
//        this.taskId = task.taskId;
//        this.taskStatus = tastStatus;
//    }
//
//    public int getTaskId() {
//        return taskId;
//    }
//
//    public String getTaskName() {
//        return taskName;
//    }
//
//    public String getTastDescription() {
//        return tastDescription;
//    }
//
//    public TaskStatus getTaskStatus() {
//        return taskStatus;
//    }

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

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
