package core;

import java.util.List;

public final class DifficultyLevel {
    private static final List<DifficultyLevel> LEVELS = List.of(new DifficultyLevel(1, 3, 6, 3, 4, 2, 5, 3, 1, 3, 0.10),
            new DifficultyLevel(2, 3, 6, 3, 4, 2, 4, 3, 1, 4, 0.15),
            new DifficultyLevel(3, 4, 7, 3, 4, 2, 4, 3, 1, 5, 0.20),
            new DifficultyLevel(4, 4, 7, 3, 3, 3, 3, 3, 1, 6, 0.25),
            new DifficultyLevel(5, 4, 7, 3, 3, 3, 3, 3, 1, 7, 0.30),
            new DifficultyLevel(6, 5, 8, 3, 3, 3, 2, 2, 2, 8, 0.35),
            new DifficultyLevel(7, 5, 8, 2, 3, 4, 2, 2, 2, 9, 0.40),
            new DifficultyLevel(8, 5, 8, 2, 2, 4, 2, 2, 2, 10, 0.45),
            new DifficultyLevel(9, 6, 8, 2, 2, 4, 1, 2, 2, 11, 0.50),
            new DifficultyLevel(10, 6, 8, 2, 2, 5, 1, 2, 2, 12, 0.55));

    private final int number;
    private final int numTunnels;
    private final int tunnelLength;
    private final int moatFrequency;
    private final int startingFood;
    private final int beeArmor;
    private final int firstAttackTurn;
    private final int attackInterval;
    private final int beesPerWave;
    private final int finalWaveBees;
    private final double ghostBeeChance;

    private DifficultyLevel(int number, int numTunnels, int tunnelLength, int moatFrequency, int startingFood,
            int beeArmor, int firstAttackTurn, int attackInterval, int beesPerWave, int finalWaveBees,
            double ghostBeeChance) {
        this.number = number;
        this.numTunnels = numTunnels;
        this.tunnelLength = tunnelLength;
        this.moatFrequency = moatFrequency;
        this.startingFood = startingFood;
        this.beeArmor = beeArmor;
        this.firstAttackTurn = firstAttackTurn;
        this.attackInterval = attackInterval;
        this.beesPerWave = beesPerWave;
        this.finalWaveBees = finalWaveBees;
        this.ghostBeeChance = ghostBeeChance;
    }

    public static List<DifficultyLevel> all() {
        return LEVELS;
    }

    public static DifficultyLevel forNumber(int number) {
        return LEVELS.stream().filter(level -> level.number == number).findFirst().orElse(LEVELS.get(0));
    }

    public int number() {
        return number;
    }

    public String displayName() {
        return "Level " + number;
    }

    public String summary() {
        return "Tunnels: " + numTunnels + ", food: " + startingFood + ", bees: " + beesPerWave + "/wave";
    }

    public AntColony createColony() {
        return new AntColony(numTunnels, tunnelLength, moatFrequency, startingFood);
    }

    public Hive createHive() {
        Hive hive = new Hive(beeArmor, ghostBeeChance);
        for (int attackTime = firstAttackTurn; attackTime < 15; attackTime += attackInterval) {
            hive.addWave(attackTime, beesPerWave);
        }
        hive.addWave(15, finalWaveBees);
        return hive;
    }

    public int beeArmor() {
        return beeArmor;
    }

    @Override
    public String toString() {
        return displayName() + " (" + summary() + ")";
    }
}
