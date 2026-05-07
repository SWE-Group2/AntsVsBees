package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import core.AntColony;
import core.Bee;
import core.Place;
import org.junit.jupiter.api.Test;

class ShortThrowerAntTest {
    @Test
    void shortThrowerAntHasFoodCostOfThree() {
        ShortThrowerAnt ant = new ShortThrowerAnt();

        assertEquals(3, ant.getFoodCost());
    }

    @Test
    void shortThrowerAntHasArmorOfOne() {
        ShortThrowerAnt ant = new ShortThrowerAnt();

        assertEquals(1, ant.getArmor());
    }

    @Test
    void getTargetReturnsNearestBeeWithinShortRange() {
        Place tunnel0 = new Place("tunnel0");
        Place tunnel1 = new Place("tunnel1", tunnel0);
        tunnel0.setEntrance(tunnel1);
        Place tunnel2 = new Place("tunnel2", tunnel1);
        tunnel1.setEntrance(tunnel2);
        Place tunnel3 = new Place("tunnel3", tunnel2);
        tunnel2.setEntrance(tunnel3);

        ShortThrowerAnt ant = new ShortThrowerAnt();
        tunnel0.addInsect(ant);

        Bee inRange = new Bee(3);
        Bee outOfRange = new Bee(3);
        tunnel2.addInsect(inRange);
        tunnel3.addInsect(outOfRange);

        assertSame(inRange, ant.getTarget());
    }

    @Test
    void getTargetReturnsNullWhenBeeIsBeyondShortRange() {
        Place tunnel0 = new Place("tunnel0");
        Place tunnel1 = new Place("tunnel1", tunnel0);
        tunnel0.setEntrance(tunnel1);
        Place tunnel2 = new Place("tunnel2", tunnel1);
        tunnel1.setEntrance(tunnel2);
        Place tunnel3 = new Place("tunnel3", tunnel2);
        tunnel2.setEntrance(tunnel3);

        ShortThrowerAnt ant = new ShortThrowerAnt();
        tunnel0.addInsect(ant);

        Bee outOfRange = new Bee(3);
        tunnel3.addInsect(outOfRange);

        assertNull(ant.getTarget());
    }

    @Test
    void actionDamagesTargetInShortRange() {
        AntColony colony = new AntColony(1, 4, 0, 10);
        Place antPlace = colony.getPlaces()[0];
        Place beePlace = colony.getPlaces()[2];
        ShortThrowerAnt ant = new ShortThrowerAnt();
        Bee bee = new Bee(3);
        antPlace.addInsect(ant);
        beePlace.addInsect(bee);

        ant.action(colony);

        assertEquals(2, bee.getArmor());
    }
}
