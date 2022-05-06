import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

public class FileBacketTaskManager extends InMemoryTaskManager implements Saveable {

    /**
     * Загрузка данных из файла происходит в контрукторе
     * @param historyManager
     * @throws Exception
     */
    final Path path = Path.of(System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt");

    public FileBacketTaskManager(HistoryManager historyManager, boolean shouldRecovery) throws Exception {
        super(historyManager);
        fileRecoveryChecker(shouldRecovery);
    }

    public FileBacketTaskManager(HistoryManager historyManager, Path path) throws Exception {
        super(historyManager);
        fileRecoveryFromPath(path);
    }

    /**
     * Метод проверяет создан ли файл. Если файла нет, создает его,
     * а если файл есть, то загружает из него информацию через класс Restorer. Стандартный
     * класс InMemoryTaskManager поддерживает подгрузку данных из файла,
     * но не поддерживает сохранение в него
     *
     * @throws IOException
     */

    protected void fileRecoveryChecker(boolean shouldRecovery) throws IOException, ManagerSaveException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        if (shouldRecovery && Files.exists(path)) {
            Restorer.dataLoader(path, this);
        }
    }

    protected void fileRecoveryFromPath(Path path) throws ManagerSaveException {
            Restorer.dataLoader(path, this);

    }

    @Override
    public void add(AbstractTask task) throws ManagerSaveException, AddEmptyElementException, ExceptionTaskIntersection {
        super.add(task);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractTask getTask(int id) throws ManagerSaveException, NoSuchElementException {
        if (taskMap.containsKey(id)) {
            historyManager.addTask(taskMap.get(id));
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return taskMap.get(id);
        }
        throw new NoSuchElementException("Задачи с таким id не существует");
    }

    @Override
    public void deteteTask(int id) throws NoSuchElementException, ManagerSaveException {
        super.deteteTask(id);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTaskStatus(int id, TaskStatus status) throws ManagerSaveException {
        super.updateTaskStatus(id, status);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод сохраняет список задач и историю запросов
     * в файл
     */
    public void save() throws ManagerSaveException, IOException {
            try (Writer writer = new FileWriter(path.toString())) {
                Saveable saveTask = () -> {
                    StringBuilder allTask = new StringBuilder();
                    writer.write("typetask.id.name.description.status.execution start time." +
                            "task duration_min.id epic/subtask\n");
                    for (AbstractTask task : taskMap.values()) {
                        allTask.append(task.toString() + "\n");
                    }
                    writer.write(allTask + "\n");
                };
                Saveable saveRequestHistory = () -> {
                    if (history() != null) {
                        writer.write("\nrequesthistory\n");
                        StringBuilder hist = new StringBuilder();
                        for (int i = 0; i < history().size(); i++) {
                            hist.append(i == history().size() - 1 ?
                                    String.valueOf(history().get(i).getTaskId()) : history().get(i).getTaskId() + ",");
                        }
                        writer.write(hist.toString());
                    } else {
                        writer.write("");
                    }
                };
                saveTask.save();
                saveRequestHistory.save();

            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new ManagerSaveException("Данные не были сохранены");
            }
    }
}
