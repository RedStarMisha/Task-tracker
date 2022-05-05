import java.util.TreeSet;

public class TaskSorter {

    static void add(TreeSet<AbstractTask> treeSet, AbstractTask task) {
        if (treeSet.contains(task)) {
            treeSet.remove(task);
        }
        treeSet.add(task);
    }
}
