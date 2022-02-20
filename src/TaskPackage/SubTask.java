package TaskPackage;

public class SubTask extends Task {
    EpicTask epicTask;

    public SubTask(String taskName, String tastDescription, int taskId, EpicTask epicTask) {
        super(taskName, tastDescription, taskId);
        this.epicTask = epicTask;
        //epicTask.addSubTask(this);
    }

    public void addTaskForEpicList () {
        System.out.println(this.toString());
        epicTask.addSubTask(this);
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "taskName='" + taskName + '\'' +
                ", tastDescription='" + tastDescription + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
