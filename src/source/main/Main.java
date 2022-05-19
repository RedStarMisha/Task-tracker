/**
 * Прошу дополнительно посмотреть верность(а скорее неверность) применение исключений.
 * Я в этой теме не совсем хорошо разобрался.
 * Также прошу взглянуть на MyLinkedList т.к. в том спринте я его не делал
 */

public class Main {
    static TaskManager manager;

    public static void main(String[] args) throws ManagerSaveException, AddEmptyElementException {
        try {
            new KVServer().start();
            HttpTaskServer httpTaskServer = new HttpTaskServer("http://localhost:8080");
            httpTaskServer.createServer();


//            manager.getTask(3);
//            System.out.println(manager.history());
//            manager.getTask(1);
//            System.out.println(manager.history());
//            manager.getTask(3);
//            manager.getTask(8);
//            manager.getTask(3);
//            manager.getTask(4);
//            manager.deteteTask(8);
//            manager.add(new EpicTask("Приготовить ужин", "Приготовить ужин на двоих",
//                    manager.setIdNumeration(), TaskStatus.NEW));
//            manager.getTask(9);
//            manager.updateTaskStatus(2, TaskStatus.IN_PROGRESS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createTask() throws Exception {

        manager.add(new Task("Поменять лампочку на кухне",
                "Выкрутить старую лампочку, правильно ее утилизировать и вкрутить новую",
                manager.setIdNumeration(), TaskStatus.NEW, "12-01-2022, 16:00",10));
        manager.add(new Epictask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", manager.setIdNumeration(), TaskStatus.NEW,
                "15-01-2022, 11:00",20));
        manager.add(new Subtask("Помыть посуду", "Посуда должна быть чистой",
                manager.setIdNumeration(), TaskStatus.IN_PROGRESS, "12-01-2022, 17:05",40,
                2));
        manager.add(new Subtask("Убрать со стола",
                "Убрать грязную посуду, стереть со стола", manager.setIdNumeration(), TaskStatus.DONE,
                "15-01-2022, 11:50",10,2));
        manager.add(new Epictask("Убраться в спальне",
                "Провести быструю уборку в спальной комнате", manager.setIdNumeration(), TaskStatus.NEW));
        manager.add(new Subtask("Убрать постель",
                "Убрать одеяла и застелить постель", manager.setIdNumeration(), TaskStatus.NEW,
                "14-01-2022, 09:00",5, 5));
        manager.add(new Epictask("Приготовить ужин",
                "Приготовить ужин на двоих", manager.setIdNumeration(), TaskStatus.NEW));
    }
}
