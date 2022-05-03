import java.util.List;

public interface HistoryManager {

    void addTask (AbstractTask task);

    void remove(int id);

    List<AbstractTask> getHistory();
}
