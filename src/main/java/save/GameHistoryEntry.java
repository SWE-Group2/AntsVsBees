package save;

import java.io.Serializable;
import java.time.LocalDateTime;

public record GameHistoryEntry(String username, int difficultyLevel, int score, String outcome, int turn, int food,
        LocalDateTime completedAt) implements Serializable {

    @Override
    public String toString() {
        return completedAt + " - " + outcome + " - Level " + difficultyLevel + " - Score " + score;
    }
}
