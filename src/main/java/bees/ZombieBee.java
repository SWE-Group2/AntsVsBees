package bees;

import core.Ant;
import core.AntColony;
import core.Bee;
import core.Place;

/**
 * A ZombieBee is a dangerous bee that can attack up to 3 ants at once!
 * Unlike a normal bee that only stings the ant directly in its way,
 * a ZombieBee attacks the ant in its current place AND the ants in
 * the next 2 places ahead of it.
 * 
 * This makes it much more dangerous than a normal bee!
 * 
 * @author YOUR NAME HERE
 */
public class ZombieBee extends Bee
{
    // how many ants the zombie bee can attack at once
    private static final int ATTACK_RANGE = 3;

    /**
     * Creates a new ZombieBee with the given armor.
     * Post-condition: watersafe is true (bees can fly)
     * @param armor The ZombieBee's armor
     */
    public ZombieBee(int armor)
    {
        super(armor);
        this.watersafe = true;
    }

    /**
     * ZombieBee's action - attacks up to 3 ants at once!
     * It stings the ant in its current place AND ants in the next 2 places ahead.
     * If no ants are found in range, it moves forward like a normal bee.
     * 
     * Pre-condition: colony must not be null
     * Post-condition: up to 3 ants take damage, or zombie bee moves forward
     * 
     * @param colony The ant colony
     */
    @Override
    public void action(AntColony colony)
    {
        assert colony != null : "Colony cannot be null";

        // check if stunned - if so cannot act
        if (this.isStunned())
        {
            System.out.println(this + " ZombieBee is stunned and cannot act this turn.");
            this.reduceEffects();
            return;
        }

        // check if slowed - if so skip every other turn
        if (this.isSlowed())
        {
            System.out.println(this + " ZombieBee is slowed.");
            if (this.getSlowedTurns() % 2 == 0)
            {
                this.reduceEffects();
                return;
            }
        }

        // count how many ants we have attacked
        int attackCount = 0;

        // start from current place and check up to ATTACK_RANGE places ahead
        Place current = this.place;
        for (int i = 0; i < ATTACK_RANGE && current != null; i++)
        {
            Ant ant = current.getAnt();
            if (ant != null)
            {
                // attack the ant in this place
                sting(ant);
                attackCount++;
                System.out.println("ZombieBee attacked " + ant + " at " + current + 
                                   " (" + attackCount + "/" + ATTACK_RANGE + " attacks)");
            }
            // move to next place ahead (exit direction = towards queen)
            current = current.getExit();
        }

        // if no ants were found in range, move forward like a normal bee
        if (attackCount == 0 && this.armor > 0)
        {
            System.out.println("ZombieBee found no ants in range, moving forward.");
            this.moveTo(this.place.getExit());
        }

        this.reduceEffects();
    }

    /**
     * Returns a string representation of this ZombieBee
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "ZombieBee[" + this.getArmor() + ", " + this.getPlace() + "]";
    }
}