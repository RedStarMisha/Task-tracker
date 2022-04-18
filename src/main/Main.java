package main;

import manager.*;
import mylist.MyLinkedList;
import org.w3c.dom.Node;
import taskmodel.*;

import java.io.IOException;
import java.util.*;


public class Main {

    //static TaskManager manager = Managers.getDefault();
    static FileBacketTaskManager manager;

    static {
        try {
            manager = new FileBacketTaskManager(new InMemoryHistoryManager());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {

       createTask();
//        manager.getTask(3);
//        System.out.println(manager.history());
//        manager.getTask(1);
//        System.out.println(manager.history());
//        manager.getTask(3);
//        System.out.println(manager.history());
//        manager.getTask(8);
//        System.out.println(manager.history());
//        manager.getTask(3);
//        System.out.println(manager.history());
//        manager.getTask(4);
//        manager.deteteTask(3);
//        System.out.println(manager.getAllTask());
//        System.out.println(manager.history());
//
//        //MyLinkedList<Integer> myLinkedList = new MyLinkedList<>();
        //manager.add(new Task("Погладить брюки","Погладить брюки на работу, сделать стрелки",manager.setIdNumeration(),TaskStatus.NEW));
        manager.getTask(3);
        manager.getTask(5);
        manager.getTask(1);
        manager.getTask(7);
       // manager.dataLoader();
    }

    private static void createTask() throws IOException {
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
                "Убрать одеяла и застелить постель", manager.setIdNumeration(), TaskStatus.NEW,
                6));
        manager.add(new EpicTask("Приготовить ужин",
                "Приготовить ужин на двоих", manager.setIdNumeration(), TaskStatus.NEW));
    }
}
