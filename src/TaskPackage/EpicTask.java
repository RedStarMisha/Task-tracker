package TaskPackage;

import java.util.HashMap;
import java.util.Map;

public class EpicTask extends Task {
    private Map<Integer , SubTask> subTaskList;

    public EpicTask (String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
        subTaskList = new HashMap <>();
    }

    public EpicTask(EpicTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        this.subTaskList = task.getSubTaskList();
    }

    /** Добавляет субклассы в локальное хранилище эпика */
    protected void addSubTask (SubTask subTask) {
        subTaskList.put(subTask.getTaskId() , subTask);
    }

    public Map<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "taskName='" + taskName + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
