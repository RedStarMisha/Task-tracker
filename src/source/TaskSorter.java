import java.util.TreeSet;

public class TaskSorter {

    static void add(TreeSet<AbstractTask> treeSet, AbstractTask task) {
        if (task instanceof EpicTask) {
            return;
        }
        treeSet.add(task);
    }

    static void remove(TreeSet<AbstractTask> treeSet, AbstractTask task){
        if (treeSet.contains(task)) {
            treeSet.remove(task);
        }
        treeSet.remove(task);
    }
}
