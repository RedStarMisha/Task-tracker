import java.time.format.DateTimeFormatter;

public class Formater {

    public static DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");

    public static String firstLettertoUpperCase(String word) {

        char firstLetter = word.charAt(0);
        String substring = word.substring(1);
        char capitalFirstLetter = Character.toUpperCase(firstLetter);
        return capitalFirstLetter + substring.toLowerCase();
    }

}
