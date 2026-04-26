package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import core.AntColony;
import core.Bee;
import core.Place;

/**
 * Tests for FireAnt class.
 * Checks food cost, armor, damage value, and explosion behaviour on death.
 */
class FireAntTest
{
    /**
     * Test that FireAnt costs 4 food to deploy
     */
    @Test
    void fireAntHasFoodCostOfFour()
    {
        FireAnt ant = new FireAnt();
        assertEquals(4, ant.getFoodCost());
    }

    /**
     * Test that FireAnt starts with 1 armor
     */
    @Test
    void fireAntHasArmorOfOne()
    {
        FireAnt ant = new FireAnt();
        assertEquals(1, ant.getArmor());
    }

    /**
     * Test that FireAnt has damage value of 3
     */
    @Test
    void fireAntHasDamageOfThree()
    {
        FireAnt ant = new FireAnt();
        assertEquals(3, ant.getDamage());
    }

    /**
     * Test that when FireAnt dies, bees in the same place take damage
     */
    @Test
    void fireAntDamagesBeesWhenItDies()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        FireAnt ant = new FireAnt();
        Place place = colony.getPlaces()[0];

        // add a bee with 5 armor to the same place as the fire ant
        Bee bee = new Bee(5);
        place.addInsect(ant);
        place.addInsect(bee);

        // kill the fire ant - this should trigger the explosion
        ant.reduceArmor(1);

        // bee should have lost 3 armor (5 - 3 = 2)
        assertEquals(2, bee.getArmor());
    }

    /**
     * Test that FireAnt damages ALL bees in its place when it dies
     */
    @Test
    void fireAntDamagesAllBeesWhenItDies()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        FireAnt ant = new FireAnt();
        Place place = colony.getPlaces()[0];

        // add TWO bees to the same place
        Bee bee1 = new Bee(5);
        Bee bee2 = new Bee(5);
        place.addInsect(ant);
        place.addInsect(bee1);
        place.addInsect(bee2);

        // kill the fire ant
        ant.reduceArmor(1);

        // BOTH bees should have lost 3 armor (5 - 3 = 2)
        assertEquals(2, bee1.getArmor());
        assertEquals(2, bee2.getArmor());
    }

    /**
     * Test that FireAnt action does nothing
     */
    @Test
    void fireAntActionDoesNothing()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        FireAnt ant = new FireAnt();
        int foodBefore = colony.getFood();

        ant.action(colony);

        // food should not change
        assertEquals(foodBefore, colony.getFood());
    }
}