import TaskPackage.*;

import java.util.HashMap;
import java.util.Map;

public class Manager {
    static int idNumeration = 1;
    private Map<Integer , Task> simpleTaskMap;
    private Map<Integer, EpicTask> epicTaskMap;
    private Map<Integer, SubTask> subTaskMap;

    public Manager () {
        simpleTaskMap = new HashMap<>();
        epicTaskMap = new HashMap<>();
        subTaskMap = new HashMap<>();
    }

    public void addTaskToSimpleTaskMap (Task newTask) {
        simpleTaskMap.put(newTask.getTaskId(), newTask);
        System.out.println("Задача " + newTask.getTaskName() + " добавлена, id = " + newTask.getTaskId());
    }

    public void addTaskToEpicTaskMap (EpicTask newTask) {
        epicTaskMap.put(newTask.getTaskId(), newTask);
        System.out.println("Эпическая задача " + newTask.getTaskName() + " добавлена, id = " + newTask.getTaskId());
    }

    public void addTaskToSubTaskMap (SubTask newTask) {
        subTaskMap.put(newTask.getTaskId(), newTask);
        System.out.println("Подзадача " + newTask.getTaskName() + " добавлена, id = " + newTask.getTaskId());
    }

    public static int setIdNumeration() {
        return idNumeration++;
    }

    public Map<Integer, Task> getSimpleTaskMap() {
        return simpleTaskMap;
    }

    public Map<Integer, EpicTask> getEpicTaskMap() {
        return epicTaskMap;
    }

    public Map<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public void clearSimpleTaskMap() {
        simpleTaskMap.clear();
    }

    public void clearEpicAndSubTaskMap() {
        epicTaskMap.clear();
        subTaskMap.clear();
    }
    
    public int findTaskId (String taskName) {
        for (int taskId: simpleTaskMap.keySet()) {
            if (simpleTaskMap.get(taskId).getTaskName().equals(taskName)) {
                return taskId;
            }
        }
        for (int taskId: epicTaskMap.keySet()) {
            if (epicTaskMap.get(taskId).getTaskName().equals(taskName)) {
                return taskId;
            }
        }
        for (int taskId: subTaskMap.keySet()) {
            if (subTaskMap.get(taskId).getTaskName().equals(taskName)) {
                return taskId;
            }
        }
        throw new RuntimeException();
    }

    public Task getSimpleTask(int id) {
        for (int taskId: simpleTaskMap.keySet()) {
            if (taskId == id) {
                return simpleTaskMap.get(id);
            }
        }
        throw new RuntimeException();
    }

    public EpicTask getEpicTask(int id) {
        for (int taskId: epicTaskMap.keySet()) {
            if (taskId == id) {
                return epicTaskMap.get(id);
            }
        }
        throw new RuntimeException();
    }

    public SubTask getSubTask(int id) {
        for (int taskId : subTaskMap.keySet()) {
            if (taskId == id) {
                return subTaskMap.get(id);
            }
        }
        throw new RuntimeException();
    }


   public void updateSimpleTaskStatus (Task simpleTask) {
        simpleTaskMap.replace(simpleTask.getTaskId(),simpleTask);
       System.out.println("Статус задачи " + simpleTask.getTaskName() + " обновлен");
    }

    public void updateEpicTaskStatus (EpicTask epicTask) {

        if (epicTask.getTaskStatus()==TaskStatus.IN_PROGRESS) {
            System.out.println("Обновите статус подзадачи");
            return;
        } else {
            simpleTaskMap.replace(epicTask.getTaskId(),epicTask);
            for (SubTask localSubTask : epicTask.getSubTaskList().values()) {
                updateSubTaskStatus(new SubTask(localSubTask, TaskStatus.DONE));
            }
            System.out.println("Статус задачи " + epicTask.getTaskName() + " обновлен");
        }

    }

    public void updateSubTaskStatus (SubTask subTask) {
        boolean doneEpicTask = true;
        simpleTaskMap.replace(subTask.getTaskId(),subTask);
        System.out.println("Статус задачи " + subTask.getTaskName() + " обновлен");
        for (SubTask localSubTask:subTask.getEpicTask().getSubTaskList().values()) {
            if (localSubTask.getTaskStatus()==TaskStatus.IN_PROGRESS) {
                updateEpicTaskStatus(new EpicTask(subTask.getEpicTask(), TaskStatus.IN_PROGRESS));
                return;
            }
            if (localSubTask.getTaskStatus()!=TaskStatus.DONE) {
                doneEpicTask = false;
            }
        }
        if (doneEpicTask) {
            updateEpicTaskStatus(new EpicTask(subTask.getEpicTask(), TaskStatus.DONE));
        }

    }
/*
    public void checkEpicTask () {
        for (int keyNumber : toDoList.keySet()) {
            Task localTask = (Task) toDoList.get(keyNumber);

        }
    }*/
}
