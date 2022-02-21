package TaskPackage;

public class SubTask extends Task {
    private EpicTask epicTask;

    public SubTask(String taskName, String tastDescription, int taskId, TaskStatus tastStatus, EpicTask epicTask) {
        super(taskName, tastDescription, taskId, tastStatus);
        this.epicTask = epicTask;
        epicTask.addSubTask(this);
    }

    public SubTask(SubTask task, TaskStatus tastStatus) {
        super(task, tastStatus);
        this.epicTask = task.getEpicTask();
    }

    @Override
    public String toString() {
        return "SubTask '" + epicTask.taskName + "'" + "{"
                + "taskName='" + taskName + '\'' +
                ", tastDescription='" + tastDescription + '\''
                + ", taskStatus='" + taskStatus + '\''
                + '}';
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }
}
