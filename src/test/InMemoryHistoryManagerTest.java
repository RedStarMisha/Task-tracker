import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

class InMemoryHistoryManagerTest {
    static Task task1;
    static Task task2;
    static Task task3;
    static Task task4;
    static Task task5;


    HistoryManager historyManager;

    @BeforeEach
    void createHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @BeforeAll
    static void createTask() {
        task1 = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                1,TaskStatus.NEW);

        task2 = new Task("Пострирать белье", "Белое",
                2,TaskStatus.NEW);
        task3 = new Task("Почистить обувь", "Черные туфли",
                3,TaskStatus.NEW);
        task4 = new Task("Помыть посуду", "Кострюли, сковородки",
                4,TaskStatus.NEW);
        task5 = new Task("Убраться на кухне",
                "Необходимо провести полную уборку кухни", 5, TaskStatus.NEW);

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