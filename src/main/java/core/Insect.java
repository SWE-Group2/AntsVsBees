package core;

import exceptions.InvalidArmorException;

/**
 * Represents an insect (e.g., an Ant or a Bee) in the game. Uses custom InvalidArmorException instead of generic
 * IllegalArgumentException.
 *
 * @author Joel
 * @version Fall 2014
 */
public abstract class Insect {
    protected int armor; // insect's current armor
    protected Place place; // insect's current location
    protected boolean watersafe; // if insect is watersafe

    /**
     * Creates a new Insect with the given armor in the given location. Pre-condition: armor must be greater than 0
     * 
     * @param armor
     *            The insect's armor
     * @param place
     *            The insect's location
     * @throws InvalidArmorException
     *             if armor is 0 or less
     */
    public Insect(int armor, Place place) {
        if (armor <= 0)
            throw new InvalidArmorException(armor);
        this.armor = armor;
        this.place = place;
    }

    /**
     * Creates an Insect with the given armor. The insect's location is null.
     * 
     * @param armor
     *            The insect's armor
     */
    public Insect(int armor) {
        this(armor, null);
    }

    /**
     * Sets the insect's current location.
     * 
     * @param place
     *            The insect's current location
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * Returns the insect's current location.
     * 
     * @return the insect's current location
     */
    public Place getPlace() {
        return this.place;
    }

    /**
     * Returns the insect's current armor.
     * 
     * @return the insect's current armor
     */
    public int getArmor() {
        return this.armor;
    }

    /**
     * Reduces the insect's current armor (e.g., through damage). Pre-condition: amount must be greater than 0
     * 
     * @param amount
     *            The amount to decrease the armor by
     */
    public void reduceArmor(int amount) {
        assert amount > 0 : "Damage amount must be greater than 0";
        this.armor -= amount;
        if (this.armor <= 0) {
            System.out.println(this + " ran out of armor and expired");
            leavePlace();
        }
    }

    /**
     * Returns whether this insect can survive in water places.
     * 
     * @return true if watersafe, false otherwise
     */
    public boolean isWatersafe() {
        return this.watersafe;
    }

    /**
     * Has the insect move out of its current location. Abstract so subclasses can define special leave behaviour.
     */
    public abstract void leavePlace();

    /**
     * The insect takes an action on its turn.
     * 
     * @param colony
     *            The colony in which this action takes place
     */
    public abstract void action(AntColony colony);

    public String toString() {
        return this.getClass().getName() + "[" + armor + ", " + place + "]";
    }
}