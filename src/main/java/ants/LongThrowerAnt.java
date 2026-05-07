package ants;

import core.Bee;

public class LongThrowerAnt extends ThrowerAnt {
    /** Creates a new LongThrowerAnt. Post-condition: food cost is 3, armor is 1, and damage is 1 */
    public LongThrowerAnt() {
        super();
        this.foodCost = 3;
        this.armor = 1;
        this.damage = 1;
    }

    /**
     * Returns the nearest bee within range to throw at. Post-condition: returns a Bee or null if no bee in range
     */
    @Override
    public Bee getTarget() {
        return place.getClosestBee(4, Integer.MAX_VALUE);
    }
}
