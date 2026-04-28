package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import core.AntColony;
import core.Bee;
import core.Place;
import org.junit.jupiter.api.Test;

class NinjaAntTest {
  @Test
  void ninjaAntHasFoodCostOfSix() {
    NinjaAnt ant = new NinjaAnt();

    assertEquals(6, ant.getFoodCost());
  }

  @Test
  void ninjaAntHasArmorOfOne() {
    NinjaAnt ant = new NinjaAnt();

    assertEquals(1, ant.getArmor());
  }

  @Test
  void ninjaAntDoesNotBlockBees() {
    NinjaAnt ant = new NinjaAnt();

    assertFalse(ant.blocksBees());
  }

  @Test
  void getTargetReturnsBeeInSamePlace() {
    Place place = new Place("tunnel0");
    NinjaAnt ant = new NinjaAnt();
    Bee bee = new Bee(3);

    place.addInsect(ant);
    place.addInsect(bee);

    assertSame(bee, ant.getTarget());
  }

  @Test
  void getTargetReturnsNullWhenBeeIsNotInSamePlace() {
    Place tunnel0 = new Place("tunnel0");
    Place tunnel1 = new Place("tunnel1", tunnel0);
    tunnel0.setEntrance(tunnel1);
    NinjaAnt ant = new NinjaAnt();
    Bee bee = new Bee(3);

    tunnel0.addInsect(ant);
    tunnel1.addInsect(bee);

    assertNull(ant.getTarget());
  }

  @Test
  void actionDamagesBeeInSamePlace() {
    AntColony colony = new AntColony(1, 3, 0, 10);
    Place place = colony.getPlaces()[0];
    NinjaAnt ant = new NinjaAnt();
    Bee bee = new Bee(3);

    place.addInsect(ant);
    place.addInsect(bee);

    ant.action(colony);

    assertEquals(2, bee.getArmor());
  }

  @Test
  void beeMovesPastNinjaAntInsteadOfStingingIt() {
    AntColony colony = new AntColony(1, 3, 0, 10);
    Place place = colony.getPlaces()[0];
    NinjaAnt ant = new NinjaAnt();
    Bee bee = new Bee(3);

    place.addInsect(ant);
    place.addInsect(bee);

    bee.action(colony);

    assertSame(colony.getQueenPlace(), bee.getPlace());
    assertEquals(1, ant.getArmor());
  }
}
