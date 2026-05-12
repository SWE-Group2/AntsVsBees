package strategy;

import core.Bee;

/**
 * A BeeEffect that deals damage to a bee. Implements the Strategy Pattern for the damage effect.
 */
public class DamageEffect implements BeeEffect {
    // how much damage to deal
    private final int damage;

    /**
     * Creates a new DamageEffect with the given damage amount. Pre-condition: damage must be greater than 0
     * 
     * @param damage
     *            The amount of damage to deal
     */
    public DamageEffect(int damage) {
        assert damage > 0 : "Damage must be greater than 0";
        this.damage = damage;
    }

    /**
     * Deals damage to the bee.
     * 
     * @param bee
     *            The bee to damage
     */
    @Override
    public void apply(Bee bee) {
        assert bee != null : "Bee cannot be null";
        bee.reduceArmor(damage);
    }

    @Override
    public String describe() {
        return "dealt " + damage + " damage";
    }
}