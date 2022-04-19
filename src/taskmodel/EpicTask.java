package taskmodel;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends AbstractTask {
    private List<Integer> subTaskListId = new ArrayList<>();

    public EpicTask (String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
        taskType = TaskType.EpicTask;
    }

    public EpicTask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus, List<Integer> subTaskListId) {
        super(taskName, tastDescription, taskId, tastStatus);
        this.subTaskListId = subTaskListId;
        taskType = TaskType.EpicTask;
    }


    public EpicTask(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        EpicTask epicTask = (EpicTask) task;
        this.subTaskListId = epicTask.getSubTaskListId();
        taskType = TaskType.EpicTask;
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
        String epicInfo = super.toString() + ".";
        for (int i = 0; i < subTaskListId.size(); i++) {
            epicInfo += (i == subTaskListId.size() - 1) ? subTaskListId.get(i) : subTaskListId.get(i) + ",";
        }
        return epicInfo;
    }

}
