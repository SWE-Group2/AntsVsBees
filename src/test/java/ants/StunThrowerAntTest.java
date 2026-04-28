package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import core.AntColony;
import core.Bee;
import core.Place;

class StunThrowerAntTest
{
    @Test
    void stunThrowerAntHasFoodCostOfSix()
    {
        StunThrowerAnt ant = new StunThrowerAnt();

        assertEquals(6, ant.getFoodCost());
    }

    @Test
    void stunThrowerAntHasArmorOfOne()
    {
        StunThrowerAnt ant = new StunThrowerAnt();

        assertEquals(1, ant.getArmor());
    }

    @Test
    void actionStunsTargetWithoutDamagingIt()
    {
        AntColony colony = new AntColony(1, 4, 0, 10);
        Place antPlace = colony.getPlaces()[0];
        Place beePlace = colony.getPlaces()[2];
        StunThrowerAnt ant = new StunThrowerAnt();
        Bee bee = new Bee(3);
        antPlace.addInsect(ant);
        beePlace.addInsect(bee);

        ant.action(colony);

        assertTrue(bee.isStunned());
        assertEquals(3, bee.getArmor());
    }

    @Test
    void stunnedBeeSkipsOneTurnThenEffectExpires()
    {
        Place queen = new Place("queen");
        Place tunnel0 = new Place("tunnel0", queen);
        queen.setEntrance(tunnel0);
        Place tunnel1 = new Place("tunnel1", tunnel0);
        tunnel0.setEntrance(tunnel1);

        Bee bee = new Bee(3);
        tunnel1.addInsect(bee);
        bee.stun(1);

        bee.action(new AntColony(1, 1, 0, 0));
        assertSame(tunnel1, bee.getPlace());
        assertFalse(bee.isStunned());

        bee.action(new AntColony(1, 1, 0, 0));
        assertSame(tunnel0, bee.getPlace());
    }
}
