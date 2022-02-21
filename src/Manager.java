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

    public void clearToDoList () {
        simpleTaskMap.clear();
        epicTaskMap.clear();
        subTaskMap.clear();
    }

    public Object showTask (int id) {
        for (int taskId: simpleTaskMap.keySet()) {
            if (taskId == id) {
                return simpleTaskMap.get(id);
            }
        }
        for (int taskId: epicTaskMap.keySet()) {
            if (taskId == id) {
                return epicTaskMap.get(id);
            }
        }
        for (int taskId : subTaskMap.keySet()) {
            if (taskId == id) {
                return subTaskMap.get(id);
            }
        }
        return "Такой задачи нет";
    }

  /*  public void updateStatus (SubTask subTask) {
        toDoList.replace(subTask.getTaskId(), subTask);
    }

    public void checkEpicTask () {
        for (int keyNumber : toDoList.keySet()) {
            Task localTask = (Task) toDoList.get(keyNumber);

        }
    }*/
}
