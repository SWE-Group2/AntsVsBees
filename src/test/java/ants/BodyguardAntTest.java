package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import core.AntColony;
import core.Bee;
import core.Place;

/**
 * Tests for BodyguardAnt class.
 * Checks food cost, armor, protecting behaviour, and death behaviour.
 */
class BodyguardAntTest
{
    /**
     * Test that BodyguardAnt costs 4 food
     */
    @Test
    void bodyguardAntHasFoodCostOfFour()
    {
        BodyguardAnt ant = new BodyguardAnt();
        assertEquals(4, ant.getFoodCost());
    }

    /**
     * Test that BodyguardAnt starts with 2 armor
     */
    @Test
    void bodyguardAntHasArmorOfTwo()
    {
        BodyguardAnt ant = new BodyguardAnt();
        assertEquals(2, ant.getArmor());
    }

    /**
     * Test that BodyguardAnt starts with no contained ant
     */
    @Test
    void bodyguardAntStartsWithNoContainedAnt()
    {
        BodyguardAnt ant = new BodyguardAnt();
        assertNull(ant.getContainedAnt());
    }

    /**
     * Test that BodyguardAnt can protect another ant in the same place
     */
    @Test
    void bodyguardAntCanProtectAnotherAnt()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        Place place = colony.getPlaces()[0];

        // place a ThrowerAnt first
        ThrowerAnt thrower = new ThrowerAnt();
        place.addInsect(thrower);

        // now place a BodyguardAnt in the same spot
        BodyguardAnt bodyguard = new BodyguardAnt();
        place.addInsect(bodyguard);

        // bodyguard should be the main ant and protecting the thrower
        assertEquals(bodyguard, place.getAnt());
        assertEquals(thrower, bodyguard.getContainedAnt());
    }

    /**
     * Test that when BodyguardAnt dies, the protected ant takes its place
     */
    @Test
    void protectedAntTakesPlaceWhenBodyguardDies()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        Place place = colony.getPlaces()[0];

        // place a ThrowerAnt first
        ThrowerAnt thrower = new ThrowerAnt();
        place.addInsect(thrower);

        // place a BodyguardAnt to protect it
        BodyguardAnt bodyguard = new BodyguardAnt();
        place.addInsect(bodyguard);

        // kill the bodyguard
        bodyguard.reduceArmor(2);

        // thrower should now be exposed in the place
        assertEquals(thrower, place.getAnt());
    }

    /**
     * Test that bee stings BodyguardAnt first before hurting protected ant
     */
    @Test
    void beeStingsBodyguardFirst()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        Place place = colony.getPlaces()[0];

        // place a ThrowerAnt first
        ThrowerAnt thrower = new ThrowerAnt();
        place.addInsect(thrower);

        // place a BodyguardAnt to protect it
        BodyguardAnt bodyguard = new BodyguardAnt();
        place.addInsect(bodyguard);

        // bee stings the place
        Bee bee = new Bee(3);
        place.addInsect(bee);
        bee.sting(place.getAnt());

        // bodyguard should have lost 1 armor (2-1=1)
        assertEquals(1, bodyguard.getArmor());

        // thrower should be untouched (still 1 armor)
        assertEquals(1, thrower.getArmor());
    }
}