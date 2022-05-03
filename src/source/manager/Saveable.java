import java.io.IOException;

@FunctionalInterface
public interface Saveable {

    void save() throws IOException, ManagerSaveException;
}