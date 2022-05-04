import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class FileBacketTaskManagerTest extends TaskManagerTest {

        @BeforeEach
        void createTaskManager() throws Exception {
            taskManager = Managers.getFileManager();
        }

        @Override  @Test
        void shouldAddThreeTask() throws ManagerSaveException, AddEmptyElementException {
            super.createAndAddThreeTask();
            super.shouldAddThreeTask();
        }

        @Test @Override
        void shouldAddNullTask() {
            super.shouldAddNullTask();
        }


        @Override  @Test
        void shouldGetThreeTaskAndRecordInHistory() throws Exception {
            super.createAndAddThreeTask();
            super.shouldGetThreeTaskAndRecordInHistory();
        }

        @Override @Test
        void shouldGetTaskWithIncorrectId() throws ManagerSaveException, AddEmptyElementException {
            super.createAndAddThreeTask();
            super.shouldGetTaskWithIncorrectId();
        }


        @Override  @Test
        void shouldCheckNumeration() throws Exception {
            super.createAndAddThreeTask();
            super.shouldCheckNumeration();
        }

        @Override  @Test
        void shouldUpdateTaskStatusInSimpleTask() throws Exception {
            super.createAndAddThreeTask();
            super.shouldUpdateTaskStatusInSimpleTask();
        }

        @Override  @Test
        void shouldUpdateTaskStatusInSubTaskAndEpicTask() throws Exception {
            super.createAndAddThreeTask();
            super.shouldUpdateTaskStatusInSubTaskAndEpicTask();
        }

        @Test @Override
        void shouldUpdateTaskStatusForNonExistingId() throws ManagerSaveException {
            super.shouldUpdateTaskStatusForNonExistingId();
        }

        @Override  @Test
        void shouldGetAllTask() throws ManagerSaveException, AddEmptyElementException {
            super.createAndAddThreeTask();
            super.shouldGetAllTask();
        }

        @Override  @Test
        void shouldClearTaskMap() throws ManagerSaveException, AddEmptyElementException {
            super.createAndAddThreeTask();
            super.shouldClearTaskMap();
        }

        @Override  @Test
        void shouldDeteteSimpleTaskWithIdOne() throws IOException, ManagerSaveException, AddEmptyElementException {
            super.createAndAddThreeTask();
            super.shouldDeteteSimpleTaskWithIdOne();
        }

        @Override  @Test
        void shouldDeteteEpicTaskWithIdTwo() throws IOException, ManagerSaveException, AddEmptyElementException {
            super.createAndAddThreeTask();
            super.shouldDeteteEpicTaskWithIdTwo();
        }

        @Override  @Test
        void shouldReturnEpicIdFromSubTask() throws ManagerSaveException, AddEmptyElementException {
            super.createAndAddThreeTask();
            super.shouldReturnEpicIdFromSubTask();
        }

        @Test @Override
        void shouldDeleteTaskStatusForNonExistingId () throws ManagerSaveException, IOException {
            super.shouldDeleteTaskStatusForNonExistingId();
        }

        @Test
        void shouldRecoverTaskData() throws Exception {
            super.createAndAddThreeTask();
            Path pathTest = Path.of(System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt");
            TaskManager localTaskManager = new FileBacketTaskManager(Managers.getDefaultHistory(),pathTest);
            Assertions.assertFalse(localTaskManager.getAllTask().isEmpty());
            Assertions.assertEquals(taskManager.getTask(1),localTaskManager.getTask(1));
            Assertions.assertEquals(taskManager.getTask(2),localTaskManager.getTask(2));
            Assertions.assertEquals(taskManager.getTask(3),localTaskManager.getTask(3));
            Assertions.assertIterableEquals(taskManager.history(),localTaskManager.history());
        }

    @Test
    void shouldRecoverTaskDataWithoutHistory() throws Exception {
        super.createAndAddThreeTask();
        Path pathTest = Path.of(System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt");
        TaskManager localTaskManager = new FileBacketTaskManager(Managers.getDefaultHistory(),pathTest);
        Assertions.assertFalse(localTaskManager.getAllTask().isEmpty());
        Assertions.assertEquals(taskManager.getAllTask().get(1),localTaskManager.getAllTask().get(1));
        Assertions.assertEquals(taskManager.getAllTask().get(2),localTaskManager.getAllTask().get(2));
        Assertions.assertEquals(taskManager.getAllTask().get(3),localTaskManager.getAllTask().get(3));
        Assertions.assertTrue(localTaskManager.history().isEmpty());
    }

    @Test
    void shouldRecoverTaskDataWithoutSubTask() throws Exception {
        Task localTask = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                1, TaskStatus.NEW);
        EpicTask localEpicTask = new EpicTask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", 2, TaskStatus.NEW);
        taskManager.add(localTask);
        taskManager.add(localEpicTask);
        Path pathTest = Path.of(System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt");
        TaskManager localTaskManager = new FileBacketTaskManager(Managers.getDefaultHistory(),pathTest);
        Assertions.assertFalse(localTaskManager.getAllTask().isEmpty());
        Assertions.assertEquals(taskManager.getTask(1),localTaskManager.getTask(1));
        Assertions.assertEquals(taskManager.getTask(2),localTaskManager.getTask(2));
        Assertions.assertIterableEquals(taskManager.history(),localTaskManager.history());
    }
    }
