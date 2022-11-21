import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Restorer {

    /**
     * Метод загружает данные в список задач и в историю запросов из файла
     * historyPartInFile - маркер указывающий что в файле начался блок с исторей запросов
     *
     * @param path
     * @param fileBacketTaskManager
     * @throws IOException
     */
    public static void dataLoader(Path path, InMemoryTaskManager fileBacketTaskManager) throws ManagerSaveException {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toString()))) {
                boolean historyPartInFile = false;
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    historyPartInFile = historyLoader(historyPartInFile, line, fileBacketTaskManager);
                    taskMapLoader(line, fileBacketTaskManager);
                }
            } catch (Exception e) {
                throw new ManagerSaveException("База задач не была загружена из файла");
            }
    }

    /**
     * метод загрузки данных в хранилище запросов
     * когда строка равна "request history" маркер historyPartInFile меняется на true,
     * что означает, что след строка будет история запросов
     *
     * @param historyPart
     * @param line
     * @param fileBacketTaskManager
     * @return
     */
    private static boolean historyLoader(boolean historyPart, String line, InMemoryTaskManager fileBacketTaskManager) {
        if (historyPart) {
            String[] history = line.split(",");
            for (String idForHistory : history) {
                int taskId = Integer.parseInt(idForHistory);
                fileBacketTaskManager.getHistoryManager().addTask(fileBacketTaskManager.getAllTask().get(taskId));
            }
        }
        if (line.equals("requesthistory")) {
            return true;
        }
        return false;
    }

    /**
     * Метод загрузки данных в хранилище задач
     * по history[0] определяется тип задачи
     * у Epic может не быть подзадач, по этому нужна проверка (history.length == 5) или нет
     *
     * @param line
     * @param fileBacketTaskManager
     */
    private static void taskMapLoader(String line, InMemoryTaskManager fileBacketTaskManager) throws Exception {
        if (line.startsWith("typetask")) {
            return;
        }
        String[] history = line.split("\\.");
        String taskType = history[0];
        int taskId = Integer.parseInt(history[1]);
        String name = history[2];
        String description =  history[3];
        TaskStatus status = TaskStatus.valueOf(history[4]);
        String startTime = history[5];
        long duration = Long.parseLong(history[6]);
        Map<Integer, AbstractTask> taskMap = fileBacketTaskManager.getAllTask();
        boolean withDateOrNot = history.length > 6;
        if (withDateOrNot) {
            switch (taskType) {
                case "TASK":
                    taskMap.put(taskId, new Task(name, description, taskId, status, startTime, duration));
                    break;
                case "EPICTASK":
                    if (history.length == 7) {
                        taskMap.put(taskId, new Epictask(name, description, taskId, status, startTime, duration));
                    } else {
                        List<Integer> subTaskList = Arrays.stream(history[7].split(","))
                                .map(str -> Integer.parseInt(str))
                                .collect(Collectors.toList());
                        taskMap.put(taskId, new Epictask(name, description, taskId, status, startTime, duration, subTaskList));
                    }
                    break;
                case "SUBTASK":
                    taskMap.put(taskId, new Subtask(name, description,
                            taskId, status, startTime, duration, Integer.parseInt(history[7]) ));
                    break;
            }
        } else {
            if (taskType.equals("TASK")) {
                taskMap.put(taskId, new Task(name, description, taskId, status));
            } else if (taskType.equals("EPICTASK")) {
                if (history.length == 5) {
                    taskMap.put(taskId, new Epictask(name, description, taskId, status));
                } else {
                    List<Integer> subTaskList = Arrays.stream(startTime.split(","))
                            .map(str -> Integer.parseInt(str))
                            .collect(Collectors.toList());
                    taskMap.put(taskId, new Epictask(name, description, taskId, status, subTaskList));
                }
            } else if (taskType.equals("SUBTASK")) {
                taskMap.put(taskId, new Subtask(name, description, taskId, status, Integer.parseInt(startTime)));
            }
        }
    }

    public static void saveToFile(Writer writer, FileBacketTaskManager manager) throws Exception {
        Saveable saveTask = () -> {
            StringBuilder allTask = new StringBuilder();
            writer.write("typetask.id.name.description.status.execution start time." +
                    "task duration_min.id epic/subtask\n");
            for (AbstractTask task : manager.getAllTask().values()) {
                allTask.append(task.toString() + "\n");
            }
            writer.write(allTask + "\n");
        };
        Saveable saveRequestHistory = () -> {
            if (manager.history() != null) {
                writer.write("\nrequesthistory\n");
                StringBuilder hist = new StringBuilder();
                for (int i = 0; i < manager.history().size(); i++) {
                    hist.append(i == manager.history().size() - 1 ?
                            String.valueOf(manager.history().get(i).getTaskId()) :
                            manager.history().get(i).getTaskId() + ",");
                }
                writer.write(hist.toString());
            } else {
                writer.write("");
            }
        };
        saveTask.save();
        saveRequestHistory.save();
    }
}
