import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    void add(AbstractTask task) throws ManagerSaveException, AddEmptyElementException;

    AbstractTask getTask(int id) throws Exception;

    int setIdNumeration();

    void updateTaskStatus(int id, TaskStatus status) throws ManagerSaveException;

    Map<Integer, AbstractTask> getAllTask();

    void clearTaskMap();

    void deteteTask(int id) throws IOException, ManagerSaveException;

    List<AbstractTask> history();

}
