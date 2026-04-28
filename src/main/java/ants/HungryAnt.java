package ants;

import core.Ant;
import core.AntColony;
import core.Bee;

/**
 * A HungryAnt is an ant that eats bees! It randomly picks a bee from its location and instantly
 * kills it. However, after eating a bee, it needs 3 turns to digest before it can eat again.
 *
 * <p>Food Cost: 4, Armor: 1
 *
 * @author muhammad hassan
 */
public class HungryAnt extends Ant {
  // how many turns the ant needs to digest after eating a bee
  public static final int DIGESTION_TIME = 3;

  // tracks how many turns of digesting are left (0 means ready to eat)
  private int turnsDigesting;

  /**
   * Creates a new HungryAnt. Starts with 0 digestion turns (ready to eat straight away).
   * Post-condition: food cost is 4, armor is 1, digesting is 0
   */
  public HungryAnt() {
    super(1); // armor = 1
    this.foodCost = 4;
    this.turnsDigesting = 0; // not digesting at the start
  }

  /**
   * Returns how many turns of digesting are left.
   *
   * @return number of turns left digesting
   */
  public int getTurnsDigesting() {
    return turnsDigesting;
  }

  /**
   * Each turn, the HungryAnt either: - Eats a random bee if it is not digesting - Counts down its
   * digestion timer if it is still digesting
   *
   * <p>Pre-condition: colony must not be null Post-condition: either a bee is eaten or digestion
   * timer decreases by 1
   *
   * @param colony The ant colony (used to check game state)
   */
  @Override
  public void action(AntColony colony) {
    // make sure colony exists
    assert colony != null : "Colony cannot be null";

    if (turnsDigesting > 0) {
      // still digesting - count down the timer
      turnsDigesting--;
      System.out.println("HungryAnt is digesting. Turns left: " + turnsDigesting);
    } else {
      // get all bees in this location
      Bee[] bees = place.getBees();

      // pick a random bee if there are any
      Bee target = bees.length > 0 ? bees[(int) (Math.random() * bees.length)] : null;

      if (target != null) {
        // eat the bee by reducing its armor to 0 (instantly kills it)
        target.reduceArmor(target.getArmor());
        System.out.println(
            "HungryAnt ate " + target + "! Now digesting for " + DIGESTION_TIME + " turns.");

        // start digestion timer
        turnsDigesting = DIGESTION_TIME;
      } else {
        // no bee here to eat
        System.out.println("HungryAnt is hungry but no bee is here!");
      }
    }
  }
}
