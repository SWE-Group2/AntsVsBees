package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class BeeTest {
  @Test
  void actionStingsBlockingAntInsteadOfMoving() {
    Place queen = new Place("queen");
    Place tunnel = new Place("tunnel", queen);
    queen.setEntrance(tunnel);

    Bee bee = new Bee(3);
    tunnel.addInsect(bee);

    TestAnt ant = new TestAnt(1, 0);
    tunnel.addInsect(ant);

    bee.action(new AntColony(1, 1, 0, 0));

    assertNull(tunnel.getAnt());
    assertSame(tunnel, bee.getPlace());
  }

  @Test
  void actionMovesToExitWhenNoAntBlocksThePath() {
    Place queen = new Place("queen");
    Place tunnel = new Place("tunnel", queen);
    queen.setEntrance(tunnel);

    Bee bee = new Bee(3);
    tunnel.addInsect(bee);

    bee.action(new AntColony(1, 1, 0, 0));

    assertSame(queen, bee.getPlace());
    assertEquals(1, queen.getBees().length);
    assertEquals(0, tunnel.getBees().length);
  }

  private static class TestAnt extends Ant {
    TestAnt(int armor, int foodCost) {
      super(armor);
      this.foodCost = foodCost;
    }

    @Override
    public void action(AntColony colony) {
      // No-op test double.
    }
  }
}
