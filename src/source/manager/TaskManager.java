import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface TaskManager {

    void add(AbstractTask task) throws ManagerSaveException, AddEmptyElementException, ExceptionTaskIntersection;

    AbstractTask getTask(int id) throws Exception;

    int setIdNumeration();

    void updateTaskStatus(int id, TaskStatus status) throws ManagerSaveException;

    Map<Integer, AbstractTask> getAllTask();

    void clearTaskMap();

    void deteteTask(int id) throws IOException, ManagerSaveException;

    List<AbstractTask> history();

    TreeSet<AbstractTask> getSortedTask();
}
