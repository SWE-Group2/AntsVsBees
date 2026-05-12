package strategy;

import core.Bee;

/**
 * A BeeEffect that slows a bee for a number of turns. Implements the Strategy Pattern for the slow effect.
 */
public class SlowEffect implements BeeEffect {
    // how many turns to slow the bee for
    private final int turns;

    /**
     * Creates a new SlowEffect. Pre-condition: turns must be greater than 0
     * 
     * @param turns
     *            The number of turns to slow the bee for
     */
    public SlowEffect(int turns) {
        assert turns > 0 : "Turns must be greater than 0";
        this.turns = turns;
    }

    /**
     * Slows the bee for the specified number of turns.
     * 
     * @param bee
     *            The bee to slow
     */
    @Override
    public void apply(Bee bee) {
        assert bee != null : "Bee cannot be null";
        bee.slow(turns);
    }

    @Override
    public String describe() {
        return "slowed for " + turns + " turns";
    }
}