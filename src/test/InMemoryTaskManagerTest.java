import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void createTaskManager() throws Exception {
        taskManager = Managers.getDefault();
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

    @Test @Override
    void shouldGetAllTaskForEmptyTaskMap() throws ManagerSaveException, AddEmptyElementException {
        super.shouldGetAllTaskForEmptyTaskMap();
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
}
