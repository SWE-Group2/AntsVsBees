package ants;

import core.AntColony;
import core.Bee;

public class StunThrowerAnt extends ThrowerAnt {
  /** Creates a new StunThrowerAnt. Post-condition: food cost is 6, armor is 1, and damage is 0 */
  public StunThrowerAnt() {
    super();
    this.foodCost = 6;
    this.armor = 1;
    this.damage = 0;
  }

  /**
   * Throws a leaf at the nearest bee in range. Pre-condition: colony must not be null
   * Post-condition: target bee is stunned for 1 turn
   */
  public void action(AntColony colony) {
    assert colony != null : "Colony cannot be null";
    Bee target = getTarget();
    if (target != null) {
      target.stun(1); // Example: stun the bee for 1 turn
      System.out.println("StunThrowerAnt threw a leaf at " + target);
    }
  }
}
