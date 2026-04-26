package ants;

import core.Ant;
import core.AntColony;

/**
 * A WallAnt is an ant that does nothing but has a large armor value.
 * It acts as a shield to block bees from reaching the queen.
 * 
 * Food Cost: 4, Armor: 4
 * 
 * @author muhammad hassan
 */
public class WallAnt extends Ant
{
    /**
     * Creates a new WallAnt.
     * Post-condition: food cost is 4, armor is 4
     */
    public WallAnt()
    {
        super(4); // armor = 4
        this.foodCost = 4;
    }

    /**
     * WallAnt does nothing on its turn.
     * Pre-condition: colony must not be null
     * Post-condition: no change to colony or game state
     */
    @Override
    public void action(AntColony colony)
    {
        assert colony != null : "Colony cannot be null";
        // WallAnt does nothing - it just blocks bees with its high armor
    }
}