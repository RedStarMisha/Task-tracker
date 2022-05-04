public class AddEmptyElementException extends Exception {

    public AddEmptyElementException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Ошибка: " + this.getMessage();
    }
}
