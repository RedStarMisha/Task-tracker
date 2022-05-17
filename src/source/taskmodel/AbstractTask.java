import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractTask {
    private final String taskName;
    private final String taskDescription;
    private final int taskId;
    protected TaskStatus taskStatus;
    private final TaskType taskType;
    private Duration duration;
    private LocalDateTime startTime;

    public AbstractTask(String taskName, String taskDescription, int taskId, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        taskType = TaskType.getTaskType(this.getClass().toString());
    }

    public AbstractTask(String taskName, String taskDescription, int taskId, TaskStatus taskStatus,
                        String startTime, long duration) throws Exception {
        durationChecker(duration);
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, Formater.FORMATTER_DATE);
        taskType = TaskType.getTaskType(this.getClass().toString());
    }

    public AbstractTask(AbstractTask task, TaskStatus taskStatus) {
        this.taskName = task.taskName;
        this.taskDescription = task.taskDescription;
        this.taskId = task.taskId;
        this.taskStatus = taskStatus;
        this.taskType = task.taskType;
        if (task.getStartTime() != null) {
            this.duration = task.getDuration();
            this.startTime = task.getStartTime();
        }
    }


    private void durationChecker(long duration) {
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

    public String getTaskDescription() {
        return taskDescription;
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

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        if (startTime != null) {
            return taskType.toString() + "." + taskId + "." + taskName + "." + taskDescription + "." + taskStatus +
                    "." + startTime.format(Formater.FORMATTER_DATE) + "." + duration.toMinutes();
        } else {
            return taskType.toString() + "." + taskId + "." + taskName + "." + taskDescription + "." + taskStatus;
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
