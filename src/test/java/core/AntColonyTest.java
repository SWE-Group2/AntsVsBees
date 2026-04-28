package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ants.HarvesterAnt;
import ants.ThrowerAnt;
import org.junit.jupiter.api.Test;

class AntColonyTest {
  @Test
  void constructorCapsTunnelLengthAtMaximum() {
    AntColony colony = new AntColony(2, 99, 0, 5);

    assertEquals(16, colony.getPlaces().length);
    assertEquals(2, colony.getBeeEntrances().length);
  }

  @Test
  void deployAntSpendsFoodAndPlacesAntWhenAffordable() {
    AntColony colony = new AntColony(1, 3, 0, 5);
    Place place = colony.getPlaces()[0];
    HarvesterAnt ant = new HarvesterAnt();

    colony.deployAnt(place, ant);

    assertSame(ant, place.getAnt());
    assertEquals(3, colony.getFood());
  }

  @Test
  void deployAntDoesNothingWhenFoodIsInsufficient() {
    AntColony colony = new AntColony(1, 3, 0, 1);
    Place place = colony.getPlaces()[0];
    ThrowerAnt ant = new ThrowerAnt();

    colony.deployAnt(place, ant);

    assertNull(place.getAnt());
    assertEquals(1, colony.getFood());
  }

  @Test
  void removeAntClearsTheOccupiedPlace() {
    AntColony colony = new AntColony(1, 3, 0, 5);
    Place place = colony.getPlaces()[0];
    HarvesterAnt ant = new HarvesterAnt();
    colony.deployAnt(place, ant);

    colony.removeAnt(place);

    assertNull(place.getAnt());
    assertFalse(colony.getAllAnts().contains(ant));
  }

  @Test
  void queenHasBeesTracksWhetherTheQueenIsOccupied() {
    AntColony colony = new AntColony(1, 3, 0, 5);

    assertFalse(colony.queenHasBees());

    colony.getQueenPlace().addInsect(new Bee(2));

    assertTrue(colony.queenHasBees());
  }
}
