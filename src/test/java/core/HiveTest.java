package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class HiveTest {
  @Test
  void invadeMovesScheduledWaveIntoTheOnlyEntrance() {
    AntColony colony = new AntColony(1, 3, 0, 2);
    Hive hive = new Hive(3);
    hive.addWave(2, 2);

    Bee[] invaders = hive.invade(colony, 2);

    assertEquals(2, invaders.length);
    assertSame(colony.getBeeEntrances()[0], invaders[0].getPlace());
    assertSame(colony.getBeeEntrances()[0], invaders[1].getPlace());
  }

  @Test
  void invadeReturnsEmptyArrayWhenNoWaveIsScheduled() {
    AntColony colony = new AntColony(1, 3, 0, 2);
    Hive hive = new Hive(3);

    Bee[] invaders = hive.invade(colony, 99);

    assertEquals(0, invaders.length);
  }
}
