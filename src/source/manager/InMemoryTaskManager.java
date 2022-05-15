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
        sortedTask.clear();
        id = 1;
    }

    public void updateTaskStatus(int id, TaskStatus status) throws ManagerSaveException, NoSuchElementException {
        if (taskMap.get(id) instanceof Task) {
            updateSimpleTaskStatus((Task) taskMap.get(id), status);
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

    private void epicStatusChecker(Integer epicId, TaskStatus statusForNewSubTask) {
        EpicTask epicForSubTask = (EpicTask) taskMap.get(epicId);
        if (epicForSubTask.getTaskStatus() != statusForNewSubTask) {
            boolean statusNew = true;
            boolean statusDone = true;
            for (int subTaskId : ((EpicTask) taskMap.get(epicId)).getSubTaskListId()) {
                if (taskMap.get(subTaskId).getTaskStatus() != TaskStatus.NEW) {
                    statusNew = false;
                }
                if (taskMap.get(subTaskId).getTaskStatus() != TaskStatus.DONE) {
                    statusDone = false;
                }
            }
            if (statusNew) {
                updateEpicTaskStatus(epicForSubTask, TaskStatus.NEW);
            } else if (statusDone) {
                updateEpicTaskStatus(epicForSubTask, TaskStatus.DONE);
            } else {
                updateEpicTaskStatus(epicForSubTask, TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public void deteteTask(int id) throws NoSuchElementException, ManagerSaveException {
        if (!taskMap.containsKey(id)) {
            throw new NoSuchElementException("Задачи с таким id не существует");
        }
        Consumer<Integer> stepToRemove = (taskId) -> {
            taskMap.remove(taskId);
            historyManager.remove(taskId);
            TaskSorter.remove(sortedTask,taskMap.get(taskId));
        };
        if (taskMap.get(id) instanceof EpicTask) {
            for (Integer subTaskid : ((EpicTask) taskMap.get(id)).getSubTaskListId()) {
                stepToRemove.accept(subTaskid);
            }
        }
        stepToRemove.accept(id);
    }

    public TreeSet<AbstractTask> getSortedTask() {
        return sortedTask;
    }

    public List<AbstractTask> history() throws NoSuchElementException {
        return historyManager.getHistory();
    }
}
