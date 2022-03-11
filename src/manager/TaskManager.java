package manager;

import taskmodel.AbstractTask;
import taskmodel.TaskStatus;

import java.util.Map;

public interface TaskManager {

    void add(AbstractTask task);

    AbstractTask getTask(int id);

    int setIdNumeration();

    void updateTaskStatus(int id, TaskStatus status);

    Map<Integer, AbstractTask> getAllTask();

    void clearTaskMap();

    void deteteTask(int id);

    void history();
}
