package exception;

public class InvalidFormatException extends Exception {
    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String file, int lineNumber, String field) {
        super(String.format("Invalid %s format. Skipping this line %d from the %s file.", field, lineNumber, file));
    }

    public InvalidFormatException(String message) {
        super(message);
    }
}

