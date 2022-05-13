import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileBacketTaskManagerWithDateTest extends TaskManagerWithDateTest{

    @BeforeEach
    void createTaskManager() throws Exception {
        taskManager = Managers.getFileManager();
    }

    @Override @Test
    void shouldGetEndTimeEpicTaskWhenStartTimeOfSubTaskBeforeEpic() throws Exception {
        createAndAddThreeTaskWithDate();
        super.shouldGetEndTimeEpicTaskWhenStartTimeOfSubTaskBeforeEpic();
    }

    @Override @Test
    void shouldGetDurationEpicTaskWhenStartTimeOfSubTaskBeforeEpic() throws Exception {
        createAndAddThreeTaskWithDate();
        super.shouldGetDurationEpicTaskWhenStartTimeOfSubTaskBeforeEpic();
    }

    @Override @Test
    void shouldThrowExceptionWhenDurationIsZero() throws Exception {
        super.shouldThrowExceptionWhenDurationIsZero();
    }

    @Override @Test
    void shouldThrowExceptionWhenDurationIsNegativeNumber() throws Exception {
        super.shouldThrowExceptionWhenDurationIsNegativeNumber();
    }

    @Test
    void shouldRecoverDateInfoFromFile() throws Exception {
        super.createAndAddThreeTaskWithDate();
        String pathTest = System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt";
        TaskManager localTaskManager = new FileBacketTaskManager(Managers.getDefaultHistory(), pathTest);
        assertAll(
                () -> assertFalse(localTaskManager.getAllTask().isEmpty()),
                () -> assertEquals(taskManager.getTask(1).getStartTime(),
                localTaskManager.getTask(1).getStartTime()),
                () -> assertEquals(taskManager.getTask(2).getStartTime(),
                        localTaskManager.getTask(2).getStartTime()),
                () -> assertEquals(taskManager.getTask(3).getStartTime(),
                        localTaskManager.getTask(3).getStartTime()),
                () -> assertEquals(taskManager.getTask(4).getStartTime(),
                        localTaskManager.getTask(4).getStartTime())
        );
    }
}
