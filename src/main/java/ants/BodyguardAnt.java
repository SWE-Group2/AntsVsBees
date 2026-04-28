package ants;

import core.Ant;
import core.AntColony;

/**
 * A BodyguardAnt is a special ant that can share a place with another ant.
 * It shields the other ant from damage - bees must kill the BodyguardAnt first
 * before they can hurt the ant it is protecting.
 * 
 * Food Cost: 4, Armor: 2
 * 
 * @author YOUR NAME HERE
 */
public class BodyguardAnt extends Ant
{
    // the ant that this BodyguardAnt is protecting
    private Ant containedAnt;

    /**
     * Creates a new BodyguardAnt.
     * Post-condition: food cost is 4, armor is 2, containedAnt is null
     */
    public BodyguardAnt()
    {
        super(2); // armor = 2
        this.foodCost = 4;
        this.containedAnt = null; // not protecting anyone yet
    }

    /**
     * Returns the ant being protected by this BodyguardAnt.
     * @return the contained ant, or null if not protecting anyone
     */
    public Ant getContainedAnt()
    {
        return containedAnt;
    }

    /**
     * Sets the ant that this BodyguardAnt will protect.
     * @param ant The ant to protect
     */
    public void setContainedAnt(Ant ant)
    {
        this.containedAnt = ant;
    }

    /**
     * BodyguardAnt does nothing on its turn.
     * It just stands there and takes hits for the ant it protects.
     * Pre-condition: colony must not be null
     * Post-condition: no change to colony or game state
     */
    @Override
    public void action(AntColony colony)
    {
        assert colony != null : "Colony cannot be null";
        // BodyguardAnt does nothing - it just protects the ant inside it
    }

    /**
     * Removes the BodyguardAnt from its place.
     * If it was protecting another ant, that ant stays in the place.
     */
    @Override
    public void leavePlace()
    {
        // Place.removeInsect handles moving the contained ant out
        this.place.removeInsect(this);
    }
}