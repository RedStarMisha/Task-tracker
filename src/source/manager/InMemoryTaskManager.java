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
    public static HistoryManager getHistoryManager() {
        return historyManager;
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public void add(AbstractTask task) throws ManagerSaveException, AddEmptyElementException {
            if (task == null) {
                throw new AddEmptyElementException("Нельзя добавить пустую задачу");
            }
            taskMap.put(task.getTaskId(), task);
            if (task instanceof SubTask) {
                addSubTask((SubTask)task);
            }
            System.out.println("Задача '" + task.getTaskName() + "' добавлена, id = " + task.getTaskId());
    }

    private void addSubTask(SubTask task) {
        EpicTask epicForSubTask = (EpicTask) taskMap.get(task.getEpicTaskId());
        epicForSubTask.addSubTask(task.getTaskId());
        epicStatusChecker(task.getEpicTaskId(), task.getTaskStatus());
    }

    @Override
    public AbstractTask getTask(int id) throws NoSuchElementException, ManagerSaveException {
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
        if (id == Integer.MAX_VALUE) {
            throw new ArrayIndexOutOfBoundsException("Список задач заполнен");
        }
        return id++;
    }

    @Override
    public void clearTaskMap() {
        taskMap.clear();
        id = 1;
    }

    public void updateTaskStatus(int id, TaskStatus status) throws ManagerSaveException, NoSuchElementException {
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

    private void updateSubTaskStatus(SubTask subTask, TaskStatus statusForNewSubTask) {
        taskMap.replace(subTask.getTaskId(), new SubTask(subTask, statusForNewSubTask));
        epicStatusChecker(subTask.getEpicTaskId(), statusForNewSubTask);
    }

    private void epicStatusChecker (Integer epicId, TaskStatus statusForNewSubTask) {
        EpicTask epicForSubTask = (EpicTask) taskMap.get(epicId);
        if (epicForSubTask.getTaskStatus() != statusForNewSubTask) {
            boolean statusNew = true;
            boolean statusDone = true;
            for (int subTaskId : ((EpicTask) taskMap.get(epicId)).getSubTaskListId()) {
                if (taskMap.get(subTaskId).getTaskStatus() != TaskStatus.NEW) {
                    statusNew = false;
                }
                if (taskMap.get(subTaskId).getTaskStatus() != TaskStatus.DONE){
                    statusDone = false;
                }
            }
            if (statusNew) {
                updateEpicTaskStatus(epicForSubTask, TaskStatus.NEW);
            } else if(statusDone) {
                updateEpicTaskStatus(epicForSubTask, TaskStatus.DONE);
            } else {
                updateEpicTaskStatus(epicForSubTask, TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public void deteteTask(int id) throws NoSuchElementException, ManagerSaveException {
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

    public List<AbstractTask> history() throws NoSuchElementException {
        return historyManager.getHistory();
    }

}
