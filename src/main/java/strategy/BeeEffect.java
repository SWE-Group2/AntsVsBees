package strategy;

import core.Bee;

/**
 * Strategy interface for effects that can be applied to a Bee. This is the Strategy Pattern - different effects
 * implement this interface. Examples: damage, slow, stun.
 */
public interface BeeEffect {
    /**
     * Applies the effect to the given bee. Pre-condition: bee must not be null
     * 
     * @param bee
     *            The bee to apply the effect to
     */
    void apply(Bee bee);

    /**
     * Returns a description of this effect for logging purposes.
     * 
     * @return description of the effect
     */
    String describe();
}