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

    abstract void createTaskManager() throws Exception;

    void createAndAddThreeTask() throws ManagerSaveException, AddEmptyElementException {
        task = new Task("Приготовить ужин", "Запечь рыбу в духовке",
                taskManager.setIdNumeration(), TaskStatus.NEW);
        epicTask = new EpicTask("Убраться на кухне",
                "Необходимо провести полную уборку кухни", taskManager.setIdNumeration(), TaskStatus.NEW);
        subTask = new SubTask("Помыть посуду", "Посуда должна быть чистой",
                taskManager.setIdNumeration(), TaskStatus.IN_PROGRESS,2);
        taskManager.add(task);
        taskManager.add(epicTask);
        taskManager.add(subTask);
    }


    @Test
    void shouldAddThreeTask() throws ManagerSaveException, AddEmptyElementException {
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
        System.out.println(taskManager.history().size());
        Assertions.assertEquals(task, taskManager.history().get(2));
        Assertions.assertEquals(epicTask , taskManager.getTask(2));
        System.out.println(taskManager.history().size());
        Assertions.assertEquals(epicTask, taskManager.history().get(2));
        Assertions.assertEquals(subTask , taskManager.getTask(3));
        Assertions.assertEquals(subTask, taskManager.history().get(2));
    }

    @Test
    void shouldGetTaskWithIncorrectId() throws ManagerSaveException, AddEmptyElementException {
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
    void shouldGetAllTask() throws ManagerSaveException, AddEmptyElementException {
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
    void shouldGetAllTaskForEmptyTaskMap() throws ManagerSaveException, AddEmptyElementException {
        Assertions.assertTrue(taskManager.getAllTask().isEmpty());
    }

    @Test
    void shouldClearTaskMap() throws ManagerSaveException, AddEmptyElementException {
        taskManager.clearTaskMap();
        Assertions.assertTrue(taskManager.getAllTask().isEmpty());
    }

    @Test
    void shouldDeteteSimpleTaskWithIdOne() throws IOException, ManagerSaveException, AddEmptyElementException {
        taskManager.deteteTask(1);
        Assertions.assertFalse(taskManager.getAllTask().containsKey(1));
        Assertions.assertEquals(2,taskManager.getAllTask().size());
    }

    @Test
    void shouldDeteteEpicTaskWithIdTwo() throws IOException, ManagerSaveException, AddEmptyElementException {
        taskManager.deteteTask(2);
        Assertions.assertFalse(taskManager.getAllTask().containsKey(2));
        Assertions.assertFalse(taskManager.getAllTask().containsKey(3));
        Assertions.assertEquals(1,taskManager.getAllTask().size());
    }

    @Test
    void shouldDeleteTaskStatusForNonExistingId() throws ManagerSaveException, IOException {
        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> taskManager.deteteTask(0)
        );
        Assertions.assertEquals("Задачи с таким id не существует",exception.getMessage());
    }


    @Test
    void shouldReturnEpicIdFromSubTask() throws ManagerSaveException, AddEmptyElementException {
        Assertions.assertEquals(epicTask.getTaskId(),subTask.getEpicTaskId());
    }

}