package manager;

import taskmodel.AbstractTask;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private List<AbstractTask> historyList = new LinkedList<>();
    private Map<Integer, AbstractTask> mapForFindTaskInList = new HashMap<>();

    @Override
    public void addTask(AbstractTask task) {
        remove(task.getTaskId());
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
        historyList.add(task);
        mapForFindTaskInList.put(task.getTaskId(),task);
    }

    @Override
    public List<AbstractTask> getHistory() {
        System.out.println("История обращений к задачам");
        return new ArrayList<>(historyList);
    }

    @Override
    public void remove(int id) {
        if (mapForFindTaskInList.containsKey(id)) {
            historyList.remove(mapForFindTaskInList.get(id));
            mapForFindTaskInList.remove(id);
        }
    }

}
