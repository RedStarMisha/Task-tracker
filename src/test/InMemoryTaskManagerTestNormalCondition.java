import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class InMemoryTaskManagerTestNormalCondition extends TaskManagerTest {

    @BeforeEach
    void createTaskManager() throws Exception {
        taskManager = Managers.getDefault();
    }

    @Override  @Test
    void shouldAddThreeTask() throws ManagerSaveException {
        super.createAndAddThreeTask();
        super.shouldAddThreeTask();
    }

    @Override  @Test
    void shouldGetThreeTaskAndRecordInHistory() throws Exception {
        super.createAndAddThreeTask();
        super.shouldGetThreeTaskAndRecordInHistory();
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

    @Override  @Test
    void shouldGetAllTask() throws ManagerSaveException {
        super.createAndAddThreeTask();
        super.shouldGetAllTask();
    }

    @Override  @Test
    void shouldClearTaskMap() throws ManagerSaveException {
        super.createAndAddThreeTask();
        super.shouldClearTaskMap();
    }

    @Override  @Test
    void shouldDeteteSimpleTaskWithIdOne() throws IOException, ManagerSaveException {
        super.createAndAddThreeTask();
        super.shouldDeteteSimpleTaskWithIdOne();
    }

    @Override  @Test
    void shouldDeteteEpicTaskWithIdTwo() throws IOException, ManagerSaveException {
        super.createAndAddThreeTask();
        super.shouldDeteteEpicTaskWithIdTwo();
    }

    @Override  @Test
    void shouldReturnEpicIdFromSubTask() throws ManagerSaveException {
        super.createAndAddThreeTask();
        super.shouldReturnEpicIdFromSubTask();
    }

    @Override  @Test
    void shouldChangeEpicStatusToSubTaskStatus() throws Exception {
        super.createAndAddThreeTask();
        super.shouldChangeEpicStatusToSubTaskStatus();
    }
}
