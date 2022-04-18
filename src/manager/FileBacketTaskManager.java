package manager;

import taskmodel.AbstractTask;
import taskmodel.EpicTask;
import taskmodel.Task;
import taskmodel.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileBacketTaskManager extends InMemoryTaskManager {

    Path path = Path.of("C:\\Users\\Valya\\IdeaProjects\\java-sprint2-hw\\files\\back.txt");
    Set<Integer> savedStringIfFile = new HashSet<>();


    public FileBacketTaskManager(HistoryManager historyManager) throws IOException {
        super(historyManager);
        fileChecker();
    }

    private void fileChecker() throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        } else {
            dataLoader();
        }
    }

    public void dataLoader() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toString()));
        boolean historyPart = false;
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            if (line.equals("requesthistory")) {
                historyPart = true;
            }
            if (historyPart) {
                String [] history = line.split(",");
                for(String idForHistory:history){
                    //historyManager.addTask(taskMap.get(Integer.parseInt(idForHistory)));
                }
                break;
            }
            taskMapLoader(line);
        }
        bufferedReader.close();
    }

    private void taskMapLoader(String line) {
        if (line.startsWith("typetask")) {
            return;
        }
        String [] history = line.split(".");
        if (history[0].equals("Task")) {
            taskMap.put(Integer.parseInt(history[1]),new Task(history[2],history[3],Integer.parseInt(history[1]),
                    TaskStatus.valueOf(history[4])));
        } else if(history[0].equals("EpicTask")) {
           // taskMap.put(Integer.parseInt(history[1]),new EpicTask(history[2],history[3],Integer.parseInt(history[1]),
                    //TaskStatus.valueOf(history[4]), Arrays.asList(history[5].split(","))));
        }

    }

    @Override
    public void add(AbstractTask task) throws IOException {
        super.add(task);
        save();
    }

    @Override
    public AbstractTask getTask(int id) throws IOException {
        AbstractTask task = super.getTask(id);
        save();
        return task;
    }

    private void save() throws IOException {
        try(Writer writer = new FileWriter(path.toString())){
            writer.write("typetask.id.name.description.status.id epic/subtask\n");
            for (AbstractTask task:taskMap.values()) {
                writer.write(task.toString() + "\n");
            }
            writer.write("\nrequesthistory\n");
            for (int i = 0; i < history().size(); i++){
                writer.write(i == history().size() - 1 ?
                        String.valueOf(history().get(i).getTaskId()):history().get(i).getTaskId() + ",");
            }
        } catch (Exception e) {
        }
    }
}
