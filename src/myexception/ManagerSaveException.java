package myexception;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Ошибка: " + this.getMessage();
    }
}
