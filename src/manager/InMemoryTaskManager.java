package manager;

import taskmodel.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager {
    private int id = 1;
    List<AbstractTask> historyList = new LinkedList<>();

    private Map<Integer, Task> simpleTaskMap;
    private Map<Integer, EpicTask> epicTaskMap;
    private Map<Integer, SubTask> subTaskMap;

    private Map<Integer, AbstractTask> taskMap;

    public InMemoryTaskManager() {
        simpleTaskMap = new HashMap<>();
        epicTaskMap = new HashMap<>();
        subTaskMap = new HashMap<>();

        taskMap = new HashMap<>();
    }



    /**
     * Добавляет задачу соответсвующего типа в хранилище соответсвующего типа
     */

    public void addTask(AbstractTask task) {
        if (task instanceof Task) {
            addTaskToSimpleTaskMap((Task) task);
        } else if (task instanceof EpicTask) {
            addTaskToEpicTaskMap((EpicTask)  task);
        } else if (task instanceof SubTask) {
            addTaskToSubTaskMap((SubTask) task);
        }
    }

    private void addTaskToSimpleTaskMap(Task newTask) {
        simpleTaskMap.put(newTask.getTaskId(), newTask);
        System.out.println("Задача '" + newTask.getTaskName() + "' добавлена, id = " + newTask.getTaskId());
    }

    private void addTaskToEpicTaskMap(EpicTask newTask) {
        epicTaskMap.put(newTask.getTaskId(), newTask);
        System.out.println("Эпическая задача '" + newTask.getTaskName() + "' добавлена, id = " + newTask.getTaskId());
    }

    private void addTaskToSubTaskMap(SubTask newTask) {
        subTaskMap.put(newTask.getTaskId(), newTask);
        System.out.println("Подзадача '" + newTask.getTaskName() + "' добавлена, id = " + newTask.getTaskId());
    }


    public int setIdNumeration() {
        return id++;
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
    /**
     * Возвращает задачу соответсвующего типа по id
     */

    public Task getSimpleTask(int id) {
        for (int taskId : simpleTaskMap.keySet()) {
            if (taskId == id) {
                addToHistoryList(simpleTaskMap.get(id));
                return simpleTaskMap.get(id);
            }
        }
        throw new RuntimeException();
    }

    public EpicTask getEpicTask(int id) {
        for (int taskId : epicTaskMap.keySet()) {
            if (taskId == id) {
                addToHistoryList(epicTaskMap.get(id));
                return epicTaskMap.get(id);
            }
        }
        throw new RuntimeException();
    }

    public SubTask getSubTask(int id) {
        for (int taskId : subTaskMap.keySet()) {
            if (taskId == id) {
                addToHistoryList(subTaskMap.get(id));
                return subTaskMap.get(id);
            }
        }
        throw new RuntimeException();
    }

    /**
     * Обновляет статус задачи соответствующего типа.
     * В качестве параметра принимается объект с уже обновленным статусом
     */
    public void updateTaskStatus(Object object) {
        if (object.getClass() == Task.class) {
            updateSimpleTaskStatus((Task) object);
        } else if (object.getClass() == EpicTask.class) {
            EpicTask localEpicTask = (EpicTask) object;
            if (localEpicTask.getTaskStatus() != epicTaskMap.get(localEpicTask.getTaskId()).getTaskStatus()) {
                System.out.println("Сначала обновите статус подзадач");
            }
        } else if (object.getClass() == SubTask.class) {
            updateSubTaskStatus((SubTask) object);
        } else {
            System.out.println("Такого класса задач нет");
        }
    }

    private void updateSimpleTaskStatus(Task simpleTask) {
        simpleTaskMap.replace(simpleTask.getTaskId(), simpleTask);
    }

    private void updateEpicTaskStatus(EpicTask epicTask) {
        epicTaskMap.replace(epicTask.getTaskId(), epicTask);
    }

    private void updateSubTaskStatus(SubTask subTask) {
        subTaskMap.replace(subTask.getTaskId(), subTask);
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

    /**
     * Удаляет задачу соответсвующего типа
     */
    public void deteteTask(int id) {
        if (simpleTaskMap.containsKey(id)) {
            deleteSimpleTask(id);
        } else if (epicTaskMap.containsKey(id)) {
            deleteEpicTask(id);
        } else if (subTaskMap.containsKey(id)) {
            deleteSubTask(id);
        } else {
            System.out.println("Задача не найдена");
        }
    }

    private void deleteSubTask(int id) {
        int epicTaskId = subTaskMap.get(id).getEpicTask().getTaskId();
        subTaskMap.get(id).getEpicTask().getSubTaskList().remove(id);
        subTaskMap.remove(id);
        if (epicTaskMap.get(epicTaskId).getSubTaskList().isEmpty()) {
            deleteEpicTask(epicTaskId);
        }
    }

    private void deleteSimpleTask(int id) {
        simpleTaskMap.remove(id);
    }

    private void deleteEpicTask(int id) {
        if (!epicTaskMap.get(id).getSubTaskList().isEmpty()) {
            for (SubTask localSubTask : epicTaskMap.get(id).getSubTaskList().values()) {
                if (localSubTask.getEpicTask().getTaskId() == id) {
                    subTaskMap.remove(localSubTask.getTaskId());
                }
            }
        }
        epicTaskMap.remove(id);
    }

    private void addToHistoryList(AbstractTask task) {
        if (historyList.size() == 10) {
            historyList.remove(0);
            historyList.add(task);
        } else {
            historyList.add(task);
        }
    }


    public List history() {
        return historyList;
    }
}
