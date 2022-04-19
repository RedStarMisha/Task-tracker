package manager;

import filemanagment.Restorer;
import taskmodel.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected Map<Integer, AbstractTask> taskMap = new HashMap<>();
    protected static HistoryManager historyManager;
    Path path = Path.of(System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt");
    public static HistoryManager getHistoryManager() {
        return historyManager;
    }

    public InMemoryTaskManager(HistoryManager historyManager) throws Exception {
        this.historyManager = historyManager;
        fileRecoveryChecker();
    }

    protected void fileRecoveryChecker() throws IOException {
        if (Files.exists(path)) {
            Restorer.dataLoader(path , this);
        }
    }

    @Override
    public void add(AbstractTask task) throws IOException {
        if (task instanceof SubTask) {
            EpicTask epicForSubTask = (EpicTask) taskMap.get(((SubTask) task).getEpicTaskId());
            epicForSubTask.addSubTask(task.getTaskId());
        }
        taskMap.put(task.getTaskId(), task);
        System.out.println("Задача '" + task.getTaskName() + "' добавлена, id = " + task.getTaskId());
    }

    @Override
    public AbstractTask getTask(int id) throws IOException {
        if (taskMap.containsKey(id)) {
            historyManager.addTask(taskMap.get(id));
            return taskMap.get(id);
        }
        throw new NoSuchElementException("Задачи с таким id не существует");
    }

    @Override
    public Map<Integer, AbstractTask> getAllTask() {
        return taskMap;
    }

    @Override
    public int setIdNumeration() {
        return id++;
    }

    @Override
    public void clearTaskMap() {
        taskMap.clear();
        id = 1;
    }

    public void updateTaskStatus(int id, TaskStatus status) {
        if (taskMap.get(id) instanceof Task) {
            updateSimpleTaskStatus((Task) taskMap.get(id) , status);
        } else if (taskMap.get(id) instanceof EpicTask) {
            if (taskMap.get(id).getTaskStatus() != status) {
                System.out.println("Сначала обновите статус подзадач");
            }
        } else if (taskMap.get(id) instanceof SubTask) {
            updateSubTaskStatus((SubTask) taskMap.get(id), status);
        } else {
            throw new NoSuchElementException("Задачи с таким id не существует");
        }
    }

    private void updateSimpleTaskStatus(Task simpleTask, TaskStatus status) {
        taskMap.replace(simpleTask.getTaskId(), new Task(simpleTask, status));
    }

    private void updateEpicTaskStatus(EpicTask epicTask, TaskStatus status) {
        taskMap.replace(epicTask.getTaskId(), new EpicTask(epicTask, status));
    }

    private void updateSubTaskStatus(SubTask subTask, TaskStatus status) {
        taskMap.replace(subTask.getTaskId(), new SubTask(subTask, status));
        epicStatusChecker(subTask.getEpicTaskId(), status);
    }

    private void epicStatusChecker (Integer epicId, TaskStatus status) {
        boolean epicDoneOrNot = true;
        EpicTask epicForSubTask = (EpicTask) taskMap.get(epicId);
        if (status != TaskStatus.NEW
                && epicForSubTask.getTaskStatus() == TaskStatus.NEW) {
            updateEpicTaskStatus(epicForSubTask, TaskStatus.IN_PROGRESS);
            return;
        }
        for (int subTaskId : epicForSubTask.getSubTaskListId()) {
            if (taskMap.get(subTaskId).getTaskStatus() != TaskStatus.DONE) {
                epicDoneOrNot = false;
            }
        }
        if (epicDoneOrNot) {
            updateEpicTaskStatus(epicForSubTask, TaskStatus.DONE);
        }
    }

    @Override
    public void deteteTask(int id) throws IOException {
        if (taskMap.containsKey(id)) {
            if (taskMap.get(id) instanceof EpicTask) {
                for (Integer subTaskid : ((EpicTask) taskMap.get(id)).getSubTaskListId()) {
                    taskMap.remove(subTaskid);
                    historyManager.remove(subTaskid);
                }
            }
            taskMap.remove(id);
            historyManager.remove(id);
        } else {
            throw new NoSuchElementException("Задачи с таким id не существует");
        }
    }

    public List<AbstractTask> history() {
        return historyManager.getHistory();
    }

}
