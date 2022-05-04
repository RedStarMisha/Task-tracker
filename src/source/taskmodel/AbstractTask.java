import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractTask {
    protected final String taskName;
    protected final String tastDescription;
    protected final int taskId;
    protected TaskStatus taskStatus;
    protected TaskType taskType;
    Duration duration;
    LocalDateTime startTime;


    public AbstractTask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.tastDescription = tastDescription;
        this.taskId = taskId;
        this.taskStatus = tastStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    public AbstractTask(AbstractTask task, TaskStatus tastStatus) {
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

//    public LocalDateTime getEndTime() {
//
//    }

    @Override
    public String toString() {
        return taskType.toString() + "." + taskId + "." + taskName + "." + tastDescription + "." + taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTask task = (AbstractTask) o;

        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return taskId;
    }
}
