package ants;

import core.Bee;
import strategy.StunEffect;

/**
 * A ThrowerAnt that stuns bees instead of damaging them. Uses Strategy Pattern via StunEffect. Uses Template Method
 * Pattern - only overrides applyEffect().
 *
 * Food Cost: 6, Armor: 1
 */
public class StunThrowerAnt extends ThrowerAnt {
    // number of turns to stun the bee for
    private static final int STUN_TURNS = 1;

    /**
     * Creates a new StunThrowerAnt. Post-condition: food cost is 6, armor is 1, effect is StunEffect
     */
    public StunThrowerAnt() {
        super();
        this.foodCost = 6;
        this.armor = 1;
        this.damage = 0;
        // use StunEffect strategy instead of DamageEffect
        this.effect = new StunEffect(STUN_TURNS);
    }

    /**
     * Applies stun effect to the target bee. Overrides ThrowerAnt's applyEffect using Strategy Pattern.
     * 
     * @param target
     *            The bee to stun
     */
    @Override
    protected void applyEffect(Bee target) {
        effect.apply(target);
    }
}