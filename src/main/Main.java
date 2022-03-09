package main;

import manager.InMemoryTaskManager;
import manager.InMemoryTaskManagerOneMap;
import manager.Managers;
import manager.TaskManager;
import taskmodel.*;

public class Main {
    //static InMemoryTaskManager manager = new InMemoryTaskManager();
    static TaskManager manager = new InMemoryTaskManagerOneMap();

    public static void main(String[] args) {
        createOneMap ();
        System.out.println(manager.getTask(1));
        System.out.println(manager.getAllTask());
        manager.clearTaskMap();
        System.out.println(manager.getAllTask());
        createOneMap();
        System.out.println(manager.getAllTask());
//    createMapsForToDo();
//        System.out.println(manager.getSimpleTaskMap());
//        System.out.println(manager.getEpicTaskMap());
//        System.out.println(manager.getSubTaskMap());
//        //System.out.println(manager.getSimpleTask());
//        manager.updateTaskStatus(new Task(manager.getSimpleTask(1),
//                TaskStatus.IN_PROGRESS));
//        manager.updateTaskStatus(new SubTask(manager.getSubTask(4),
//                TaskStatus.IN_PROGRESS));
//        System.out.println("После обновления статусов In progress");
//        System.out.println(manager.getSimpleTaskMap());
//        System.out.println(manager.getEpicTaskMap());
//        System.out.println(manager.getSubTaskMap());
//        System.out.println(manager.history());
//        manager.updateTaskStatus(new SubTask(manager.getSubTask(4),
//                TaskStatus.DONE));
//        manager.updateTaskStatus(new SubTask(manager.getSubTask(5),
//                TaskStatus.DONE));
//        manager.updateTaskStatus(new Task(manager.getSimpleTask(1),
//                TaskStatus.DONE));
//        manager.updateTaskStatus(new SubTask(manager.getSubTask(7),
//                TaskStatus.DONE));
//        System.out.println("После обновления статусов DONE");
//        System.out.println(manager.getSimpleTaskMap());
//        System.out.println(manager.getEpicTaskMap());
//        System.out.println(manager.getSubTaskMap());
//        System.out.println("После удаления задач");
//        manager.deteteTask(7);
//        manager.deteteTask(1);
//        System.out.println(manager.getSimpleTaskMap());
//        System.out.println(manager.getEpicTaskMap());
//        System.out.println(manager.getSubTaskMap());
    }

    private static void createOneMap () {
        manager.add(new Task("Убрать за котом",
        "Необходимо убрать лоток за котом, помыть его и насыпать новго наполнителя",
        manager.setIdNumeration(), TaskStatus.NEW));
        manager.add(new Task("Поменять лампочку на кухне",
                "Выкрутить старую лампочку, правильно ее утилизировать и вкрутить новую",
                manager.setIdNumeration(), TaskStatus.NEW));
        manager.add(new EpicTask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", manager.setIdNumeration(), TaskStatus.NEW));
        manager.add(new SubTask("Помыть посуду", "Посуда должна быть чистой",
                manager.setIdNumeration(), TaskStatus.NEW,
                3));
        manager.add(new SubTask("Убрать со стола",
                "Убрать грязную посуду, стереть со стола", manager.setIdNumeration(), TaskStatus.NEW,
                3));
        manager.add(new EpicTask("Убраться в спальне",
                "Провести быструю уборку в спальной комнате", manager.setIdNumeration(), TaskStatus.NEW));
        manager.add(new SubTask("Убрать постель",
                "Убрать одеяла, застелить постель", manager.setIdNumeration(), TaskStatus.NEW,
                6));
    }

    private static void createMapsForToDo () {
//        manager.addTask(new Task("Убрать за котом",
//                "Необходимо убрать лоток за котом, помыть его и насыпать новго наполнителя",
//                manager.setIdNumeration(), TaskStatus.NEW));
//        manager.addTask(new Task("Поменять лампочку на кухне",
//                "Выкрутить старую лампочку, правильно ее утилизировать и вкрутить новую",
//                manager.setIdNumeration(), TaskStatus.NEW));
//        manager.addTask(new EpicTask("Убраться на кухне",
//                "Необходимо провести полную уборку кухни", manager.setIdNumeration(), TaskStatus.NEW));
//        manager.addTask(new SubTask("Помыть посуду", "Посуда должна быть чистой",
//                manager.setIdNumeration(), TaskStatus.NEW,
//                manager.getEpicTask(3)));
//        manager.addTask(new SubTask("Убрать со стола",
//                "Убрать грязную посуду, стереть со стола", manager.setIdNumeration(), TaskStatus.NEW,
//                manager.getEpicTask(3)));
//        manager.addTask(new EpicTask("Убраться в спальне",
//                "Провести быструю уборку в спальной комнате", manager.setIdNumeration(), TaskStatus.NEW));
//        manager.addTask(new SubTask("Убрать постель",
//                "Убрать одеяла, застелить постель", manager.setIdNumeration(), TaskStatus.NEW,
//                manager.getEpicTask(6)));
    }
}
