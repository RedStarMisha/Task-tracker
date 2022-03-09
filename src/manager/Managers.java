package manager;

public class Managers <T extends TaskManager>  {

    public T getDefault() {
        return (T) new InMemoryTaskManager();
    }
}
