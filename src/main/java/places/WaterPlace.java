package places;

import core.Ant;
import core.Insect;
import core.Place;

/**
 * Only an insect that is watersafe can be deployed to a Water place. Bees can fly and so they are watersafe (set to
 * true)
 *
 * @author YOUR NAME HERE
 */
public class WaterPlace extends Place {

    /*
     * create a new WaterPlace with the given name and exit. The constructor should
     */
    public WaterPlace(String name, Place exit) {
        super(name, exit);
    }

    public WaterPlace(String name) {
        super(name);
    }

    /*
     * override the addInsect method to only allow watersafe insects to be added to this place. If a non-watersafe
     * insect is added, it should be removed from the game (i.e., not added to the place and not have a location).
     */
    @Override
    public void addInsect(Ant ant) {
        if (ant.isWatersafe()) {
            super.addInsect(ant);
        } else {
            ant.setPlace(null); // remove the insect from the game
            System.out.println(ant + " is not watersafe and cannot be added to " + this);
        }
    }

    /*
     * Determines if an insect can be added to this place.
     *
     * @param insect The insect to check.
     *
     * @return true if the insect can be added, false otherwise.
     */
    @Override
    public boolean canAddInsect(Insect insect) {
        if (!insect.isWatersafe()) {
            System.out.println(insect + " is not watersafe and cannot be added to " + this);
        }
        return insect.isWatersafe();
    }
}
