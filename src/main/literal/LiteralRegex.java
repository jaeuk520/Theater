package literal;

public class LiteralRegex {
    public static final String MAIN_INPUT = "\\s*[0-3]\\s*$";
    public static final String MANAGE_INPUT = "\\s*[0-57-9]\\s*$";
    public static final String RESERVATION_INPUT = "\\s*[0-57-8]\\s*$";

    public static final String MOVIE_NAME = "\\s*.*\\s*";
    public static final String RUNNING_TIME = "\\s*[1-9][0-9]*\\s*";
    public static final String FILE_DELIMITER = "(\\r\\n)|(\\n)|(\\r)|(\\$)";
    public static final String CONSOLE_DELIMITER = "(\\r\\n)|(\\n)|(\\r)";
    public static final String MOVIE_ID = "\\s*[A-Za-z0-9]{4}\\s*";
}
