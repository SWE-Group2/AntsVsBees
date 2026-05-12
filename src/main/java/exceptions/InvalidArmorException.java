package exceptions;

/**
 * Thrown when an insect is created with invalid armor value. Replaces the generic IllegalArgumentException in Insect.
 */
public class InvalidArmorException extends RuntimeException {
    public InvalidArmorException(int armor) {
        super("Cannot create an insect with armor of " + armor + ". Armor must be greater than 0.");
    }
}