package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import core.AntColony;
import core.Bee;
import core.Place;

class SlowThrowerAntTest
{
    @Test
    void slowThrowerAntHasFoodCostOfFour()
    {
        SlowThrowerAnt ant = new SlowThrowerAnt();

        assertEquals(4, ant.getFoodCost());
    }

    @Test
    void slowThrowerAntHasArmorOfOne()
    {
        SlowThrowerAnt ant = new SlowThrowerAnt();

        assertEquals(1, ant.getArmor());
    }

    @Test
    void actionSlowsTargetWithoutDamagingIt()
    {
        AntColony colony = new AntColony(1, 4, 0, 10);
        Place antPlace = colony.getPlaces()[0];
        Place beePlace = colony.getPlaces()[2];
        SlowThrowerAnt ant = new SlowThrowerAnt();
        Bee bee = new Bee(3);
        antPlace.addInsect(ant);
        beePlace.addInsect(bee);

        ant.action(colony);

        assertTrue(bee.isSlowed());
        assertEquals(3, bee.getArmor());
    }

    @Test
    void slowedBeeOnlyActsEveryOtherTurnUntilEffectExpires()
    {
        Place queen = new Place("queen");
        Place tunnel0 = new Place("tunnel0", queen);
        queen.setEntrance(tunnel0);
        Place tunnel1 = new Place("tunnel1", tunnel0);
        tunnel0.setEntrance(tunnel1);
        Place tunnel2 = new Place("tunnel2", tunnel1);
        tunnel1.setEntrance(tunnel2);

        Bee bee = new Bee(3);
        tunnel2.addInsect(bee);
        bee.slow(3);

        bee.action(new AntColony(1, 1, 0, 0));
        assertSame(tunnel1, bee.getPlace());
        assertTrue(bee.isSlowed());

        bee.action(new AntColony(1, 1, 0, 0));
        assertSame(tunnel1, bee.getPlace());
        assertTrue(bee.isSlowed());

        bee.action(new AntColony(1, 1, 0, 0));
        assertSame(tunnel0, bee.getPlace());
        assertFalse(bee.isSlowed());
    }
}
