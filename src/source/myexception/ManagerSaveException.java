public class ManagerSaveException extends Exception {

    public ManagerSaveException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Ошибка: " + this.getMessage();
    }
}
