package core;

import java.util.ArrayList;

/**
 * Represents a location in the game
 *
 * @author Joel
 * @version Fall 2014
 */
public class Place {
  private String name;
  private Place exit;
  private Place entrance;
  private ArrayList<Bee> bees;
  private Ant ant;

  public Place(String name, Place exit) {
    this.name = name;
    this.exit = exit;
    this.entrance = null;
    this.bees = new ArrayList<Bee>();
    this.ant = null;
  }

  public Place(String name) {
    this(name, null);
  }

  public Ant getAnt() {
    return ant;
  }

  /**
   * Checks whether an insect can be added to this place. Normal places accept all insects.
   * WaterPlace overrides this to only allow watersafe insects.
   *
   * @param insect The insect to check
   * @return true if the insect can be added
   */
  public boolean canAddInsect(Insect insect) {
    return true;
  }

  public Bee[] getBees() {
    return bees.toArray(new Bee[0]);
  }

  public Bee getClosestBee(int minDistance, int maxDistance) {
    Place p = this;
    for (int dist = 0; p != null && dist <= maxDistance; dist++) {
      if (dist >= minDistance && p.bees.size() > 0)
        return p.bees.get((int) (Math.random() * p.bees.size()));
      p = p.entrance;
    }
    return null;
  }

  public String getName() {
    return name;
  }

  public Place getExit() {
    return exit;
  }

  public void setEntrance(Place entrance) {
    this.entrance = entrance;
  }

  public Place getEntrance() {
    return this.entrance;
  }

  public Ant getMainAnt() {
    if (ant instanceof ants.BodyguardAnt) {
      ants.BodyguardAnt bodyguard = (ants.BodyguardAnt) ant;
      if (bodyguard.getContainedAnt() != null) {
        return bodyguard.getContainedAnt();
      }
    }
    return ant;
  }

  /**
   * Adds an ant to the place. Special case: if a BodyguardAnt is being added and there is already
   * an ant here, the BodyguardAnt wraps around the existing ant and protects it. Also if there is
   * already a BodyguardAnt here, a new ant can go inside it.
   *
   * @param ant The ant to add to the place.
   */
  public void addInsect(Ant ant) {
    if (this.ant == null) {
      // no ant here yet - just add normally
      this.ant = ant;
      ant.setPlace(this);
    } else if (ant instanceof ants.BodyguardAnt) {
      // adding a BodyguardAnt on top of an existing ant
      ants.BodyguardAnt bodyguard = (ants.BodyguardAnt) ant;
      bodyguard.setContainedAnt(this.ant);
      this.ant = bodyguard;
      bodyguard.setPlace(this);
      System.out.println("BodyguardAnt is now protecting " + bodyguard.getContainedAnt());
    } else if (this.ant instanceof ants.BodyguardAnt) {
      // there is already a BodyguardAnt here - put the new ant inside it
      ants.BodyguardAnt bodyguard = (ants.BodyguardAnt) this.ant;
      if (bodyguard.getContainedAnt() == null) {
        bodyguard.setContainedAnt(ant);
        ant.setPlace(this);
        System.out.println("BodyguardAnt is now protecting " + ant);
      } else {
        System.out.println("Already an ant inside the BodyguardAnt in " + this);
      }
    } else {
      System.out.println("Already an ant in " + this);
    }
  }

  public void addInsect(Bee bee) {
    bees.add(bee);
    bee.setPlace(this);
  }

  /**
   * Removes the ant from the place. If the ant being removed is a BodyguardAnt, the ant it was
   * protecting takes its place.
   *
   * @param ant The ant to remove from the place
   */
  public void removeInsect(Ant ant) {
    if (this.ant == ant) {
      if (this.ant instanceof ants.BodyguardAnt) {
        ants.BodyguardAnt bodyguard = (ants.BodyguardAnt) this.ant;
        Ant protected_ant = bodyguard.getContainedAnt();
        this.ant = protected_ant;
        if (protected_ant != null) {
          protected_ant.setPlace(this);
          System.out.println("BodyguardAnt died! " + protected_ant + " is now exposed.");
        }
        ant.setPlace(null);
      } else {
        this.ant = null;
        ant.setPlace(null);
      }
    } else {
      System.out.println(ant + " is not in " + this);
    }
  }

  public void removeInsect(Bee bee) {
    if (bees.contains(bee)) {
      bees.remove(bee);
      bee.setPlace(null);
    } else System.out.println(bee + " is not in " + this);
  }

  public String toString() {
    return name;
  }
}
