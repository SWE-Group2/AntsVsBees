package ants;

import core.Ant;
import core.AntColony;
import core.Bee;

public class NinjaAnt extends Ant {
    protected int damage;

    /** Creates a new NinjaAnt. Post-condition: food cost is 5, armor is 1, and does not block bees */
    public NinjaAnt() {
        super(1);
        this.foodCost = 6;
        this.blocker = false;
        this.damage = 1;
    }

    /**
     * Returns the nearest bee within range to throw at. Post-condition: returns a Bee or null if no bee in range
     */
    public Bee getTarget() {
        return place.getClosestBee(0, 0);
    }

    /**
     * Attacks the nearest bee in range. Pre-condition: colony must not be null Post-condition: target bee loses armor
     * equal to damage
     */
    public void action(AntColony colony) {
        assert colony != null : "Colony cannot be null";
        Bee target = getTarget();
        if (target != null) {
            if (isQueenNearby()) {
                System.out.println("Queen is nearby! NinjaAnt does double damage.");
                target.reduceArmor(this.damage * 2);
                return;
            }
            target.reduceArmor(this.damage);
            System.out.println("NinjaAnt attacked " + target);
        }
    }
}
