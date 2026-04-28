package core;

/**
 * A class representing a basic Ant
 *
 * @author YOUR NAME HERE
 */
public abstract class Ant extends Insect {
  protected int foodCost; // the amount of food needed to make this ant

  protected boolean blocker = true; // whether this ant blocks bees from advancing past its location

  /**
   * Creates a new Ant, with a food cost of 0.
   *
   * @param armor The armor of the ant.
   */
  public Ant(int armor) {
    super(armor, null);
    this.foodCost = 0;
    this.watersafe = false;
  }

  /**
   * Returns the ant's food cost
   *
   * @return the ant's food cost
   */
  public int getFoodCost() {
    return foodCost;
  }

  /**
   * Returns whether this ant blocks bees from advancing past its location
   *
   * @return whether this ant blocks bees from advancing past its location
   */
  public boolean blocksBees() {
    return blocker;
  }

  /** Removes the ant from its current place */
  public void leavePlace() {
    this.place.removeInsect(this);
  }
}
