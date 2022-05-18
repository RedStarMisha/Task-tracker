import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBacketTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void createTaskManager() throws Exception {
        taskManager = Managers.getFileManager();
    }

    @Override
    @Test
    void shouldAddThreeTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldAddThreeTask();
    }

    @Test
    @Override
    void shouldAddNullTask() {
        super.shouldAddNullTask();
    }


    @Override
    @Test
    void shouldGetThreeTaskAndRecordInHistory() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldGetThreeTaskAndRecordInHistory();
    }

    @Override
    @Test
    void shouldGetTaskWithIncorrectId() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldGetTaskWithIncorrectId();
    }

    @Override
    @Test
    void shouldCheckNumeration() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldCheckNumeration();
    }

    @Override
    @Test
    void shouldUpdateTaskStatusInSimpleTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldUpdateTaskStatusInSimpleTask();
    }

    @Override
    @Test
    void shouldUpdateTaskStatusInSubTaskAndEpicTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldUpdateTaskStatusInSubTaskAndEpicTask();
    }

    @Test
    @Override
    void shouldUpdateTaskStatusForNonExistingId() throws ManagerSaveException {
        super.shouldUpdateTaskStatusForNonExistingId();
    }

    @Override
    @Test
    void shouldGetAllTask() throws Exception {
        taskManager = Managers.getFileManagerWithoutRecovery();
        super.createAndAddThreeTaskWithoutDate();
        super.shouldGetAllTask();
    }

    @Override @Test
    void shouldGetAllTaskForEmptyTaskMap() throws Exception {
        taskManager = Managers.getFileManagerWithoutRecovery();
        super.shouldGetAllTaskForEmptyTaskMap();
    }

    @Override
    @Test
    void shouldClearTaskMap() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldClearTaskMap();
    }

    @Override
    @Test
    void shouldDeteteSimpleTaskWithIdOne() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldDeteteSimpleTaskWithIdOne();
    }

    @Override
    @Test
    void shouldDeteteEpicTaskWithIdTwo() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldDeteteEpicTaskWithIdTwo();
    }

    @Override
    @Test
    void shouldReturnEpicIdFromSubTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldReturnEpicIdFromSubTask();
    }

    @Override @Test
    void shouldDeleteSubtaskWhenDeleteEpicInHistoryManager() throws Exception {
        createAndAddThreeTaskWithoutDate();
        super.shouldDeleteSubtaskWhenDeleteEpicInHistoryManager();
    }

    @Test
    @Override
    void shouldDeleteTaskForNonExistingId() throws ManagerSaveException, IOException {
        super.shouldDeleteTaskForNonExistingId();
    }

    @Test
    void shouldRecoverTaskData() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        String pathTest = System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt";
        FileBacketTaskManager.RECOVERY = false;
        TaskManager localTaskManager = new FileBacketTaskManager(pathTest);
        assertAll(
                () ->assertFalse(localTaskManager.getAllTask().isEmpty()),
                () ->assertEquals(taskManager.getTask(1), localTaskManager.getTask(1)),
                () ->assertEquals(taskManager.getTask(2), localTaskManager.getTask(2)),
                () ->assertEquals(taskManager.getTask(3), localTaskManager.getTask(3)),
                () ->assertIterableEquals(taskManager.history(), localTaskManager.history())
        );
    }

    @Test
    void shouldRecoverTaskDataWithoutSubTask() throws Exception {
        //taskManager = Managers.getFileManagerWithoutRecovery();
        Task localTask = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                1, TaskStatus.NEW, "12-01-2022, 16:00", 10);
        Epictask localEpicTask = new Epictask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", 2, TaskStatus.NEW);
        taskManager.add(localTask);
        taskManager.add(localEpicTask);
        String pathTest = System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt";
        FileBacketTaskManager.RECOVERY = true;
        TaskManager localTaskManager = new FileBacketTaskManager(pathTest);
        assertAll(
                () ->assertFalse(localTaskManager.getAllTask().isEmpty()),
                () ->assertEquals(taskManager.getTask(1), localTaskManager.getTask(1)),
                () ->assertEquals(taskManager.getTask(2), localTaskManager.getTask(2)),
                () ->assertIterableEquals(taskManager.history(), localTaskManager.history())
        );
    }
}
