package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import core.AntColony;
import org.junit.jupiter.api.Test;

class WallAntTest {
  @Test
  void wallAntHasFoodCostOfFour() {
    WallAnt ant = new WallAnt();
    assertEquals(4, ant.getFoodCost());
  }

  @Test
  void wallAntHasArmorOfFour() {
    WallAnt ant = new WallAnt();
    assertEquals(4, ant.getArmor());
  }

  @Test
  void wallAntActionDoesNothing() {
    AntColony colony = new AntColony(1, 3, 0, 10);
    WallAnt ant = new WallAnt();
    int foodBefore = colony.getFood();

    ant.action(colony);

    // food should not change since WallAnt does nothing
    assertEquals(foodBefore, colony.getFood());
  }

  @Test
  void wallAntCanBeDeployed() {
    AntColony colony = new AntColony(1, 3, 0, 10);
    WallAnt ant = new WallAnt();

    colony.deployAnt(colony.getPlaces()[0], ant);

    assertNotNull(colony.getPlaces()[0].getAnt());
  }
}
