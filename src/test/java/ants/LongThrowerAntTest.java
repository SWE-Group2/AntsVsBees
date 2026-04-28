package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import core.AntColony;
import core.Bee;
import core.Place;

class LongThrowerAntTest
{
    @Test
    void longThrowerAntHasFoodCostOfThree()
    {
        LongThrowerAnt ant = new LongThrowerAnt();

        assertEquals(3, ant.getFoodCost());
    }

    @Test
    void longThrowerAntHasArmorOfOne()
    {
        LongThrowerAnt ant = new LongThrowerAnt();

        assertEquals(1, ant.getArmor());
    }

    @Test
    void getTargetReturnsNearestBeeAtLongRange()
    {
        Place tunnel0 = new Place("tunnel0");
        Place tunnel1 = new Place("tunnel1", tunnel0);
        tunnel0.setEntrance(tunnel1);
        Place tunnel2 = new Place("tunnel2", tunnel1);
        tunnel1.setEntrance(tunnel2);
        Place tunnel3 = new Place("tunnel3", tunnel2);
        tunnel2.setEntrance(tunnel3);
        Place tunnel4 = new Place("tunnel4", tunnel3);
        tunnel3.setEntrance(tunnel4);
        Place tunnel5 = new Place("tunnel5", tunnel4);
        tunnel4.setEntrance(tunnel5);

        LongThrowerAnt ant = new LongThrowerAnt();
        tunnel0.addInsect(ant);

        Bee tooClose = new Bee(3);
        Bee inRange = new Bee(3);
        Bee fartherInRange = new Bee(3);
        tunnel3.addInsect(tooClose);
        tunnel4.addInsect(inRange);
        tunnel5.addInsect(fartherInRange);

        assertSame(inRange, ant.getTarget());
    }

    @Test
    void getTargetReturnsNullWhenBeeIsTooClose()
    {
        Place tunnel0 = new Place("tunnel0");
        Place tunnel1 = new Place("tunnel1", tunnel0);
        tunnel0.setEntrance(tunnel1);
        Place tunnel2 = new Place("tunnel2", tunnel1);
        tunnel1.setEntrance(tunnel2);
        Place tunnel3 = new Place("tunnel3", tunnel2);
        tunnel2.setEntrance(tunnel3);

        LongThrowerAnt ant = new LongThrowerAnt();
        tunnel0.addInsect(ant);

        Bee tooClose = new Bee(3);
        tunnel3.addInsect(tooClose);

        assertNull(ant.getTarget());
    }

    @Test
    void actionDamagesTargetAtLongRange()
    {
        AntColony colony = new AntColony(1, 5, 0, 10);
        Place antPlace = colony.getPlaces()[0];
        Place beePlace = colony.getPlaces()[4];
        LongThrowerAnt ant = new LongThrowerAnt();
        Bee bee = new Bee(3);
        antPlace.addInsect(ant);
        beePlace.addInsect(bee);

        ant.action(colony);

        assertEquals(2, bee.getArmor());
    }
}
