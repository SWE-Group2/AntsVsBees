package exceptions;

/**
 * Thrown when there is not enough food to deploy an ant.
 */
public class InsufficientFoodException extends RuntimeException {
    public InsufficientFoodException(int required, int available) {
        super("Not enough food to deploy ant. Required: " + required + ", Available: " + available);
    }
}