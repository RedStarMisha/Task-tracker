import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void createTaskManager() throws Exception {
        taskManager = Managers.getDefault();
    }

    @Override  @Test
    void shouldAddThreeTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldAddThreeTask();
    }

    @Test @Override
    void shouldAddNullTask() {
        super.shouldAddNullTask();
    }


    @Override  @Test
    void shouldGetThreeTaskAndRecordInHistory() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldGetThreeTaskAndRecordInHistory();
    }

    @Override @Test
    void shouldGetTaskWithIncorrectId() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldGetTaskWithIncorrectId();
    }

    @Override  @Test
    void shouldGetAllTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldGetAllTask();
    }

    @Test @Override
    void shouldGetAllTaskForEmptyTaskMap() throws Exception {
        super.shouldGetAllTaskForEmptyTaskMap();
    }

    @Override  @Test
    void shouldCheckNumeration() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldCheckNumeration();
    }

    @Override  @Test
    void shouldUpdateTaskStatusInSimpleTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldUpdateTaskStatusInSimpleTask();
    }

    @Override  @Test
    void shouldUpdateTaskStatusInSubTaskAndEpicTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldUpdateTaskStatusInSubTaskAndEpicTask();
    }

    @Test @Override
    void shouldUpdateTaskStatusForNonExistingId() throws ManagerSaveException {
        super.shouldUpdateTaskStatusForNonExistingId();
    }

    @Override  @Test
    void shouldClearTaskMap() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldClearTaskMap();
    }

    @Override  @Test
    void shouldDeteteSimpleTaskWithIdOne() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldDeteteSimpleTaskWithIdOne();
    }

    @Override  @Test
    void shouldDeteteEpicTaskWithIdTwo() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldDeteteEpicTaskWithIdTwo();
    }

    @Override @Test
    void shouldDeleteSubtaskWhenDeleteEpicInHistoryManager() throws Exception {
        createAndAddThreeTaskWithoutDate();
        super.shouldDeleteSubtaskWhenDeleteEpicInHistoryManager();
    }

    @Test @Override
    void shouldDeleteTaskForNonExistingId() throws ManagerSaveException, IOException {
        super.shouldDeleteTaskForNonExistingId();
    }


    @Override  @Test
    void shouldReturnEpicIdFromSubTask() throws Exception {
        super.createAndAddThreeTaskWithoutDate();
        super.shouldReturnEpicIdFromSubTask();
    }

}
