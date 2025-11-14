package exception;

public class DataNotFoundException extends Exception {
    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String file, int lineNumber, String field) {
        super(String.format("%s cannot be empty. Skipping this line %d from the %s file.", capitalize(field), lineNumber, file));
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

