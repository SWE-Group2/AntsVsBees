package ants;

import core.Ant;
import core.AntColony;

/**
 * An Ant that harvests food for the colony.
 * Food Cost: 2, Armor: 1
 * Action: Produces 1 food per turn for the colony.
 */
public class HarvesterAnt extends Ant
{
    /**
     * Creates a new HarvesterAnt.
     * Pre-condition: colony must not be null
     * Post-condition: food cost is 2, armor is 1
     */
    public HarvesterAnt()
    {
        super(1); // armor = 1
        this.foodCost = 2;
    }

    /**
     * Harvests 1 food for the colony each turn.
     * Pre-condition: colony must not be null
     * Post-condition: colony food increases by 1
     */
    public void action(AntColony colony)
    {
        assert colony != null : "Colony cannot be null";
        colony.increaseFood(1);
        System.out.println("HarvesterAnt harvested 1 food. Total food: " + colony.getFood());
    }
}