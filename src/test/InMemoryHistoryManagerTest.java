import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class InMemoryHistoryManagerTest {
    static Task task1;
    static Task task2;
    static Task task3;
    static Task task4;
    static Task task5;
    static Epictask epicTask;
    static Subtask subTask;


    HistoryManager historyManager;

    @BeforeEach
    void createHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @BeforeAll
    static void createTask() throws Exception {
        task1 = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                1,TaskStatus.NEW, "12-01-2022, 16:00",30);

        task2 = new Task("Пострирать белье", "Белое",
                2,TaskStatus.NEW, "13-01-2022, 20:00",120);
        task3 = new Task("Почистить обувь", "Черные туфли",
                3,TaskStatus.NEW, "13-01-2022, 18:00",15);
        task4 = new Task("Помыть посуду", "Кострюли, сковородки",
                4,TaskStatus.NEW, "12-01-2022, 12:00",20);
        task5 = new Task("Убраться на кухне",
                "Необходимо провести полную уборку кухни", 5, TaskStatus.NEW, "12-01-2022, 13:00",40);
        epicTask = new Epictask("Сходить в магазин",
                "Гипермаркет", 6, TaskStatus.NEW, "12-01-2022, 13:00",120);
        subTask = new Subtask("Купить курочку",
                "Петелинка", 7, TaskStatus.NEW, "12-01-2022, 13:20",15,6);
    }

    @Test
    void shouldAddTaskInHistoryManagerWithoutDuplication() {
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.addTask(task4);
        historyManager.addTask(task5);
        historyManager.addTask(task1);
        Assertions.assertNotNull(historyManager.getHistory());
        Assertions.assertEquals(5,historyManager.getHistory().size());
        Assertions.assertEquals(task1,historyManager.getHistory().get(4));
    }

    @Test
    void shouldGetHistoryWhenEmptyHistoryList() {
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldDeleteTaskInHistoryManager() {
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.remove(1);
        List<Task> localList = List.of(task2,task3);
        Assertions.assertIterableEquals(historyManager.getHistory(),localList);
    }


    @Test
    void shouldDeleteNonExistElement() {
        historyManager.addTask(task1);
        historyManager.remove(5);
        Assertions.assertNotNull(historyManager.getHistory());
        Assertions.assertEquals(1,historyManager.getHistory().size());
    }

}