package manager;

import filemanagment.Restorer;
import myexception.ManagerSaveException;
import taskmodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

public class FileBacketTaskManager extends InMemoryTaskManager implements Saveable {

    /**
     * Загрузка данных из файла происходит в контрукторе
     * @param historyManager
     * @throws Exception
     */
    public FileBacketTaskManager(HistoryManager historyManager) throws Exception {
        super(historyManager);
    }

    /**
     * Метод проверяет создан ли файл. Если файла нет, создает его,
     * а если файл есть, то загружает из него информацию через класс Restorer. Стандартный
     * класс InMemoryTaskManager поддерживает подгрузку данных из файла,
     * но не поддерживает сохранение в него
     *
     * @throws IOException
     */
    @Override
    protected void fileRecoveryChecker() throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        } else {
            Restorer.dataLoader(path, this);
        }
    }

    @Override
    public void add(AbstractTask task) throws IOException {
        super.add(task);
        save();
    }

    @Override
    public AbstractTask getTask(int id) throws IOException {
        if (taskMap.containsKey(id)) {
            historyManager.addTask(taskMap.get(id));
            save();
            return taskMap.get(id);
        }
        throw new NoSuchElementException("Задачи с таким id не существует");

    }

    @Override
    public void deteteTask(int id) throws IOException {
        super.deteteTask(id);
        save();
    }

    @Override
    public void updateTaskStatus(int id, TaskStatus status) {
        super.updateTaskStatus(id, status);
        save();
    }

    /**
     * Метод сохраняет список задач и историю запросов
     * в файл
     */
    public void save() {
        try {
            try (Writer writer = new FileWriter(path.toString())) {
                writer.write("typetask.id.name.description.status.id epic/subtask\n");
                for (AbstractTask task : taskMap.values()) {
                    writer.write(task.toString() + "\n");
                }
                writer.write("\nrequesthistory\n");
                for (int i = 0; i < history().size(); i++) {
                    writer.write(i == history().size() - 1 ?
                            String.valueOf(history().get(i).getTaskId()) : history().get(i).getTaskId() + ",");
                }
            } catch (Exception e) {
                throw new ManagerSaveException("Данные не были сохранены");
            }
        } catch (ManagerSaveException m) {
            System.out.println(m);
            System.exit(0);
        }
    }
}
