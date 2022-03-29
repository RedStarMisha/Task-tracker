package main;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import mylist.MyLinkedList;
import org.w3c.dom.Node;
import taskmodel.*;

import java.util.*;


public class Main {
    static TaskManager manager = Managers.getDefault();

    public static void main(String[] args) throws Exception {
//        final long startTime = System.nanoTime();
//        createTask();
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
//        final long endTime = System.nanoTime();
//        System.out.println("Время " + (endTime - startTime));
        MyLinkedList<Integer> myLinkedList = new MyLinkedList<>();
        myLinkedList.add(1);
        myLinkedList.add(2);
        myLinkedList.add(3);
        myLinkedList.add(4);
        myLinkedList.add(5);
        myLinkedList.add(6);
        myLinkedList.add(7);
        myLinkedList.add(8);
        myLinkedList.add(9);
        myLinkedList.add(11);
        System.out.println(myLinkedList);

        //myLinkedList.delete(9);
        myLinkedList.add(9,10);
        myLinkedList.add(0,0);
        myLinkedList.add(5,25);
        System.out.println(myLinkedList);

    }

    private static void createTask() {
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
        manager.add(new EpicTask("Приготовить ужин",
                "Приготовить ужин на двоих", manager.setIdNumeration(), TaskStatus.NEW));
    }
}
