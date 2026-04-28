package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import core.AntColony;
import core.Bee;
import core.Place;
import places.WaterPlace;

class ScubaThrowerAntTest
{
    @Test
    void scubaThrowerAntHasFoodCostOfFive()
    {
        ScubaThrowerAnt ant = new ScubaThrowerAnt();

        assertEquals(5, ant.getFoodCost());
    }

    @Test
    void scubaThrowerAntHasArmorOfOne()
    {
        ScubaThrowerAnt ant = new ScubaThrowerAnt();

        assertEquals(1, ant.getArmor());
    }

    @Test
    void scubaThrowerAntIsWatersafe()
    {
        ScubaThrowerAnt ant = new ScubaThrowerAnt();

        assertTrue(ant.isWatersafe());
    }

    @Test
    void scubaThrowerAntCanBeAddedToWaterPlace()
    {
        WaterPlace water = new WaterPlace("water");
        ScubaThrowerAnt ant = new ScubaThrowerAnt();

        water.addInsect(ant);

        assertSame(ant, water.getAnt());
        assertSame(water, ant.getPlace());
    }

    @Test
    void scubaThrowerAntDamagesTargetLikeAThrower()
    {
        AntColony colony = new AntColony(1, 4, 0, 10);
        Place antPlace = colony.getPlaces()[0];
        Place beePlace = colony.getPlaces()[2];
        ScubaThrowerAnt ant = new ScubaThrowerAnt();
        Bee bee = new Bee(3);
        antPlace.addInsect(ant);
        beePlace.addInsect(bee);

        ant.action(colony);

        assertEquals(2, bee.getArmor());
    }
}
