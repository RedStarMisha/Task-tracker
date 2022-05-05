import java.util.Comparator;

public class TaskComparator implements Comparator<AbstractTask>  {


    @Override
    public int compare(AbstractTask o1, AbstractTask o2) {
        if (o2.getDuration() == null) {
            return 1;
        }
        return (int)o1.getDuration().toMinutes() - (int)o2.getDuration().toMinutes();
    }
}
