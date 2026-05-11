package bees;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ants.WallAnt;
import core.AntColony;
import core.Place;
import org.junit.jupiter.api.Test;

/**
 * Tests for ZombieBee class. Checks that ZombieBee can attack multiple ants at once.
 */
class ZombieBeeTest {

    /** Test that ZombieBee is watersafe (can fly over water) */
    @Test
    void zombieBeeIsWatersafe() {
        ZombieBee zombie = new ZombieBee(3);
        assertTrue(zombie.isWatersafe());
    }

    /** Test that ZombieBee attacks the ant in its current place */
    @Test
    void zombieBeeAttacksAntInCurrentPlace() {
        AntColony colony = new AntColony(1, 5, 0, 10);
        Place[] places = colony.getPlaces();

        // place a WallAnt (armor 4) in first place
        WallAnt wall = new WallAnt();
        places[0].addInsect(wall);

        // place zombie bee in same place
        ZombieBee zombie = new ZombieBee(3);
        places[0].addInsect(zombie);

        // zombie bee acts
        zombie.action(colony);

        // wall ant should have taken 1 damage (4-1=3)
        assertEquals(3, wall.getArmor());
    }

    /** Test that ZombieBee attacks ants in multiple places at once */
    @Test
    void zombieBeeAttacksMultipleAnts() {
        AntColony colony = new AntColony(1, 5, 0, 10);
        Place[] places = colony.getPlaces();

        // places are ordered from queen outward
        // getExit() moves toward the queen: places[2] -> places[1] -> places[0]
        // so place zombie at places[2] and ants in places[2], [1], [0]
        WallAnt wall1 = new WallAnt();
        WallAnt wall2 = new WallAnt();
        WallAnt wall3 = new WallAnt();
        places[2].addInsect(wall1);
        places[1].addInsect(wall2);
        places[0].addInsect(wall3);

        // place zombie bee in places[2] - attacks places[2], [1], [0] via getExit()
        ZombieBee zombie = new ZombieBee(3);
        places[2].addInsect(zombie);

        // zombie bee acts - should attack all 3 ants
        zombie.action(colony);

        // all 3 wall ants should have taken 1 damage (4-1=3)
        assertEquals(3, wall1.getArmor());
        assertEquals(3, wall2.getArmor());
        assertEquals(3, wall3.getArmor());
    }

    /** Test that ZombieBee moves forward when no ants are in range */
    @Test
    void zombieBeeMovesForwardWhenNoAntsInRange() {
        AntColony colony = new AntColony(1, 5, 0, 10);
        Place[] places = colony.getPlaces();

        // place zombie bee in last place with no ants nearby
        ZombieBee zombie = new ZombieBee(3);
        places[4].addInsect(zombie);

        Place exitPlace = places[4].getExit();

        // zombie bee acts - should move forward
        zombie.action(colony);

        // zombie should have moved to exit place
        assertEquals(exitPlace, zombie.getPlace());
    }

    /** Test that ZombieBee cannot act when stunned */
    @Test
    void zombieBeeCannotActWhenStunned() {
        AntColony colony = new AntColony(1, 5, 0, 10);
        Place[] places = colony.getPlaces();

        WallAnt wall = new WallAnt();
        places[0].addInsect(wall);

        ZombieBee zombie = new ZombieBee(3);
        places[0].addInsect(zombie);

        // stun the zombie bee for 1 turn
        zombie.stun(1);

        // zombie bee acts but is stunned
        zombie.action(colony);

        // wall ant should be untouched (zombie was stunned)
        assertEquals(4, wall.getArmor());
    }
}