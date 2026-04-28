package save;

import java.util.*;

public class SaveManager {

  // TODO: Change this to a csv file so it persists the life of the application
  private final Map<String, GameSnapshot> saves = new LinkedHashMap<>();

  public void save(String name, GameSnapshot snapshot) {
    if (name == "" || snapshot == null) {
      return;
    }
    saves.put(name, snapshot);
  }

  public GameSnapshot load(String name) {
    return saves.get(name);
  }

  public Set<String> list() {
    return saves.keySet();
  }

  public void delete(String name) {
    saves.remove(name);
  }
}
