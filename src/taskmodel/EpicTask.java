package taskmodel;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends AbstractTask {
    private List<Integer> subTaskListId = new ArrayList<>();

    public EpicTask (String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
    }

    public EpicTask(AbstractTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        EpicTask epicTask = (EpicTask) task;
        this.subTaskListId = epicTask.getSubTaskListId();
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
        return "EpicTask{" +
                "taskName='" + taskName + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
