package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import core.AntColony;
import core.Bee;
import core.Place;
import org.junit.jupiter.api.Test;

class ThrowerAntTest {
  @Test
  void getTargetReturnsNearestBeeWithinConfiguredRange() {
    Place tunnel0 = new Place("tunnel0");
    Place tunnel1 = new Place("tunnel1", tunnel0);
    tunnel0.setEntrance(tunnel1);
    Place tunnel2 = new Place("tunnel2", tunnel1);
    tunnel1.setEntrance(tunnel2);
    Place tunnel3 = new Place("tunnel3", tunnel2);
    tunnel2.setEntrance(tunnel3);
    Place tunnel4 = new Place("tunnel4", tunnel3);
    tunnel3.setEntrance(tunnel4);

    ThrowerAnt ant = new ThrowerAnt();
    tunnel0.addInsect(ant);

    Bee inRange = new Bee(3);
    Bee outOfRange = new Bee(3);
    tunnel2.addInsect(inRange);
    tunnel4.addInsect(outOfRange);

    assertSame(inRange, ant.getTarget());
  }

  @Test
  void getTargetReturnsNullWhenNoBeeIsInRange() {
    Place tunnel0 = new Place("tunnel0");
    Place tunnel1 = new Place("tunnel1", tunnel0);
    tunnel0.setEntrance(tunnel1);
    Place tunnel2 = new Place("tunnel2", tunnel1);
    tunnel1.setEntrance(tunnel2);
    Place tunnel3 = new Place("tunnel3", tunnel2);
    tunnel2.setEntrance(tunnel3);
    Place tunnel4 = new Place("tunnel4", tunnel3);
    tunnel3.setEntrance(tunnel4);

    ThrowerAnt ant = new ThrowerAnt();
    tunnel0.addInsect(ant);

    Bee outOfRange = new Bee(3);
    tunnel4.addInsect(outOfRange);

    assertNull(ant.getTarget());
  }

  @Test
  void actionDamagesTheTargetBee() {
    AntColony colony = new AntColony(1, 4, 0, 10);
    Place antPlace = colony.getPlaces()[0];
    Place beePlace = colony.getPlaces()[2];

    ThrowerAnt ant = new ThrowerAnt();
    Bee bee = new Bee(3);
    antPlace.addInsect(ant);
    beePlace.addInsect(bee);

    ant.action(colony);

    assertEquals(2, bee.getArmor());
  }
}
