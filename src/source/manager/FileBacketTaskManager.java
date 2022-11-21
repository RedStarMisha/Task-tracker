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

    private Path path = Path.of(System.getProperty("user.home") + "\\IdeaProjects\\java-sprint2-hw\\files\\back.txt");



    /**
     * флаг, определяющий будет ли применено восстановление из файла
     */
     public static boolean RECOVERY = true;

    public FileBacketTaskManager() throws Exception {
        super();
        fileRecoveryChecker();
    }

    public FileBacketTaskManager(String newPath) throws Exception {
        super();
        if (newPath != null) {
            path = Path.of(newPath);
            fileRecoveryFromPath(newPath);
        }
    }

    /**
     * Метод проверяет создан ли файл. Если файла нет, создает его,
     * а если файл есть, то загружает из него информацию через класс Restorer. Стандартный
     * класс InMemoryTaskManager поддерживает подгрузку данных из файла,
     * но не поддерживает сохранение в него
     *
     * @throws IOException
     */

    protected void fileRecoveryChecker() throws IOException, ManagerSaveException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        } else if (RECOVERY) {
            Restorer.dataLoader(path, this);
        }
    }

    protected void fileRecoveryFromPath(String pathStr) throws ManagerSaveException, IOException {
        if (pathStr == null) {
            return;
        }
        Files.createFile(Path.of(pathStr));
        Restorer.dataLoader(Path.of(pathStr), this);
    }

    @Override
    public void add(AbstractTask task) throws ManagerSaveException, AddEmptyElementException, ExceptionTaskIntersection {
        super.add(task);
        try {
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractTask getTask(int id) throws ManagerSaveException, NoSuchElementException {
        if (taskMap.containsKey(id)) {
            this.getHistoryManager().addTask(taskMap.get(id));
            try {
                save();
            } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTaskStatus(int id, TaskStatus status) throws ManagerSaveException {
        super.updateTaskStatus(id, status);
        try {
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод сохраняет список задач и историю запросов
     * в файл
     */
    public void save() throws Exception {
            try (Writer writer = new FileWriter(path.toString())) {
                Restorer.saveToFile(writer, this);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new ManagerSaveException("Данные не были сохранены");
            }
    }
}
