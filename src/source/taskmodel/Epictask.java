import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epictask extends AbstractTask {
    private List<Integer> subTaskListId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epictask(String taskName, String taskDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, taskDescription, taskId, tastStatus);
    }

    public Epictask(String taskName, String taskDescription, int taskId, TaskStatus tastStatus,
                    String startTime, long duration) throws Exception {
        super(taskName, taskDescription, taskId, tastStatus, startTime, duration);
        this.endTime = super.getStartTime().plus(super.getDuration());
    }

    public Epictask(String taskName, String tastDescription, int taskId,
                    TaskStatus tastStatus, List<Integer> subTaskListId) {
        super(taskName, tastDescription, taskId, tastStatus);
        this.subTaskListId = subTaskListId;
    }

    public Epictask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus,
                    String startTime, long duration, List<Integer> subTaskListId) throws Exception {
        super(taskName, tastDescription, taskId, tastStatus, startTime, duration);
        this.endTime = super.getStartTime().plus(super.getDuration());
        this.subTaskListId = subTaskListId;
    }

    public Epictask(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        Epictask epicTask = (Epictask) task;
        this.subTaskListId = epicTask.getSubTaskListId();
        if (epicTask.endTime != null) {
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
