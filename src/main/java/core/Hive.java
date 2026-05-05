package core;

import bees.GhostBee;
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
    private Map<Integer, Bee[]> waves; // a mapping from attack times to the list of bees that will charge in

    /**
     * Creates a new hive, in which Bees have the given armor
     *
     * @param beeArmor
     *            The armor of the bees
     */
    public Hive(int beeArmor) {
        this(beeArmor, 0.2);
    }

    public Hive(int beeArmor, double ghostBeeChance) {
        super(NAME, null);
        this.beeArmor = beeArmor;
        this.ghostBeeChance = Math.max(0, Math.min(1, ghostBeeChance));
        this.waves = new HashMap<Integer, Bee[]>();
    }

    /**
     * Moves in the invaders who are attacking the colony at the given time.
     *
     * @param colony
     *            The colony to attack
     * @param currentTime
     *            The current time
     * @return An array of the bees who invaded (for animation/processing)
     */
    public Bee[] invade(AntColony colony, int currentTime) {
        Place[] exits = colony.getBeeEntrances();

        Bee[] wave = waves.get(currentTime);
        if (wave == null)
            return new Bee[0]; // return empty set if no bees attacking now

        for (Bee b : wave) // move all the bees in
        {
            int randExit = (int) (Math.random() * exits.length);
            b.moveTo(exits[randExit]); // move b to a random exit from the hive (entrance to the colony)
        }
        return wave; // return who invaded
    }

    /**
     * Adds a wave of attacking bees to this hive
     *
     * @param attackTime
     *            When the bees will attack
     * @param numBees
     *            The number of bees to attack
     */
    public void addWave(int attackTime, int numBees) {
        Bee[] bees = new Bee[numBees];
        for (int i = 0; i < bees.length; i++) {
            if (Math.random() < ghostBeeChance) {
                bees[i] = new GhostBee(beeArmor);
            } else {
                bees[i] = new Bee(beeArmor);
            }
            this.addInsect(bees[i]); // put the bee in Place
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
     * Returns an array of all the bees who are part of the attack (whether they are currently in the hive or not!)
     *
     * @return An array of Bees
     */
    public Bee[] getAllBees() {
        ArrayList<Bee> bees = new ArrayList<Bee>(); // easy temp work
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

    ///////////////////////////
    // Convenience factory methods //
    ///////////////////////////

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
     * @param difficulty
     *            a level from 1 (easiest) to 10 (hardest)
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
