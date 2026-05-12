package exceptions;

/**
 * Thrown when an ant cannot be placed in a given location. For example, a non-watersafe ant being placed in a
 * WaterPlace.
 */
public class InvalidPlacementException extends RuntimeException {
    public InvalidPlacementException(String message) {
        super(message);
    }
}