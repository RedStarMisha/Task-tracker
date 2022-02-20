import TaskPackage.*;

public class Manager {
    static int idNumeration = 1;

    public static void main(String[] args) {
        Task task = new Task("Убрать за котом",
                "Необходимо убрать лоток за котом, помыть его и насыпать новго наполнителя", setIdNumeration());
        EpicTask cleanCitchen = new EpicTask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", setIdNumeration());
        SubTask cleanDishes = new SubTask("Помыть посуду", "Посуда должна быть чистой",
                setIdNumeration(), cleanCitchen);
        cleanDishes.addTaskForEpicList();
        SubTask cleanFloor = new SubTask("Помыть пол на кухне",
                "Необходимо помыть пол чтобы он блестел", setIdNumeration(), cleanCitchen);
        cleanFloor.addTaskForEpicList();
        System.out.println(cleanCitchen.subTaskList);
    }

    public static int setIdNumeration() {
        return idNumeration++;
    }
}
