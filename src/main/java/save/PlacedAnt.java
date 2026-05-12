package save;

import java.io.Serializable;

public record PlacedAnt(String placeName, String antClassName) implements Serializable {
}
