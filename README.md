# Ants vs Some-Bees

`Ants vs Some-Bees` is a small Java/JavaFX strategy game based on the classic tower-defense style ant colony assignment. The player places ants in tunnels to stop waves of invading bees before they reach the queen.

The repository currently contains a working core game loop, JavaFX GUI, bee invasion logic, and two implemented ant types:

- `HarvesterAnt`
- `ThrowerAnt`

Only ant classes that exist in `src/main/java/ants` are loaded into the in-game selector, so the game will run even though `antlist.properties` lists additional ant types that have not been implemented yet.

## Project Structure

```text
AVB/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/       # Application source
│   │   └── resources/  # Images and ant configuration
│   └── test/
│       └── java/       # JUnit tests
└── README.md
```

## Requirements

- JDK 25
- Maven 3.9+

Notes:

- The Maven build now manages JavaFX and JUnit dependencies.
- The project no longer relies on checked-in IDE metadata or local `lib/` jars.

## Build

From the project root:

```bash
mvn compile
```

## Run

Start the JavaFX application with:

```bash
mvn javafx:run
```

## Test

The repository includes JUnit 5 tests for the game model and currently implemented ant types.

Run the full suite from the project root with:

```bash
mvn test
```

The GitHub Actions workflow at `.github/workflows/test.yml` runs `mvn -B test` on every pull request targeting `main`.

If VS Code still shows stale Java errors after pulling these changes, reload the window or run `Java: Clean Java Language Server Workspace` so it re-imports the Maven project from `pom.xml`.

## Gameplay

- The colony is created with 3 tunnels of length 8.
- You start with 2 food.
- Click an ant in the selector bar to choose it.
- Click a tunnel tile to place the selected ant if you have enough food.
- Click the remover icon to switch to removal mode, then click a placed ant to remove it.
- The game begins on the first click.
- Bees invade in waves from the hive and move toward the queen.
- You lose if bees reach the queen.
- You win if all bees in the hive and colony are eliminated.

## Implemented Ant Types

### `HarvesterAnt`

- Food cost: `2`
- Armor: `1`
- Effect: adds `1` food to the colony each turn

### `ThrowerAnt`

- Food cost: `4`
- Armor: `1`
- Damage: `1`
- Range: targets the nearest bee between `0` and `3` places away

## Configuration

`src/main/resources/antlist.properties` defines ant classes, image assets, and optional projectile colors for the UI. The game loads this file and the image assets from the classpath, then attempts to load each listed ant class reflectively from the `ants` package. If a class is missing, it is skipped silently.

This makes the file useful as a simple extension point:

1. Add a new ant class under `src/main/java/ants/`
2. Add its entry to `src/main/resources/antlist.properties`
3. Run `mvn test` or `mvn javafx:run`

## Main Entry Points

- [`src/main/java/core/AntsVsSomeBees.java`](src/main/java/core/AntsVsSomeBees.java): JavaFX application launcher
- [`src/main/java/core/AntGame.java`](src/main/java/core/AntGame.java): game loop, rendering, input handling, and reflective ant loading
- [`src/main/java/core/AntColony.java`](src/main/java/core/AntColony.java): colony layout, food management, and insect lookup
- [`src/main/java/core/Hive.java`](src/main/java/core/Hive.java): bee wave scheduling and invasion logic

## Current Status

What works:

- Source and tests are structured for Maven
- JavaFX launcher and GUI code are present
- Bee waves, ant placement, food tracking, and win/loss conditions are implemented

What is still partial:

- Most ant types referenced in `antlist.properties` are not implemented in `src/main/java/ants`
- The current test coverage focuses on the model layer rather than the JavaFX UI
