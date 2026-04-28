package ants;

import core.AntColony;
import core.Bee;

public class SlowThrowerAnt extends ThrowerAnt {
  /** Creates a new SlowThrowerAnt. Post-condition: food cost is 4, armor is 1, and damage is 0 */
  public SlowThrowerAnt() {
    super();
    this.foodCost = 4;
    this.armor = 1;
    this.damage = 0;
  }

  /**
   * Throws a leaf at the nearest bee in range. Pre-condition: colony must not be null
   * Post-condition: target bee is slowed for 3 turns
   */
  public void action(AntColony colony) {
    assert colony != null : "Colony cannot be null";
    Bee target = getTarget();
    if (target != null) {
      target.slow(3); // Example: slow the bee for 3 turns
      System.out.println("ThrowerAnt threw a leaf at " + target);
    }
  }
}
