package ants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.AntColony;
import core.Bee;
import core.Place;
import org.junit.jupiter.api.Test;
import places.WaterPlace;

class QueenAntTest {
  @Test
  void queenAntHasFoodCostOfSix() {
    QueenAnt queen = new QueenAnt();

    assertEquals(6, queen.getFoodCost());
  }

  @Test
  void queenAntHasArmorOfOne() {
    QueenAnt queen = new QueenAnt();

    assertEquals(1, queen.getArmor());
  }

  @Test
  void queenAntIsWatersafe() {
    QueenAnt queen = new QueenAnt();

    assertTrue(queen.isWatersafe());
  }

  @Test
  void defaultQueenStartsAsNotRealQueen() {
    QueenAnt queen = new QueenAnt();

    assertFalse(queen.isRealQueen());
  }

  @Test
  void queenCanBeCreatedAsRealQueen() {
    QueenAnt queen = new QueenAnt(true);

    assertTrue(queen.isRealQueen());
  }

  @Test
  void setRealQueenUpdatesQueenIdentity() {
    QueenAnt queen = new QueenAnt();

    queen.setRealQueen(true);

    assertTrue(queen.isRealQueen());
  }

  @Test
  void queenAntCanBeAddedToWaterPlace() {
    WaterPlace water = new WaterPlace("water");
    QueenAnt queen = new QueenAnt();

    water.addInsect(queen);

    assertSame(queen, water.getAnt());
    assertSame(water, queen.getPlace());
  }

  @Test
  void queenAntDamagesTargetLikeAThrower() {
    AntColony colony = new AntColony(1, 4, 0, 10);
    Place queenPlace = colony.getPlaces()[0];
    Place beePlace = colony.getPlaces()[2];
    QueenAnt queen = new QueenAnt(true);
    Bee bee = new Bee(3);
    queenPlace.addInsect(queen);
    beePlace.addInsect(bee);

    queen.action(colony);

    assertEquals(2, bee.getArmor());
  }

  @Test
  void deployingFirstQueenMakesItTheRealQueen() {
    AntColony colony = new AntColony(1, 3, 0, 10);
    Place place = colony.getPlaces()[0];
    QueenAnt queen = new QueenAnt();

    colony.deployAnt(place, queen);

    assertTrue(queen.isRealQueen());
    assertSame(place, colony.getRealQueenColonyPlace());
  }

  @Test
  void deployingSecondQueenLeavesItAsAnImpostor() {
    AntColony colony = new AntColony(1, 3, 0, 20);
    QueenAnt firstQueen = new QueenAnt();
    QueenAnt secondQueen = new QueenAnt();

    colony.deployAnt(colony.getPlaces()[0], firstQueen);
    colony.deployAnt(colony.getPlaces()[1], secondQueen);

    assertTrue(firstQueen.isRealQueen());
    assertFalse(secondQueen.isRealQueen());
    assertSame(colony.getPlaces()[0], colony.getRealQueenColonyPlace());
  }

  @Test
  void realQueenCannotBeRemovedFromColony() {
    AntColony colony = new AntColony(1, 3, 0, 10);
    Place place = colony.getPlaces()[0];
    QueenAnt queen = new QueenAnt();
    colony.deployAnt(place, queen);

    colony.removeAnt(place);

    assertSame(queen, place.getAnt());
    assertSame(place, queen.getPlace());
  }

  @Test
  void impostorQueenCanBeRemovedFromColony() {
    AntColony colony = new AntColony(1, 3, 0, 20);
    QueenAnt firstQueen = new QueenAnt();
    QueenAnt secondQueen = new QueenAnt();
    colony.deployAnt(colony.getPlaces()[0], firstQueen);
    colony.deployAnt(colony.getPlaces()[1], secondQueen);

    colony.removeAnt(colony.getPlaces()[1]);

    assertNull(colony.getPlaces()[1].getAnt());
    assertNull(secondQueen.getPlace());
  }

  @Test
  void queenHasBeesTracksBeesAtTheRealQueenPlace() {
    AntColony colony = new AntColony(1, 3, 0, 10);
    Place place = colony.getPlaces()[0];
    QueenAnt queen = new QueenAnt();
    colony.deployAnt(place, queen);

    assertFalse(colony.queenHasBees());

    place.addInsect(new Bee(2));

    assertTrue(colony.queenHasBees());
  }
}
