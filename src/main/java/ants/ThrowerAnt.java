package ants;

import core.Ant;
import core.AntColony;
import core.Bee;

/** An Ant that throws leaves at bees. Food Cost: 4, Armor: 1, Damage: 1 */
public class ThrowerAnt extends Ant {
  protected int damage;

  /** Creates a new ThrowerAnt. Post-condition: food cost is 4, armor is 1, damage is 1 */
  public ThrowerAnt() {
    super(1); // armor = 1
    this.foodCost = 4;
    this.damage = 1;
  }

  /**
   * Returns the nearest bee within range to throw at. Post-condition: returns a Bee or null if no
   * bee in range
   */
  public Bee getTarget() {
    return place.getClosestBee(0, 3);
  }

  /**
   * Throws a leaf at the nearest bee in range. Pre-condition: colony must not be null
   * Post-condition: target bee loses armor equal to damage
   */
  public void action(AntColony colony) {
    assert colony != null : "Colony cannot be null";
    Bee target = getTarget();
    if (target != null) {
      target.reduceArmor(this.damage);
      System.out.println("ThrowerAnt threw a leaf at " + target);
    }
  }
}
