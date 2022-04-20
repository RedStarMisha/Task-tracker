package manager;

import myexception.ManagerSaveException;

public interface Saveable {
    void save() throws ManagerSaveException;
}
