package manager;

import filemanagment.Restorer;
import myexception.ManagerSaveException;
import taskmodel.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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
    protected void fileRecoveryChecker() throws IOException, ManagerSaveException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        } else {
            Restorer.dataLoader(path, this);
        }
    }

    @Override
    public void add(AbstractTask task) throws ManagerSaveException {
        super.add(task);
        save();
    }

    @Override
    public AbstractTask getTask(int id) throws ManagerSaveException, NoSuchElementException {
        if (taskMap.containsKey(id)) {
            historyManager.addTask(taskMap.get(id));
            save();
            return taskMap.get(id);
        }
        throw new NoSuchElementException("Задачи с таким id не существует");
    }

    @Override
    public void deteteTask(int id) throws NoSuchElementException, ManagerSaveException {
        super.deteteTask(id);
        save();
    }

    @Override
    public void updateTaskStatus(int id, TaskStatus status) throws ManagerSaveException {
        super.updateTaskStatus(id, status);
        save();
    }

    /**
     * Метод сохраняет список задач и историю запросов
     * в файл
     */
    public void save() throws ManagerSaveException {
            try (Writer writer = new FileWriter(path.toString())) {
                writer.write("typetask.id.name.description.status.id epic/subtask\n");
                for (AbstractTask task : taskMap.values()) {
                    writer.write(task.toString() + "\n");
                }
                if (history() != null) {
                    writer.write("\nrequesthistory\n");
                    for (int i = 0; i < history().size(); i++) {
                        writer.write(i == history().size() - 1 ?
                                String.valueOf(history().get(i).getTaskId()) : history().get(i).getTaskId() + ",");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new ManagerSaveException("Данные не были сохранены");
            }
    }
}
