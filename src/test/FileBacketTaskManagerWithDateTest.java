import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class FileBacketTaskManagerWithDateTest extends TaskManagerWithDateTest{

    @BeforeEach
    void createTaskManager() throws Exception {
        taskManager = Managers.getFileManager(true);
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
        Path pathTest = Path.of(System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt");
        TaskManager localTaskManager = new FileBacketTaskManager(Managers.getDefaultHistory(), pathTest);
        Assertions.assertFalse(localTaskManager.getAllTask().isEmpty());
        Assertions.assertEquals(taskManager.getTask(1).getStartTime(),
                localTaskManager.getTask(1).getStartTime());
        Assertions.assertEquals(taskManager.getTask(2).getStartTime(),
                localTaskManager.getTask(2).getStartTime());
        Assertions.assertEquals(taskManager.getTask(3).getStartTime(),
                localTaskManager.getTask(3).getStartTime());
        Assertions.assertEquals(taskManager.getTask(4).getStartTime(),
                localTaskManager.getTask(4).getStartTime());
    }
}
