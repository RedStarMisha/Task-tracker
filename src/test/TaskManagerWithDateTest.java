import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerWithDateTest<T extends TaskManager> {

    public T taskManager;
    Task task;
    EpicTask epicTask;
    SubTask subTask;
    SubTask subTaskTwo;


    void createAndAddThreeTaskWithDate() throws Exception {
        task = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                1, TaskStatus.NEW,"12-01-2022, 16:00",50);
        epicTask = new EpicTask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", 2, TaskStatus.NEW,
                "13-01-2022, 16:00", 120);
        subTask = new SubTask("Помыть посуду", "Посуда должна быть чистой",
                3, TaskStatus.IN_PROGRESS,"13-01-2022, 15:00",
                20, 2);
        subTaskTwo = new SubTask("Помыть посуду", "Посуда должна быть чистой",
                4, TaskStatus.IN_PROGRESS,"14-01-2022, 15:00",
                20, 2);
        taskManager.add(task);
        taskManager.add(epicTask);
        taskManager.add(subTask);
        taskManager.add(subTaskTwo);
    }

    @Test
    void shouldGetEndTimeEpicTaskWhenStartTimeOfSubTaskBeforeEpic() throws Exception {
        LocalDateTime endTimeEpicTask = LocalDateTime.parse("14-01-2022, 15:00",
                Formater.FORMATTER_DATE).plus(Duration.ofMinutes(20));
        Assertions.assertEquals(endTimeEpicTask,taskManager.getTask(2).getEndTime());
    }

    @Test
    void shouldGetDurationEpicTaskWhenStartTimeOfSubTaskBeforeEpic() throws Exception {
        LocalDateTime startTimeEpicTask = LocalDateTime.parse("13-01-2022, 15:00",Formater.FORMATTER_DATE);
        LocalDateTime endTimeEpicTask = LocalDateTime.parse("14-01-2022, 15:00",
                Formater.FORMATTER_DATE).plus(Duration.ofMinutes(20));
        Duration epicDuration = Duration.between(startTimeEpicTask,endTimeEpicTask);
        Assertions.assertEquals(taskManager.getTask(2).getDuration(),epicDuration);
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZero() throws Exception {
        IllegalArgumentException  exception = Assertions.assertThrows(
                IllegalArgumentException .class,
                () ->         task = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                        taskManager.setIdNumeration(), TaskStatus.NEW,"12-01-2022, 16:00",0)
        );
        Assertions.assertEquals("Длительность должна быть больше 0", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegativeNumber() throws Exception {
        IllegalArgumentException  exception = Assertions.assertThrows(
                IllegalArgumentException .class,
                () ->         task = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                        taskManager.setIdNumeration(), TaskStatus.NEW,"12-01-2022, 16:00",-50)
        );
        Assertions.assertEquals("Длительность должна быть больше 0", exception.getMessage());
    }
}
