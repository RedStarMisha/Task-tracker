import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;


public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected Map<Integer, AbstractTask> taskMap = new HashMap<>();
    private final HistoryManager historyManager;


    /**
     * TreeSet был использован как тип т.к. в методе поиска пересечений использовались методы lower и higher
     * если нужно именно перейти на более высокий уровень абстракции, то я тогда все переделаю. Пока оставил так
     */
    private TreeSet<AbstractTask> sortedTask = new TreeSet<>((o1, o2) -> {
        if (o1 == null) return 1;
        if (o2 == null) return -1;
        if (o1.getStartTime() == null && o2.getStartTime() == null) {
            return o1.getTaskId() - o2.getTaskId();
        } else if (o2.getStartTime() == null) {
            return -1;
        } else if (o1.getStartTime() == null) {
            return 1;
        } else if (Duration.between(o1.getStartTime(),o2.getStartTime()).toMinutes() == 0) {
            return o1.getTaskId() - o2.getTaskId();
        }
        return (int)Duration.between(o1.getStartTime(),o2.getStartTime()).toMinutes();
    });

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public void add(AbstractTask task) throws ManagerSaveException, AddEmptyElementException, ExceptionTaskIntersection {
        if (task == null) {
            throw new AddEmptyElementException("Нельзя добавить пустую задачу");
        }
        taskIntersectionChecker(task);
        taskMap.put(task.getTaskId(), task);
        TaskSorter.add(sortedTask , task);
        if (task instanceof SubTask) {
            addSubTask((SubTask) task);
        }
        System.out.println("Задача '" + task.getTaskName() + "' добавлена, id = " + task.getTaskId());
    }

    private void addSubTask(SubTask task) {
        EpicTask epicForSubTask = (EpicTask) taskMap.get(task.getEpicTaskId());
        dateEpicChecker(epicForSubTask, task);
        epicForSubTask.addSubTask(task.getTaskId());
        epicStatusChecker(task.getEpicTaskId(), task.getTaskStatus());
    }

    private void dateEpicChecker(EpicTask epicTask, SubTask task) {
        if (task.getDuration() != null && task.getStartTime() != null) {
            if (epicTask.getStartTime() == null || task.getStartTime().isBefore(epicTask.getStartTime())) {
                epicTask.setStartTime(task.getStartTime());
            }
            if (epicTask.getEndTime() == null || epicTask.getEndTime().isBefore(task.getEndTime())) {
                epicTask.setEndTime(task.getEndTime());
            }
            epicTask.setDuration(Duration.between(epicTask.getStartTime(),epicTask.getEndTime()));
        }
    }

    private void taskIntersectionChecker(AbstractTask task) throws ExceptionTaskIntersection {
        if (task instanceof EpicTask || task.getStartTime() == null) {
            return;
        }
        AbstractTask lowerTask = sortedTask.lower(task);
        AbstractTask higher = sortedTask.higher(task);
        if (lowerTask != null && lowerTask.getEndTime().isAfter(task.getStartTime()) &&
                lowerTask.getEndTime().isBefore(task.getEndTime()) ||
            higher != null && higher.getEndTime().isAfter(task.getStartTime()) &&
                    higher.getEndTime().isBefore(task.getEndTime())) {
            throw new ExceptionTaskIntersection("Задачи пересекаются. Измените условия");
        }
    }

    @Override
    public AbstractTask getTask(int id) throws NoSuchElementException, ManagerSaveException {
            containsKeyFromTaskMap(id);
            historyManager.addTask(taskMap.get(id));
            return taskMap.get(id);
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
        sortedTask.clear();
        id = 1;
    }

    public void updateTaskStatus(int id, TaskStatus status) throws ManagerSaveException, NoSuchElementException {
        containsKeyFromTaskMap(id);
        switch (taskMap.get(id).getTaskType()) {
            case TASK:
                taskMap.replace(id, new Task(taskMap.get(id), status));
                break;
            case EPICTASK:
                System.out.println("Сначала обновите статус подзадач");
                break;
            case SUBTASK:
                SubTask updatedSubtask = new SubTask(taskMap.get(id), status);
                taskMap.replace(id, updatedSubtask);
                epicStatusChecker(updatedSubtask.getEpicTaskId(), status);
                break;
            default:
                throw new NoSuchElementException("Задачи с таким типом не существует");
        }
    }

    private void epicStatusChecker(int epicId, TaskStatus statusForNewSubTask) {
        EpicTask updatedEpic = (EpicTask) taskMap.get(epicId);
        if (updatedEpic.getTaskStatus() != statusForNewSubTask) {
            Set<TaskStatus> subtaskStatusChecker = new HashSet<>();
            updatedEpic.getSubTaskListId().forEach(ind -> subtaskStatusChecker.add(taskMap.get(ind).getTaskStatus()));
//            for (int subtaskId : updatedEpic.getSubTaskListId()) {
//                subtaskStatusChecker.add(taskMap.get(subtaskId).getTaskStatus());
//            }
            if (subtaskStatusChecker.contains(TaskStatus.NEW) && subtaskStatusChecker.size() == 1) {
                taskMap.replace(epicId, new EpicTask(updatedEpic, TaskStatus.NEW));
            } else if (subtaskStatusChecker.contains(TaskStatus.DONE) && subtaskStatusChecker.size() == 1) {
                taskMap.replace(epicId, new EpicTask(updatedEpic, TaskStatus.DONE));
            } else {
                taskMap.replace(epicId, new EpicTask(updatedEpic, TaskStatus.IN_PROGRESS));
            }
        }
    }

    @Override
    public void deteteTask(int id) throws NoSuchElementException, ManagerSaveException {
        containsKeyFromTaskMap(id);
        Consumer<Integer> stepToRemove = (taskId) -> {
            taskMap.remove(taskId);
            historyManager.remove(taskId);
            TaskSorter.remove(sortedTask,taskMap.get(taskId));
        };
        if (taskMap.get(id) instanceof EpicTask) {
            ((EpicTask) taskMap.get(id)).getSubTaskListId().stream().forEach(stepToRemove);
        }
        stepToRemove.accept(id);
    }



    public TreeSet<AbstractTask> getSortedTask() {
        return sortedTask;
    }

    public List<AbstractTask> history() throws NoSuchElementException {
        return historyManager.getHistory();
    }

    private void containsKeyFromTaskMap(int id) {
        if (!taskMap.containsKey(id)) {
            throw new NoSuchElementException("Задачи с таким id не существует");
        }
    }
}
