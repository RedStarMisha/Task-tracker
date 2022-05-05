import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicTask extends AbstractTask {
    private List<Integer> subTaskListId = new ArrayList<>();
    private LocalDateTime endTime;

    public EpicTask (String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
        taskType = TaskType.EpicTask;
    }

    public EpicTask (String taskName, String tastDescription, int taskId, TaskStatus tastStatus,
                     String startTime, long duration) throws Exception {
        super(taskName, tastDescription, taskId, tastStatus, startTime, duration);
        taskType = TaskType.EpicTask;
        this.endTime = this.startTime.plus(this.duration);
    }


    public EpicTask(String taskName, String tastDescription, int taskId,
                    TaskStatus tastStatus, List<Integer> subTaskListId) {
        super(taskName, tastDescription, taskId, tastStatus);
        this.subTaskListId = subTaskListId;
        taskType = TaskType.EpicTask;
    }


    public EpicTask (String taskName, String tastDescription, int taskId, TaskStatus tastStatus,
                     String startTime, long duration, List<Integer> subTaskListId) throws Exception {
        super(taskName, tastDescription, taskId, tastStatus, startTime, duration);
        taskType = TaskType.EpicTask;
        this.endTime = this.startTime.plus(this.duration);
        this.subTaskListId = subTaskListId;
    }

    public EpicTask(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        EpicTask epicTask = (EpicTask) task;
        this.subTaskListId = epicTask.getSubTaskListId();
        taskType = TaskType.EpicTask;
        if (epicTask.duration != null) {
            this.duration = epicTask.getDuration();
            this.startTime = epicTask.getStartTime();
            this.endTime = epicTask.getEndTime();
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    /**
     * В Эпике хранится тоько список id Сабтасков,
     * относящихся к текущему эпику
     */
    public void addSubTask(Integer id) {
        subTaskListId.add(id);
    }

    public List<Integer> getSubTaskListId() {
        return subTaskListId;
    }

    @Override
    public String toString() {
        String epicInfo = super.toString() + ".";// + String.join("," , subTaskListId.toString());
        for (int i = 0; i < subTaskListId.size(); i++) {
            epicInfo += (i == subTaskListId.size() - 1) ? subTaskListId.get(i) : subTaskListId.get(i) + ",";
        }
        return epicInfo;
    }

}
