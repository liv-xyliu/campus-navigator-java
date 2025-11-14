package exception;

public class MovementBlockedException extends Exception {
    public MovementBlockedException() {
        super();
    }

    public MovementBlockedException(String message) {
        super(message);
    }

    public static MovementBlockedException hitEdge() {
        return new MovementBlockedException("You have hit the edge of the map.");
    }

    public static MovementBlockedException cannotEnter() {
        return new MovementBlockedException("You cannot enter that area.");
    }
}

