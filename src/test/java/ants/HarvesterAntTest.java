package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.AntColony;
import org.junit.jupiter.api.Test;

class HarvesterAntTest {
  @Test
  void actionAddsOneFoodToTheColony() {
    AntColony colony = new AntColony(1, 3, 0, 2);
    HarvesterAnt ant = new HarvesterAnt();

    ant.action(colony);

    assertEquals(3, colony.getFood());
  }
}
