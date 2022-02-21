package TaskPackage;

public class Task {
    public String taskName;
    public String tastDescription;
    public int taskId;
    public TaskStatus taskStatus;

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
                ", tastDescription='" + tastDescription + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return taskName.equals(task.taskName);
    }

    @Override
    public int hashCode() {
        return taskName.hashCode();
    }
}
