package manager;

import taskmodel.AbstractTask;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    //public void addTask(AbstractTask task);





    //public List history();

    public void add(AbstractTask task);

    public AbstractTask getTask(int id);

    public int setIdNumeration();

    public void updateTaskStatus(AbstractTask task);

    public Map<Integer, AbstractTask> getAllTask();

    public void clearTaskMap();
}
