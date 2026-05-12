package save;

import java.io.Serializable;

public record WaveSpec(int attackTime, int numBees) implements Serializable {
}
