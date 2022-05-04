import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class InMemoryHistoryManager implements HistoryManager{
    private MyLinkedList historyList = new MyLinkedList();
    private Map<Integer, AbstractTask> mapForFindTaskInList = new HashMap<>();


    @Override
    public void addTask(AbstractTask task) {
        if (task != null) {
                remove(task.getTaskId());
            if (historyList.size() == 10) {
                historyList.delete(0);
            }
            historyList.add(task);
            mapForFindTaskInList.put(task.getTaskId(),task);
        }
    }

    @Override
    public List<AbstractTask> getHistory() throws NoSuchElementException {
        return historyList.toArrayList();
    }

    @Override
    public void remove(int id) {
        if (mapForFindTaskInList.containsKey(id)) {
            historyList.delete(mapForFindTaskInList.get(id));
            mapForFindTaskInList.remove(id);
        }
    }

}
