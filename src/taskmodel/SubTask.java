package taskmodel;

public class SubTask extends AbstractTask<SubTask>{
    private EpicTask epicTask;
    private int epicTaskId;

    public SubTask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus, int epicTaskId) {
        super(taskName, tastDescription, taskId, tastStatus);
        this.epicTaskId = epicTaskId;
        //epicTask.addSubTask(this);
    }

    public SubTask(SubTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        this.epicTaskId = epicTaskId;
    }

    @Override
    public String toString() {
        return "SubTask эпика '" + epicTask.taskName + "'" + "{"
                + "taskName='" + taskName + '\''
                + ", taskStatus='" + taskStatus + '\''
                + '}';
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }
}
