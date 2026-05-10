package core;

import bees.GhostBee;
import bees.ZombieBee;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import save.WaveSpec;

/**
 * Represents a hive--which contains the bees that will attack!
 *
 * @author Joel
 * @version Fall 2014
 */
public class Hive extends Place {
    public static final String NAME = "Hive";

    private int beeArmor; // armor for all the bees
    private double ghostBeeChance;
    private double zombieBeeChance;
    private Map<Integer, Bee[]> waves; // a mapping from attack times to the list of bees

    /**
     * Creates a new hive, in which Bees have the given armor
     *
     * @param beeArmor The armor of the bees
     */
    public Hive(int beeArmor) {
        this(beeArmor, 0.2, 0.2);
    }

    public Hive(int beeArmor, double ghostBeeChance) {
        this(beeArmor, ghostBeeChance, 0.2);
    }

    /**
     * Creates a new hive with custom chances for ghost and zombie bees
     *
     * @param beeArmor The armor of the bees
     * @param ghostBeeChance Chance (0-1) of spawning a GhostBee
     * @param zombieBeeChance Chance (0-1) of spawning a ZombieBee
     */
    public Hive(int beeArmor, double ghostBeeChance, double zombieBeeChance) {
        super(NAME, null);
        this.beeArmor = beeArmor;
        this.ghostBeeChance = Math.max(0, Math.min(1, ghostBeeChance));
        this.zombieBeeChance = Math.max(0, Math.min(1, zombieBeeChance));
        this.waves = new HashMap<Integer, Bee[]>();
    }

    /**
     * Moves in the invaders who are attacking the colony at the given time.
     *
     * @param colony The colony to attack
     * @param currentTime The current time
     * @return An array of the bees who invaded
     */
    public Bee[] invade(AntColony colony, int currentTime) {
        Place[] exits = colony.getBeeEntrances();

        Bee[] wave = waves.get(currentTime);
        if (wave == null)
            return new Bee[0];

        for (Bee b : wave) {
            int randExit = (int) (Math.random() * exits.length);
            b.moveTo(exits[randExit]);
        }
        return wave;
    }

    /**
     * Adds a wave of attacking bees to this hive.
     * Each bee has a chance to be a ZombieBee, GhostBee, or a normal Bee.
     *
     * @param attackTime When the bees will attack
     * @param numBees The number of bees to attack
     */
    public void addWave(int attackTime, int numBees) {
        Bee[] bees = new Bee[numBees];
        for (int i = 0; i < bees.length; i++) {
            double roll = Math.random();
            if (roll < ghostBeeChance) {
                // spawn a GhostBee
                bees[i] = new GhostBee(beeArmor);
                System.out.println("GhostBee added to wave at turn " + attackTime);
            } else if (roll < ghostBeeChance + zombieBeeChance) {
                // spawn a ZombieBee
                bees[i] = new ZombieBee(beeArmor);
                System.out.println("ZombieBee added to wave at turn " + attackTime);
            } else {
                // spawn a normal Bee
                bees[i] = new Bee(beeArmor);
            }
            this.addInsect(bees[i]);
        }
        waves.put(attackTime, bees);
    }

    public java.util.List<WaveSpec> getRemainingWaves(int currentTurn) {
        java.util.List<WaveSpec> out = new java.util.ArrayList<>();
        for (java.util.Map.Entry<Integer, Bee[]> e : waves.entrySet()) {
            if (e.getKey() > currentTurn) {
                out.add(new WaveSpec(e.getKey(), e.getValue().length));
            }
        }
        return out;
    }

    /**
     * Returns an array of all the bees who are part of the attack
     *
     * @return An array of Bees
     */
    public Bee[] getAllBees() {
        ArrayList<Bee> bees = new ArrayList<Bee>();
        for (Bee[] wave : waves.values()) {
            for (int i = 0; i < wave.length; i++)
                bees.add(wave[i]);
        }
        return bees.toArray(new Bee[0]);
    }

    /**
     * Returns the armor that bees in this hive are created with.
     *
     * @return the bee armor template
     */
    public int getBeeArmor() {
        return beeArmor;
    }

    public double getGhostBeeChance() {
        return ghostBeeChance;
    }

    public double getZombieBeeChance() {
        return zombieBeeChance;
    }

    /**
     * Makes a hive with two attacking bees
     *
     * @return A filled hive
     */
    public static Hive makeTestHive() {
        Hive hive = new Hive(3);
        hive.addWave(2, 1);
        hive.addWave(3, 1);
        return hive;
    }

    /**
     * Makes a hive filled with attacking bees
     *
     * @return A filled hive
     */
    public static Hive makeFullHive() {
        Hive hive = new Hive(3);
        hive.addWave(2, 1);
        for (int i = 3; i < 15; i += 2)
            hive.addWave(i, 1);
        hive.addWave(15, 8);
        return hive;
    }

    /**
     * Makes a hive scaled for the selected difficulty level.
     *
     * @param difficulty a level from 1 (easiest) to 10 (hardest)
     * @return A filled hive scaled to the requested level
     */
    public static Hive makeDifficultyHive(int difficulty) {
        return DifficultyLevel.forNumber(difficulty).createHive();
    }

    /**
     * Makes a hive filled with a huge number of powerful attacking bees
     *
     * @return A filled, angry hive
     */
    public static Hive makeInsaneHive() {
        Hive hive = new Hive(4);
        hive.addWave(1, 2);
        for (int i = 3; i < 15; i++)
            hive.addWave(i, 1);
        hive.addWave(15, 20);
        return hive;
    }
}