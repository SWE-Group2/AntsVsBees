package save;

import java.io.Serializable;
import java.util.List;

public record GameSnapshot(int difficultyLevel, int numTunnels, int tunnelLength, List<String> waterPlaceNames,
        int food, int turn, List<PlacedAnt> ants, List<BeeState> bees, List<WaveSpec> remainingWaves,
        int beeArmorTemplate) implements Serializable {
}
