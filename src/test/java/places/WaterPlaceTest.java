package places;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ants.HarvesterAnt;
import ants.ScubaThrowerAnt;
import core.AntColony;
import core.Bee;

class WaterPlaceTest
{
    @Test
    void waterPlaceRejectsNonWatersafeAnt()
    {
        WaterPlace water = new WaterPlace("water");
        HarvesterAnt ant = new HarvesterAnt();

        water.addInsect(ant);

        assertNull(water.getAnt());
        assertNull(ant.getPlace());
    }

    @Test
    void waterPlaceAcceptsWatersafeAnt()
    {
        WaterPlace water = new WaterPlace("water");
        ScubaThrowerAnt ant = new ScubaThrowerAnt();

        water.addInsect(ant);

        assertSame(ant, water.getAnt());
        assertSame(water, ant.getPlace());
    }

    @Test
    void waterPlaceCanAddInsectOnlyAllowsWatersafeInsects()
    {
        WaterPlace water = new WaterPlace("water");
        HarvesterAnt harvester = new HarvesterAnt();
        ScubaThrowerAnt scuba = new ScubaThrowerAnt();
        Bee bee = new Bee(3);

        assertFalse(water.canAddInsect(harvester));
        assertTrue(water.canAddInsect(scuba));
        assertTrue(water.canAddInsect(bee));
    }

    @Test
    void deployAntDoesNotSpendFoodWhenWaterRejectsAnt()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        WaterPlace water = new WaterPlace("water");
        HarvesterAnt ant = new HarvesterAnt();

        colony.deployAnt(water, ant);

        assertNull(water.getAnt());
        assertNull(ant.getPlace());
        assertEquals(10, colony.getFood());
    }

    @Test
    void deployAntSpendsFoodWhenWaterAcceptsScubaThrower()
    {
        AntColony colony = new AntColony(1, 3, 0, 10);
        WaterPlace water = new WaterPlace("water");
        ScubaThrowerAnt ant = new ScubaThrowerAnt();

        colony.deployAnt(water, ant);

        assertSame(ant, water.getAnt());
        assertSame(water, ant.getPlace());
        assertEquals(5, colony.getFood());
    }
}
