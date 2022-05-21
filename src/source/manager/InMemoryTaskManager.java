import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;


public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected Map<Integer, AbstractTask> taskMap = new HashMap<>();
    private HistoryManager historyManager;


    /**
     * TreeSet был использован как тип т.к. в методе поиска пересечений использовались методы lower и higher
     * если нужно именно перейти на более высокий уровень абстракции, то я тогда все переделаю. Пока оставил так
     */
    private TreeSet<AbstractTask> sortedTask = new TreeSet<>((o1, o2) ->
        Comparator.comparing(AbstractTask::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
        .thenComparing(AbstractTask::getTaskId).compare(o1, o2));

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void add(AbstractTask task) throws ManagerSaveException, AddEmptyElementException, ExceptionTaskIntersection {
        if (task == null) {
            throw new AddEmptyElementException("Нельзя добавить пустую задачу");
        }
        taskIntersectionChecker(task);
        taskMap.put(task.getTaskId(), task);
        TaskSorter.add(sortedTask , task);
        if (task instanceof Subtask) {
            addSubTask((Subtask) task);
        }
        System.out.println("Задача '" + task.getTaskName() + "' добавлена, id = " + task.getTaskId());
    }

    private void addSubTask(Subtask task) {
        Epictask epicForSubTask = (Epictask) taskMap.get(task.getEpicTaskId());
        dateEpicChecker(epicForSubTask, task);
        epicForSubTask.addSubTask(task.getTaskId());
        epicStatusChecker(task.getEpicTaskId(), task.getTaskStatus());
    }

    private void dateEpicChecker(Epictask epicTask, Subtask task) {
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
        if (task instanceof Epictask || task.getStartTime() == null) {
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
        historyManager = Managers.getDefaultHistory();
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
                Subtask updatedSubtask = new Subtask(taskMap.get(id), status);
                taskMap.replace(id, updatedSubtask);
                epicStatusChecker(updatedSubtask.getEpicTaskId(), status);
                break;
            default:
                throw new NoSuchElementException("Задачи с таким типом не существует");
        }
    }

    private void epicStatusChecker(int epicId, TaskStatus statusForNewSubTask) {
        Epictask updatedEpic = (Epictask) taskMap.get(epicId);
        if (updatedEpic.getTaskStatus() != statusForNewSubTask) {
            Set<TaskStatus> subtaskStatusChecker = new HashSet<>();
            updatedEpic.getSubTaskListId().forEach(ind -> subtaskStatusChecker.add(taskMap.get(ind).getTaskStatus()));
            if (subtaskStatusChecker.contains(TaskStatus.NEW) && subtaskStatusChecker.size() == 1) {
                taskMap.replace(epicId, new Epictask(updatedEpic, TaskStatus.NEW));
            } else if (subtaskStatusChecker.contains(TaskStatus.DONE) && subtaskStatusChecker.size() == 1) {
                taskMap.replace(epicId, new Epictask(updatedEpic, TaskStatus.DONE));
            } else {
                taskMap.replace(epicId, new Epictask(updatedEpic, TaskStatus.IN_PROGRESS));
            }
        }
    }

    @Override
    public void deteteTask(int id) throws NoSuchElementException, ManagerSaveException {
        containsKeyFromTaskMap(id);
        Consumer<Integer> stepToRemove = (taskId) -> {
            TaskSorter.remove(sortedTask, taskMap.get(taskId));
            taskMap.remove(taskId);
            historyManager.remove(taskId);
        };
        if (taskMap.get(id) instanceof Epictask) {
            ((Epictask) taskMap.get(id)).getSubTaskListId().stream().forEach(stepToRemove);
        } else if (taskMap.get(id) instanceof Subtask) {
            Subtask subtask = (Subtask) taskMap.get(id);
            Epictask epictask = (Epictask) taskMap.get(subtask.getEpicTaskId());
            epictask.getSubTaskListId().remove(Integer.valueOf(id));
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
