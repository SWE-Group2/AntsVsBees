package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import core.AntColony;
import core.Bee;
import core.Place;

/**
 * Tests for HungryAnt class.
 * Checks food cost, armor, digestion, and bee eating behaviour.
 */
class HungryAntTest
{
    /**
     * Test that HungryAnt costs 4 food to deploy
     */
    @Test
    void hungryAntHasFoodCostOfFour()
    {
        HungryAnt ant = new HungryAnt();
        assertEquals(4, ant.getFoodCost());
    }

    /**
     * Test that HungryAnt starts with 1 armor
     */
    @Test
    void hungryAntHasArmorOfOne()
    {
        HungryAnt ant = new HungryAnt();
        assertEquals(1, ant.getArmor());
    }

    /**
     * Test that HungryAnt starts with 0 digestion turns
     * meaning it is ready to eat straight away
     */
    @Test
    void hungryAntStartsWithZeroDigestionTurns()
    {
        HungryAnt ant = new HungryAnt();
        assertEquals(0, ant.getTurnsDigesting());
    }

    /**
     * Test that after eating a bee, HungryAnt starts digesting for 3 turns
     */
    @Test
    void hungryAntDigestsForThreeTurnsAfterEating()
    {
        // set up a colony and place
        AntColony colony = new AntColony(1, 3, 0, 10);
        HungryAnt ant = new HungryAnt();
        Place place = colony.getPlaces()[0];

        // add a bee to the same place as the ant
        Bee bee = new Bee(3);
        place.addInsect(ant);
        place.addInsect(bee);

        // ant eats the bee
        ant.action(colony);

        // should now be digesting for 3 turns
        assertEquals(3, ant.getTurnsDigesting());
    }

    /**
     * Test that digestion timer counts down each turn
     */
    @Test
    void hungryAntDigestionCountsDownEachTurn()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        HungryAnt ant = new HungryAnt();
        Place place = colony.getPlaces()[0];

        // add a bee so ant can eat
        Bee bee = new Bee(3);
        place.addInsect(ant);
        place.addInsect(bee);

        // ant eats bee - starts digesting
        ant.action(colony);
        assertEquals(3, ant.getTurnsDigesting());

        // next turn - digestion counts down
        ant.action(colony);
        assertEquals(2, ant.getTurnsDigesting());

        // next turn - digestion counts down again
        ant.action(colony);
        assertEquals(1, ant.getTurnsDigesting());
    }

    /**
     * Test that after digestion is done, ant can eat again
     */
    @Test
    void hungryAntCanEatAgainAfterDigestion()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        HungryAnt ant = new HungryAnt();
        Place place = colony.getPlaces()[0];

        // add first bee
        Bee bee1 = new Bee(3);
        place.addInsect(ant);
        place.addInsect(bee1);

        // eat first bee
        ant.action(colony);
        assertEquals(3, ant.getTurnsDigesting());

        // wait 3 turns for digestion
        ant.action(colony); // 2 left
        ant.action(colony); // 1 left
        ant.action(colony); // 0 left - ready to eat!

        // add second bee
        Bee bee2 = new Bee(3);
        place.addInsect(bee2);

        // eat second bee
        ant.action(colony);
        assertEquals(3, ant.getTurnsDigesting());
    }
}