import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractTask {
    protected final String taskName;
    protected final String tastDescription;
    protected final int taskId;
    protected TaskStatus taskStatus;
    protected TaskType taskType;
    protected Duration duration;
    protected LocalDateTime startTime;


    public AbstractTask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus,
                        String startTime, long duration) throws Exception {
        durationChecker(duration);
        this.taskName = taskName;
        this.tastDescription = tastDescription;
        this.taskId = taskId;
        this.taskStatus = tastStatus;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, Formater.FORMATTER_DATE);
    }

    public AbstractTask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        this.taskName = taskName;
        this.tastDescription = tastDescription;
        this.taskId = taskId;
        this.taskStatus = tastStatus;
    }

    public AbstractTask(AbstractTask task, TaskStatus tastStatus) {
        this.taskName = task.taskName;
        this.tastDescription = task.tastDescription;
        this.taskId = task.taskId;
        this.taskStatus = tastStatus;
    }

    protected void durationChecker(long duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException ("Длительность должна быть больше 0");
        }
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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        if (startTime != null) {
            return taskType.toString() + "." + taskId + "." + taskName + "." + tastDescription + "." + taskStatus +
                    "." + startTime.format(Formater.FORMATTER_DATE) + "." + duration.toMinutes();
        } else {
            return taskType.toString() + "." + taskId + "." + taskName + "." + tastDescription + "." + taskStatus;
        }
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
