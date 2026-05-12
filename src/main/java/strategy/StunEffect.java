package strategy;

import core.Bee;

/**
 * A BeeEffect that stuns a bee for a number of turns. Implements the Strategy Pattern for the stun effect.
 */
public class StunEffect implements BeeEffect {
    // how many turns to stun the bee for
    private final int turns;

    /**
     * Creates a new StunEffect. Pre-condition: turns must be greater than 0
     * 
     * @param turns
     *            The number of turns to stun the bee for
     */
    public StunEffect(int turns) {
        assert turns > 0 : "Turns must be greater than 0";
        this.turns = turns;
    }

    /**
     * Stuns the bee for the specified number of turns.
     * 
     * @param bee
     *            The bee to stun
     */
    @Override
    public void apply(Bee bee) {
        assert bee != null : "Bee cannot be null";
        bee.stun(turns);
    }

    @Override
    public String describe() {
        return "stunned for " + turns + " turns";
    }
}