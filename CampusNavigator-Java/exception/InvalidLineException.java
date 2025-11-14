package exception;

public class InvalidLineException extends Exception {
    public InvalidLineException() {
        super();
    }

    public InvalidLineException(String file, int lineNumber) {
        super(String.format("Invalid line for %s. Skipping this line %d from the %s file.", file, lineNumber, file));
    }

    public InvalidLineException(String message) {
        super(message);
    }
}

