package manager;

import taskmodel.AbstractTask;
import taskmodel.TaskStatus;

import java.util.Map;

public interface TaskManager {

    public void add(AbstractTask task);

    public AbstractTask getTask(int id);

    public int setIdNumeration();

    public void updateTaskStatus(int id, TaskStatus status);

    public Map<Integer, AbstractTask> getAllTask();

    public void clearTaskMap();

    public void deteteTask(int id);

    public void history();
}
