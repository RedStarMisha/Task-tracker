package manager;

import taskmodel.AbstractTask;

import java.util.List;

public interface HistoryManager {

    void addTask (AbstractTask task);

    List<AbstractTask> getHistory();
}
