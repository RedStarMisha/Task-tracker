import java.util.Collection;

public class TaskSorter {

    public static void add(Collection<AbstractTask> set, AbstractTask task) {
        if (task instanceof Epictask) {
            return;
        }
        set.add(task);
    }

    public static void remove(Collection<AbstractTask> set, AbstractTask task){
        if (set.contains(task)) {
            set.remove(task);
        }
        set.remove(task);
    }
}
