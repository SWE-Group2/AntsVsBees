package ants;

import core.Ant;
import core.AntColony;
import core.Bee;
import core.Place;

/**
 * A FireAnt does nothing on its turn. BUT when it dies, it damages ALL bees in the same place by
 * its damage value. Think of it like a bomb - it explodes when it dies!
 *
 * <p>Food Cost: 4, Armor: 1, Damage: 3
 *
 * @author YOUR NAME HERE
 */
public class FireAnt extends Ant {
  // how much damage the FireAnt deals to bees when it dies
  private int damage;

  /** Creates a new FireAnt. Post-condition: food cost is 4, armor is 1, damage is 3 */
  public FireAnt() {
    super(1); // armor = 1
    this.foodCost = 4;
    this.damage = 3; // deals 3 damage to all bees when it dies
  }

  /**
   * Returns the damage this FireAnt deals when it dies.
   *
   * @return the damage value
   */
  public int getDamage() {
    return damage;
  }

  /**
   * FireAnt does nothing on its turn. Its power only activates when it dies. Pre-condition: colony
   * must not be null Post-condition: no change to colony or game state
   */
  @Override
  public void action(AntColony colony) {
    assert colony != null : "Colony cannot be null";
    // FireAnt does nothing on its turn - it waits to explode!
  }

  /**
   * Overrides reduceArmor to trigger the fire explosion when armor hits 0. When FireAnt dies, it
   * damages ALL bees in its place by its damage value.
   *
   * <p>Pre-condition: amount must be greater than 0 Post-condition: if armor reaches 0, all bees in
   * same place take damage
   *
   * @param amount The amount of damage to take
   */
  @Override
  public void reduceArmor(int amount) {
    assert amount > 0 : "Damage amount must be greater than 0";

    // reduce armor normally first
    this.armor -= amount;

    if (this.armor <= 0) {
      // FireAnt is dying - get the place before leaving
      Place currentPlace = this.place;

      System.out.println("FireAnt died and explodes! Dealing " + damage + " damage to all bees!");

      // damage ALL bees in the same place
      // we use a copy of the array because damaging bees might remove them from the list
      Bee[] beesInPlace = currentPlace.getBees();
      for (Bee bee : beesInPlace) {
        bee.reduceArmor(damage);
        System.out.println("FireAnt explosion hit " + bee + " for " + damage + " damage!");
      }

      // now leave the place (die properly)
      System.out.println(this + " ran out of armor and expired");
      leavePlace();
    }
  }
}
