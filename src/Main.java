import TaskPackage.EpicTask;
import TaskPackage.SubTask;
import TaskPackage.Task;
import TaskPackage.TaskStatus;

public class Main {
    static Manager manager = new Manager();
    public static void main(String[] args) {
    createMapsForToDo();
        System.out.println(manager.getSimpleTaskMap());
        System.out.println(manager.getEpicTaskMap());
        System.out.println(manager.getSubTaskMap());
        manager.updateTaskStatus(new Task(manager.getSimpleTask(manager.findTaskId("Убрать за котом")) , TaskStatus.IN_PROGRESS));
        manager.updateTaskStatus(new SubTask(manager.getSubTask(manager.findTaskId("Убрать постель")) , TaskStatus.IN_PROGRESS));
        System.out.println("После обновления статусов");
        System.out.println(manager.getSimpleTaskMap());
        System.out.println(manager.getEpicTaskMap());
        System.out.println(manager.getSubTaskMap());

    }

    private static void createMapsForToDo () {
        manager.addTask(new Task("Убрать за котом",
                "Необходимо убрать лоток за котом, помыть его и насыпать новго наполнителя",
                manager.setIdNumeration(), TaskStatus.NEW));
        manager.addTask(new Task("Поменять лампочку на кухне",
                "Выкрутить старую лампочку, правильно ее утилизировать и вкрутить новую",
                manager.setIdNumeration(), TaskStatus.NEW));
        manager.addTask(new EpicTask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", manager.setIdNumeration(), TaskStatus.NEW));
        manager.addTask(new SubTask("Помыть посуду", "Посуда должна быть чистой",
                manager.setIdNumeration(), TaskStatus.NEW,
                manager.getEpicTask(manager.findTaskId("Убраться на кухне"))));
        manager.addTask(new SubTask("Убрать со стола",
                "Убрать грязную посуду, стереть со стола", manager.setIdNumeration(), TaskStatus.NEW,
                manager.getEpicTask(manager.findTaskId("Убраться на кухне"))));
        manager.addTask(new EpicTask("Убраться в спальне",
                "Провести быструю уборку в спальной комнате", manager.setIdNumeration(), TaskStatus.NEW));
        manager.addTask(new SubTask("Убрать постель",
                "Убрать одеяла, застелить постель", manager.setIdNumeration(), TaskStatus.NEW,
                manager.getEpicTask(manager.findTaskId("Убраться в спальне"))));
    }
}
