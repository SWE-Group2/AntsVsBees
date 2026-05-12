package save;

import java.io.Serializable;

public record BeeState(String placeName, int armor, int slowedTurns, int stunnedTurns) implements Serializable {
}
