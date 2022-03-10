package manager;

import taskmodel.AbstractTask;

public interface HistoryManager {

    void addTask (AbstractTask task);

    void getHistory();
}
