import TaskPackage.EpicTask;
import TaskPackage.SubTask;
import TaskPackage.Task;
import TaskPackage.TaskStatus;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.addTaskToSimpleTaskMap(new Task("Убрать за котом",
                "Необходимо убрать лоток за котом, помыть его и насыпать новго наполнителя",
                manager.setIdNumeration(), TaskStatus.NEW));

        manager.addTaskToEpicTaskMap(new EpicTask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", manager.setIdNumeration(), TaskStatus.NEW));

        manager.addTaskToSubTaskMap(new SubTask("Помыть посуду", "Посуда должна быть чистой",
                manager.setIdNumeration(), (EpicTask) manager.showTask(2), TaskStatus.NEW));

        manager.addTaskToSubTaskMap(new SubTask("Убрать со стола",
                "Убрать грязную посуду, стереть со стола", manager.setIdNumeration(),
                (EpicTask) manager.showTask(2), TaskStatus.NEW));

        manager.addTaskToEpicTaskMap(new EpicTask("Убраться в спальне",
                "Провести быструю уборку в спальной комнате", manager.setIdNumeration(), TaskStatus.NEW));

        manager.addTaskToSubTaskMap(new SubTask("Убрать постель",
                "Убрать одеяла, застелить постель", manager.setIdNumeration(),
                (EpicTask) manager.showTask(5), TaskStatus.NEW));
    }
}
