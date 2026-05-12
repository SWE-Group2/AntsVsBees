package ants;

import core.Bee;
import strategy.SlowEffect;

/**
 * A ThrowerAnt that slows bees instead of damaging them. Uses Strategy Pattern via SlowEffect. Uses Template Method
 * Pattern - only overrides applyEffect().
 *
 * Food Cost: 4, Armor: 1
 */
public class SlowThrowerAnt extends ThrowerAnt {
    // number of turns to slow the bee for
    private static final int SLOW_TURNS = 3;

    /**
     * Creates a new SlowThrowerAnt. Post-condition: food cost is 4, armor is 1, effect is SlowEffect
     */
    public SlowThrowerAnt() {
        super();
        this.foodCost = 4;
        this.armor = 1;
        this.damage = 0;
        // use SlowEffect strategy instead of DamageEffect
        this.effect = new SlowEffect(SLOW_TURNS);
    }

    /**
     * Applies slow effect to the target bee. Overrides ThrowerAnt's applyEffect using Strategy Pattern.
     * 
     * @param target
     *            The bee to slow
     */
    @Override
    protected void applyEffect(Bee target) {
        effect.apply(target);
    }
}