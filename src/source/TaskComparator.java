import java.time.Duration;
import java.util.Comparator;

public class TaskComparator implements Comparator<AbstractTask>  {


    @Override
    public int compare(AbstractTask o1, AbstractTask o2) {
        if (o1.getStartTime() == null && o2.getStartTime() == null) {
            return o1.getTaskId() - o2.getTaskId();
        }
        if (o2.getStartTime() == null) {
            return 1;
        }
        if (o1.getStartTime() == null) {
            return -1;
        }
        if (Duration.between(o1.getStartTime(),o2.getStartTime()).equals(0)) {
            return o1.getTaskId() - o2.getTaskId();
        }
        return Duration.between(o1.getStartTime(),o2.getStartTime()).toMinutesPart();
    }
}
