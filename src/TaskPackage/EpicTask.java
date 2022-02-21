package TaskPackage;

import java.util.HashMap;
import java.util.Map;

public class EpicTask extends Task {
    public Map<Integer , SubTask> subTaskList;

    public EpicTask (String taskName, String tastDescription, int taskId, TaskStatus tastStatus) {
        super(taskName, tastDescription, taskId, tastStatus);
        subTaskList = new HashMap <>();
    }

    public EpicTask(Task task, TaskStatus tastStatus) {
        super(task, tastStatus);
    }

    public void addSubTask (SubTask subTask) {
        subTaskList.put(subTask.getTaskId() , subTask);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "taskName='" + taskName + '\'' +
                ", tastDescription='" + tastDescription + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

    @Override
    public int hashCode() {
        return subTaskList != null ? subTaskList.hashCode() : 0;
    }
}
