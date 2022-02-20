package TaskPackage;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    public List<SubTask> subTaskList;

    public EpicTask (String taskName, String tastDescription, int taskId) {
        super(taskName, tastDescription, taskId);
        subTaskList = new ArrayList<>();
    }

    public void addSubTask (SubTask subTask) {
        subTaskList.add(subTask);
    }
}
