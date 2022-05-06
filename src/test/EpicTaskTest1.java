import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTaskTest1 {
    TaskManager inMemoryTaskManager;

    @BeforeEach
    public void createTastManager() throws Exception {
        inMemoryTaskManager = Managers.getDefault();
        inMemoryTaskManager.add(new EpicTask("помыть кота", "кот дожен быть чистым",
                inMemoryTaskManager.setIdNumeration(), TaskStatus.NEW));
    }

    public void createTwoSubTaskWithStatus(TaskStatus fitstStatus, TaskStatus secondStatus)
            throws ManagerSaveException, AddEmptyElementException, ExceptionTaskIntersection {
        inMemoryTaskManager.add(new SubTask("Наполнить ванную", "Температура должна быть норм",
                inMemoryTaskManager.setIdNumeration(), fitstStatus, 1));
        inMemoryTaskManager.add(new SubTask("Принести полотенце", "Выбрать самое чистое полотенце",
                inMemoryTaskManager.setIdNumeration(), secondStatus, 1));
    }

    @Test
    public void shouldReturnEpicStatusWithEmptySubtaskList() throws Exception {
        Assertions.assertEquals(TaskStatus.NEW, inMemoryTaskManager.getTask(1).getTaskStatus());
    }

    @Test
    public void shouldReturnEpicStatusWithAllSubtaskNewStatus() throws Exception {
        createTwoSubTaskWithStatus(TaskStatus.NEW,TaskStatus.NEW);
        Assertions.assertEquals(TaskStatus.NEW, inMemoryTaskManager.getTask(1).getTaskStatus());
    }

    @Test
    public void shouldReturnEpicStatusWithAllSubtaskDoneStatus() throws Exception {
        createTwoSubTaskWithStatus(TaskStatus.DONE,TaskStatus.DONE);
        Assertions.assertEquals(TaskStatus.DONE, inMemoryTaskManager.getTask(1).getTaskStatus());
    }

    @Test
    public void shouldReturnEpicStatusWithSubtaskDoneAndNewStatus() throws Exception {
        createTwoSubTaskWithStatus(TaskStatus.NEW,TaskStatus.DONE);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManager.getTask(1).getTaskStatus());
    }

    @Test
    public void shouldReturnEpicStatusWithSubtaskDoneAndInProgressStatus() throws Exception {
        createTwoSubTaskWithStatus(TaskStatus.IN_PROGRESS,TaskStatus.DONE);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManager.getTask(1).getTaskStatus());
    }

    @Test
    public void shouldReturnEpicStatusWithSubtaskNewAndInProgressStatus() throws Exception {
        createTwoSubTaskWithStatus(TaskStatus.NEW,TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManager.getTask(1).getTaskStatus());
    }

    @Test
    public void shouldReturnEpicStatusWithSubtaskAllInProgressStatus() throws Exception {
        createTwoSubTaskWithStatus(TaskStatus.IN_PROGRESS,TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, inMemoryTaskManager.getTask(1).getTaskStatus());
    }
}