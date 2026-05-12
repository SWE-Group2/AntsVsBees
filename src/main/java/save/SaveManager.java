package save;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SaveManager {
    private static final Path SAVE_DIR = Paths.get("saves");

    private final String username;
    private final Path file;
    private StoreData data;

    public SaveManager() {
        this("default");
    }

    public SaveManager(String username) {
        this.username = username == null || username.isBlank() ? "default" : username;
        String safeName = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(this.username.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        this.file = SAVE_DIR.resolve(safeName + ".dat");
        this.data = loadData();
    }

    public void save(String name, GameSnapshot snapshot) {
        if (name == null || name.isBlank() || snapshot == null) {
            return;
        }
        data.saves.put(name, snapshot);
        persist();
    }

    public GameSnapshot load(String name) {
        return data.saves.get(name);
    }

    public Set<String> list() {
        return data.saves.keySet();
    }

    public void delete(String name) {
        data.saves.remove(name);
        persist();
    }

    public void addHistory(GameResult result) {
        if (result == null) {
            return;
        }
        data.history.add(new GameHistoryEntry(username, result.difficultyLevel(), result.score(), result.outcome(),
                result.turn(), result.food(), LocalDateTime.now()));
        persist();
    }

    public List<GameHistoryEntry> history() {
        List<GameHistoryEntry> copy = new ArrayList<>(data.history);
        Collections.reverse(copy);
        return copy;
    }

    private StoreData loadData() {
        if (!Files.exists(file)) {
            return new StoreData();
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof StoreData storeData) {
                return storeData;
            }
            return new StoreData();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not read saved games", e);
        }
    }

    private void persist() {
        try {
            Files.createDirectories(SAVE_DIR);
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static class StoreData implements Serializable {
        private final Map<String, GameSnapshot> saves = new LinkedHashMap<>();
        private final List<GameHistoryEntry> history = new ArrayList<>();
    }
}
