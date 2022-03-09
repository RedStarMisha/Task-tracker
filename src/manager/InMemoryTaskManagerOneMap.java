package manager;

import taskmodel.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManagerOneMap implements TaskManager {
    private int id = 1;
    List<AbstractTask> historyList = new LinkedList<>();

    private Map<Integer, AbstractTask> taskMap;

    public InMemoryTaskManagerOneMap() {
        taskMap = new HashMap<>();
    }

    public void add(AbstractTask task) {
        if (task instanceof SubTask) {
            EpicTask localEpic = (EpicTask) taskMap.get(((SubTask) task).getEpicTaskId());
            localEpic.addSubTask((SubTask) task);
        }
        taskMap.put(task.getTaskId(), task);
        System.out.println("Задача '" + task.getTaskName() + "' добавлена, id = " + task.getTaskId());
    }

    public AbstractTask getTask(int id) {
        for (int taskId : taskMap.keySet()) {
            if (taskId == id) {
                return taskMap.get(id);
            }
        }
        throw new RuntimeException();
    }

    public Map<Integer, AbstractTask> getAllTask() {
        return taskMap;
    }

    public int setIdNumeration() {
        return id++;
    }

    public EpicTask getEpicForSubtask(int id) {
        for (int taskId : taskMap.keySet()) {
            if (taskId == id && taskMap.get(id) instanceof EpicTask) {
                return (EpicTask) taskMap.get(id);
            }
        }
        System.out.println("Такого эпика нет");
        throw new RuntimeException();
    }

    public void clearTaskMap() {
        taskMap.clear();
        id = 1;
    }

    public void updateTaskStatus(AbstractTask task) {
        if (task instanceof Task) {
            updateSimpleTaskStatus((Task) task);
        } else if (task instanceof EpicTask) {
            if (task.getTaskStatus() != taskMap.get(task.getTaskId()).getTaskStatus()) {
                System.out.println("Сначала обновите статус подзадач");
            }
        } else if (task instanceof SubTask) {
            updateSubTaskStatus((SubTask) task);
        }
    }

    private void updateSimpleTaskStatus(Task simpleTask) {
        taskMap.replace(simpleTask.getTaskId(), simpleTask);
    }

    private void updateEpicTaskStatus(EpicTask epicTask) {
        taskMap.replace(epicTask.getTaskId(), epicTask);
    }

    private void updateSubTaskStatus(SubTask subTask) {
        taskMap.replace(subTask.getTaskId(), subTask);
        subTask.getEpicTask().getSubTaskList().replace(subTask.getTaskId(), subTask);
        for (SubTask localSubTask : subTask.getEpicTask().getSubTaskList().values()) {
            if (localSubTask.getTaskStatus() == TaskStatus.IN_PROGRESS) {
                updateEpicTaskStatus(new EpicTask(subTask.getEpicTask(), TaskStatus.IN_PROGRESS));
                return;
            }
            if (localSubTask.getTaskStatus() != TaskStatus.DONE) {
                return;
            }
        }
        updateEpicTaskStatus(new EpicTask(subTask.getEpicTask(), TaskStatus.DONE));
    }
}
