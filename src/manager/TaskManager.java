package manager;

import taskmodel.AbstractTask;
import taskmodel.TaskStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    void add(AbstractTask task) throws IOException;

    AbstractTask getTask(int id) throws Exception;

    int setIdNumeration();

    void updateTaskStatus(int id, TaskStatus status);

    Map<Integer, AbstractTask> getAllTask();

    void clearTaskMap();

    void deteteTask(int id);

    List<AbstractTask> history();
}
