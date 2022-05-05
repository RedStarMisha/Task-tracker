import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


public abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;
    Task task;
    EpicTask epicTask;
    SubTask subTask;
    SubTask subTaskTwo;

    abstract void createTaskManager() throws Exception;

    void createAndAddThreeTaskWithoutDate() throws Exception {
        task = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                1, TaskStatus.NEW);
        epicTask = new EpicTask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", 2, TaskStatus.NEW);
        subTask = new SubTask("Помыть посуду", "Посуда должна быть чистой",
                3, TaskStatus.IN_PROGRESS,2);
        taskManager.add(task);
        taskManager.add(epicTask);
        taskManager.add(subTask);
    }



    @Test
    void shouldGetAllTaskForEmptyTaskMap() throws Exception {
        Assertions.assertTrue(taskManager.getAllTask().isEmpty());
    }

    @Test
    void shouldAddThreeTask() throws Exception {
        Assertions.assertEquals(3,taskManager.getAllTask().size());
        Assertions.assertNotNull(taskManager.getAllTask());
        Assertions.assertEquals(task, taskManager.getAllTask().get(1));
        Assertions.assertEquals(epicTask , taskManager.getAllTask().get(2));
        Assertions.assertEquals(subTask , taskManager.getAllTask().get(3));

    }

    @Test
    void shouldAddNullTask() {
        final AddEmptyElementException exception = Assertions.assertThrows(
                AddEmptyElementException.class,
                () -> taskManager.add(null));
        Assertions.assertEquals("Нельзя добавить пустую задачу",exception.getMessage());
    }


    @Test
    void shouldGetThreeTaskAndRecordInHistory() throws Exception {
        Assertions.assertEquals(task , taskManager.getTask(1));
        Assertions.assertEquals(task, taskManager.history().get(taskManager.history().size() - 1));
        Assertions.assertEquals(epicTask , taskManager.getTask(2));
        Assertions.assertEquals(epicTask, taskManager.history().get(taskManager.history().size() - 1));
        Assertions.assertEquals(subTask , taskManager.getTask(3));
        Assertions.assertEquals(subTask, taskManager.history().get(taskManager.history().size() - 1));
    }

    @Test
    void shouldGetTaskWithIncorrectId() throws Exception {
        final NoSuchElementException exceptionWithMaxInt = Assertions.assertThrows(
                NoSuchElementException .class,
                () -> taskManager.getTask(0));
        Assertions.assertEquals("Задачи с таким id не существует", exceptionWithMaxInt.getMessage());
    }

    @Test
    void shouldCheckNumeration() throws Exception {
        Assertions.assertTrue(taskManager.getAllTask().containsKey(1));
        Assertions.assertEquals( taskManager.getTask(1), task);
        Assertions.assertTrue(taskManager.getAllTask().containsKey(2));
        Assertions.assertEquals( taskManager.getTask(2), epicTask);
        Assertions.assertTrue(taskManager.getAllTask().containsKey(3));
        Assertions.assertEquals( taskManager.getTask(3), subTask);
    }

    @Test
    void shouldUpdateTaskStatusInSimpleTask() throws Exception {
        taskManager.updateTaskStatus(1, TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(taskManager.getTask(1).getTaskStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldUpdateTaskStatusInSubTaskAndEpicTask() throws Exception {
        taskManager.updateTaskStatus(3, TaskStatus.DONE);
        Assertions.assertEquals(taskManager.getTask(3).getTaskStatus(), TaskStatus.DONE);
        Assertions.assertEquals(taskManager.getTask(2).getTaskStatus(), TaskStatus.DONE);
    }

    @Test
    void shouldUpdateTaskStatusForNonExistingId() throws ManagerSaveException {
        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> taskManager.updateTaskStatus(0, TaskStatus.DONE)
        );
        Assertions.assertEquals("Задачи с таким id не существует",exception.getMessage());
    }


    @Test
    void shouldGetAllTask() throws Exception {
        Map<Integer, AbstractTask> map = new HashMap<>();
        map.put(1,task);
        map.put(2,epicTask);
        map.put(3,subTask);
        Assertions.assertEquals(map.size(),taskManager.getAllTask().size());
        for (Integer localId : map.keySet()) {
            Assertions.assertEquals(map.get(localId),taskManager.getAllTask().get(localId));
        }
    }

    @Test
    void shouldClearTaskMap() throws Exception {
        taskManager.clearTaskMap();
        Assertions.assertTrue(taskManager.getAllTask().isEmpty());
    }

    @Test
    void shouldDeteteSimpleTaskWithIdOne() throws Exception {
        taskManager.deteteTask(1);
        Assertions.assertFalse(taskManager.getAllTask().containsKey(1));
        Assertions.assertEquals(2,taskManager.getAllTask().size());
    }

    @Test
    void shouldDeteteEpicTaskWithIdTwo() throws Exception {
        taskManager.deteteTask(2);
        Assertions.assertFalse(taskManager.getAllTask().containsKey(2));
        Assertions.assertFalse(taskManager.getAllTask().containsKey(3));
        Assertions.assertEquals(1,taskManager.getAllTask().size());
    }

    @Test
    void shouldDeleteTaskForNonExistingId() throws ManagerSaveException, IOException {
        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> taskManager.deteteTask(0)
        );
        Assertions.assertEquals("Задачи с таким id не существует",exception.getMessage());
    }

    @Test
    void shouldDeleteSubtaskWhenDeleteEpicInHistoryManager() throws Exception {
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.deteteTask(2);
        Assertions.assertEquals(1,taskManager.history().size());
    }

    @Test
    void shouldReturnEpicIdFromSubTask() throws Exception {
        Assertions.assertEquals(epicTask.getTaskId(),subTask.getEpicTaskId());
    }

}