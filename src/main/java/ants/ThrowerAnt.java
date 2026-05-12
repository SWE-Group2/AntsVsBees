package ants;

import bees.GhostBee;
import core.Ant;
import core.AntColony;
import core.Bee;
import strategy.BeeEffect;
import strategy.DamageEffect;

/**
 * An Ant that throws leaves at bees. Uses the Template Method Pattern - action() defines the flow, subclasses override
 * applyEffect() to change the effect applied.
 *
 * Food Cost: 4, Armor: 1, Damage: 1
 */
public class ThrowerAnt extends Ant {
    protected int damage;

    // the effect to apply to the target bee (Strategy Pattern)
    protected BeeEffect effect;

    /**
     * Creates a new ThrowerAnt. Post-condition: food cost is 4, armor is 1, damage is 1
     */
    public ThrowerAnt() {
        super(1); // armor = 1
        this.foodCost = 4;
        this.damage = 1;
        this.effect = new DamageEffect(damage);
    }

    /**
     * Returns the nearest bee within range to throw at. Subclasses override this to change the range. Post-condition:
     * returns a Bee or null if no bee in range
     */
    public Bee getTarget() {
        return place.getClosestBee(0, 3);
    }

    /**
     * Template Method - defines the flow of the action. 1. Get target 2. Check if target is valid 3. Apply effect
     * (damage, slow, stun etc.)
     *
     * Pre-condition: colony must not be null Post-condition: effect applied to target bee if one exists
     */
    @Override
    public final void action(AntColony colony) {
        assert colony != null : "Colony cannot be null";

        Bee target = getTarget();

        // ghost bees cannot be targeted by normal throwers
        if (target instanceof GhostBee) {
            System.out.println("ThrowerAnt cannot target GhostBee!");
            return;
        }

        if (target != null) {
            // queen nearby doubles the effect damage
            if (isQueenNearby() && effect instanceof DamageEffect) {
                new DamageEffect(damage * 2).apply(target);
                System.out.println(getClass().getSimpleName() + " did double damage near the queen at " + target);
                return;
            }
            // apply the effect (Strategy Pattern)
            applyEffect(target);
            System.out.println(getClass().getSimpleName() + " threw a leaf at " + target);
        }
    }

    /**
     * Applies the effect to the target bee. Subclasses override this to change the effect (slow, stun, damage). This is
     * the hook method in the Template Method Pattern.
     *
     * @param target
     *            The bee to apply the effect to
     */
    protected void applyEffect(Bee target) {
        effect.apply(target);
    }
}