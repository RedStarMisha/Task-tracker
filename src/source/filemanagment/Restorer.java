import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
     * когда строка равна "requesthistory" маркер historyPartInFile меняется на true,
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
        Map<Integer, AbstractTask> taskMap = fileBacketTaskManager.getAllTask();
        if (history.length > 6) {
            mapLoaderWithDate(history, taskMap);
        } else {
            mapLoaderWithoutDate(history, taskMap);
        }

    }

    private static void mapLoaderWithDate(String[] history , Map<Integer, AbstractTask> taskMap) throws Exception {
        if (history[0].equals("TASK")) {
            taskMap.put(Integer.parseInt(history[1]), new Task(history[2], history[3], Integer.parseInt(history[1]),
                    TaskStatus.valueOf(history[4]), history[5], Long.parseLong(history[6])));
        } else if (history[0].equals("EPICTASK")) {
            if (history.length == 7) {
                taskMap.put(Integer.parseInt(history[1]), new Epictask(history[2], history[3],
                        Integer.parseInt(history[1]), TaskStatus.valueOf(history[4]), history[5],
                        Long.parseLong(history[6])));
            } else {
                List<Integer> subTaskList = new ArrayList<>();
                for (String subTaskId : history[7].split(",")) {
                    subTaskList.add(Integer.parseInt(subTaskId));
                }
                taskMap.put(Integer.parseInt(history[1]), new Epictask(history[2], history[3],
                        Integer.parseInt(history[1]), TaskStatus.valueOf(history[4]), history[5],
                        Long.parseLong(history[6]), subTaskList));
            }
        } else if (history[0].equals("SUBTASK")) {
            taskMap.put(Integer.parseInt(history[1]), new Subtask(history[2], history[3],
                    Integer.parseInt(history[1]), TaskStatus.valueOf(history[4]), history[5],
                    Long.parseLong(history[6]), Integer.parseInt(history[7]) ));
        }
    }

    private static void mapLoaderWithoutDate(String[] history , Map<Integer, AbstractTask> taskMap) {
        if (history[0].equals("TASK")) {
            taskMap.put(Integer.parseInt(history[1]), new Task(history[2], history[3], Integer.parseInt(history[1]),
                    TaskStatus.valueOf(history[4])));
        } else if (history[0].equals("EPICTASK")) {
            if (history.length == 5) {
                taskMap.put(Integer.parseInt(history[1]), new Epictask(history[2],
                        history[3], Integer.parseInt(history[1]), TaskStatus.valueOf(history[4])));
            } else {
                List<Integer> subTaskList = new ArrayList<>();
                for (String subTaskId : history[5].split(",")) {
                    subTaskList.add(Integer.parseInt(subTaskId));
                }
                taskMap.put(Integer.parseInt(history[1]), new Epictask(history[2], history[3],
                        Integer.parseInt(history[1]), TaskStatus.valueOf(history[4]), subTaskList));
            }
        } else if (history[0].equals("SUBTASK")) {
            taskMap.put(Integer.parseInt(history[1]), new Subtask(history[2], history[3],
                    Integer.parseInt(history[1]), TaskStatus.valueOf(history[4]), Integer.parseInt(history[5])));
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
                            String.valueOf(manager.history().get(i).getTaskId()) : manager.history().get(i).getTaskId() + ",");
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
