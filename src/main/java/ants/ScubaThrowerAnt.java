package ants;

public class ScubaThrowerAnt extends ThrowerAnt {
    /**
     * Creates a new ScubaThrowerAnt. Post-condition: food cost is 5, armor is 1, and watersafe is true
     */
    public ScubaThrowerAnt() {
        super();
        this.foodCost = 5;
        this.armor = 1;
        this.watersafe = true;
    }
}
