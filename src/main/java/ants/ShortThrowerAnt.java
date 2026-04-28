package ants;

import core.Bee;

public class ShortThrowerAnt extends ThrowerAnt {
  /** Creates a new ShortThrowerAnt. Post-condition: food cost is 3, armor is 1, and damage is 1 */
  public ShortThrowerAnt() {
    super();
    this.foodCost = 3;
    this.armor = 1;
    this.damage = 1;
  }

  /**
   * Returns the nearest bee within range to throw at. Post-condition: returns a Bee or null if no
   * bee in range
   */
  @Override
  public Bee getTarget() {
    return place.getClosestBee(0, 2);
  }
}
