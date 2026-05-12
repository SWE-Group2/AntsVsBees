package core;

import factory.BeeFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import save.WaveSpec;

/**
 * Represents a hive--which contains the bees that will attack! Now uses BeeFactory (Factory Pattern) to create bees.
 *
 * @author Joel
 * @version Fall 2014
 */
public class Hive extends Place {
    public static final String NAME = "Hive";

    private int beeArmor;
    private double ghostBeeChance;
    private double zombieBeeChance;
    private Map<Integer, Bee[]> waves;

    // factory for creating bees (Factory Pattern)
    private final BeeFactory beeFactory;

    /**
     * Creates a new hive with default bee chances.
     * 
     * @param beeArmor
     *            The armor of the bees
     */
    public Hive(int beeArmor) {
        this(beeArmor, 0.2, 0.2);
    }

    public Hive(int beeArmor, double ghostBeeChance) {
        this(beeArmor, ghostBeeChance, 0.2);
    }

    /**
     * Creates a new hive with custom bee spawn chances.
     * 
     * @param beeArmor
     *            The armor of the bees
     * @param ghostBeeChance
     *            Chance of spawning a GhostBee
     * @param zombieBeeChance
     *            Chance of spawning a ZombieBee
     */
    public Hive(int beeArmor, double ghostBeeChance, double zombieBeeChance) {
        super(NAME, null);
        this.beeArmor = beeArmor;
        this.ghostBeeChance = Math.max(0, Math.min(1, ghostBeeChance));
        this.zombieBeeChance = Math.max(0, Math.min(1, zombieBeeChance));
        this.waves = new HashMap<Integer, Bee[]>();
        // create factory with the given chances
        this.beeFactory = new BeeFactory(ghostBeeChance, zombieBeeChance);
    }

    /**
     * Moves in the invaders who are attacking the colony at the given time.
     * 
     * @param colony
     *            The colony to attack
     * @param currentTime
     *            The current time
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
     * Adds a wave of attacking bees using BeeFactory (Factory Pattern).
     * 
     * @param attackTime
     *            When the bees will attack
     * @param numBees
     *            The number of bees to attack
     */
    public void addWave(int attackTime, int numBees) {
        Bee[] bees = new Bee[numBees];
        for (int i = 0; i < bees.length; i++) {
            // use factory to create bee (Factory Pattern)
            bees[i] = beeFactory.createBee(beeArmor);
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
     * Returns an array of all the bees in the attack.
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

    public int getBeeArmor() {
        return beeArmor;
    }

    public double getGhostBeeChance() {
        return ghostBeeChance;
    }

    public double getZombieBeeChance() {
        return zombieBeeChance;
    }

    public static Hive makeTestHive() {
        Hive hive = new Hive(3);
        hive.addWave(2, 1);
        hive.addWave(3, 1);
        return hive;
    }

    public static Hive makeFullHive() {
        Hive hive = new Hive(3);
        hive.addWave(2, 1);
        for (int i = 3; i < 15; i += 2)
            hive.addWave(i, 1);
        hive.addWave(15, 8);
        return hive;
    }

    public static Hive makeDifficultyHive(int difficulty) {
        return DifficultyLevel.forNumber(difficulty).createHive();
    }

    public static Hive makeInsaneHive() {
        Hive hive = new Hive(4);
        hive.addWave(1, 2);
        for (int i = 3; i < 15; i++)
            hive.addWave(i, 1);
        hive.addWave(15, 20);
        return hive;
    }
}