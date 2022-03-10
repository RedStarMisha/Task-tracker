package manager;

import taskmodel.AbstractTask;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private List<AbstractTask> historyList = new LinkedList<>();

    @Override
    public void addTask(AbstractTask task) {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public void getHistory() {
        System.out.println("История запросов :");
        for (AbstractTask task : historyList) {
            System.out.println(task);
        }
    }
}
