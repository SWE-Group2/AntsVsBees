package factory;

import bees.GhostBee;
import bees.ZombieBee;
import core.Bee;

/**
 * Factory class for creating different types of bees. This is the Factory Pattern - centralises bee creation logic.
 * Previously this logic was scattered across Hive.addWave().
 */
public class BeeFactory {

    // chance of spawning a ghost bee (20%)
    private static final double DEFAULT_GHOST_CHANCE = 0.2;

    // chance of spawning a zombie bee (20%)
    private static final double DEFAULT_ZOMBIE_CHANCE = 0.2;

    private final double ghostChance;
    private final double zombieChance;

    /**
     * Creates a BeeFactory with default spawn chances.
     */
    public BeeFactory() {
        this(DEFAULT_GHOST_CHANCE, DEFAULT_ZOMBIE_CHANCE);
    }

    /**
     * Creates a BeeFactory with custom spawn chances. Pre-condition: chances must be between 0 and 1
     * 
     * @param ghostChance
     *            Chance of spawning a GhostBee
     * @param zombieChance
     *            Chance of spawning a ZombieBee
     */
    public BeeFactory(double ghostChance, double zombieChance) {
        assert ghostChance >= 0 && ghostChance <= 1 : "Ghost chance must be between 0 and 1";
        assert zombieChance >= 0 && zombieChance <= 1 : "Zombie chance must be between 0 and 1";
        this.ghostChance = ghostChance;
        this.zombieChance = zombieChance;
    }

    /**
     * Creates a random bee based on spawn chances. Uses Factory Pattern to encapsulate bee creation logic.
     * 
     * @param armor
     *            The armor of the bee to create
     * @return A new Bee, GhostBee, or ZombieBee
     */
    public Bee createBee(int armor) {
        double roll = Math.random();
        if (roll < ghostChance) {
            System.out.println("Factory created a GhostBee!");
            return new GhostBee(armor);
        } else if (roll < ghostChance + zombieChance) {
            System.out.println("Factory created a ZombieBee!");
            return new ZombieBee(armor);
        }
        return new Bee(armor);
    }

    /**
     * Creates a specific type of bee.
     * 
     * @param type
     *            The type of bee to create ("ghost", "zombie", "normal")
     * @param armor
     *            The armor of the bee
     * @return A new bee of the specified type
     */
    public Bee createBee(String type, int armor) {
        return switch (type.toLowerCase()) {
            case "ghost" -> new GhostBee(armor);
            case "zombie" -> new ZombieBee(armor);
            default -> new Bee(armor);
        };
    }
}