package bees;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.AntColony;
import core.Bee;
import core.Place;
import org.junit.jupiter.api.Test;

class GhostBeeTest {
    @Test
    void ghostBeeKeepsBeeDefaults() {
        GhostBee bee = new GhostBee(3);

        assertTrue(bee instanceof Bee);
        assertEquals(3, bee.getArmor());
        assertTrue(bee.isWatersafe());
    }

    @Test
    void ghostBeeMovesLikeARegularBeeWhenNotBlocked() {
        Place queen = new Place("queen");
        Place tunnel = new Place("tunnel", queen);
        queen.setEntrance(tunnel);

        GhostBee bee = new GhostBee(3);
        tunnel.addInsect(bee);

        bee.action(new AntColony(1, 1, 0, 0));

        assertSame(queen, bee.getPlace());
        assertEquals(1, queen.getBees().length);
        assertEquals(0, tunnel.getBees().length);
    }
}
