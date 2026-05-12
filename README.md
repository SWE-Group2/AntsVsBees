# Ants vs Some-Bees

`Ants vs Some-Bees` is a small Java/JavaFX strategy game based on the classic tower-defense style ant colony assignment. The player places ants in tunnels to stop waves of invading bees before they reach the queen.

The repository currently contains a working core game loop, JavaFX GUI, bee invasion logic, water places, and these implemented ant types:

- `HarvesterAnt`
- `ThrowerAnt`
- `ShortThrowerAnt`
- `LongThrowerAnt`
- `FireAnt`
- `WallAnt`
- `ScubaThrowerAnt`
- `HungryAnt`
- `SlowThrowerAnt`
- `StunThrowerAnt`
- `NinjaAnt`
- `BodyguardAnt`
- `QueenAnt`

Only ant classes that exist in `src/main/java/ants` are loaded into the in-game selector, so the game will run even though `antlist.properties` still lists a few future ant types that have not been implemented yet.

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

## Export JAR Package

Create the packaged JAR in `target/` with:

```bash
mvn clean package
```

The generated project JAR will be written to `target/avb-1.0.0-SNAPSHOT.jar`.

## Run

Start the JavaFX application with:

```bash
mvn javafx:run
```

## Test

The repository includes JUnit 5 tests for the game model, places, and implemented ant types.

Run the full suite from the project root with:

```bash
mvn test
```

The GitHub Actions workflow at `.github/workflows/test.yml` runs `mvn -B test` on every pull request targeting `main`.

If VS Code still shows stale Java errors after pulling these changes, reload the window or run `Java: Clean Java Language Server Workspace` so it re-imports the Maven project from `pom.xml`.

## Format Before Pushing

The project uses Spotless with Google Java Format for Java files and `sortPom` for `pom.xml`.

Before pushing, run:

```bash
mvn spotless:apply
mvn test
```

To check formatting without changing files, run:

```bash
mvn spotless:check
```

The CI workflow runs `mvn -B spotless:check` before `mvn -B test`, so unformatted code will fail the pull request checks.

## Gameplay

- Choose a difficulty level before starting a new game.
- Higher levels increase the challenge with tougher layouts and bee waves.
- Click an ant in the selector bar to choose it.
- Click a tunnel tile to place the selected ant if you have enough food.
- Click the remover icon to switch to removal mode, then click a placed ant to remove it.
- The game begins on the first click.
- Use `Pause` and `Resume` to temporarily stop and continue the game without losing the current turn, food, ants, bees, or wave state.
- Use `Reset` to restart the current level quickly.
- Use `Save` or press `S` to persist the current game for the logged-in user.
- Bees invade in waves from the hive and move toward the queen.
- You lose if bees reach the queen.
- You complete a level if all bees in the hive and colony are eliminated.
- Completing a level advances to the next difficulty level.
- Each completed or lost game records a score in the current user's game history.

## Saves and Game History

- User accounts are stored in `users.dat`.
- Saved games and score history are stored per user under `saves/`.
- The main menu lists saved games for the current user and the user's previous game results.
- Saved games restore the level, layout, food, placed ants, active bees, bee effects, and remaining waves.

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

### `ShortThrowerAnt`

- Food cost: `3`
- Armor: `1`
- Damage: `1`
- Range: targets the nearest bee between `0` and `2` places away

### `LongThrowerAnt`

- Food cost: `3`
- Armor: `1`
- Damage: `1`
- Range: targets the nearest bee `4` or more places away

### `FireAnt`

- Food cost: `4`
- Armor: `1`
- Effect: does nothing on its own turn, but deals `3` damage to all bees in its place when it dies

### `WallAnt`

- Food cost: `4`
- Armor: `4`
- Effect: blocks bees with high armor and does nothing on its own turn

### `ScubaThrowerAnt`

- Food cost: `5`
- Armor: `1`
- Damage: `1`
- Range: targets the nearest bee between `0` and `3` places away
- Water safe: can be deployed to `WaterPlace`

### `HungryAnt`

- Food cost: `4`
- Armor: `1`
- Effect: eats one random bee in its place, then digests for `3` turns before it can eat again

### `SlowThrowerAnt`

- Food cost: `4`
- Armor: `1`
- Damage: `0`
- Range: targets the nearest bee between `0` and `3` places away
- Effect: slows the target bee for `3` turns

### `StunThrowerAnt`

- Food cost: `6`
- Armor: `1`
- Damage: `0`
- Range: targets the nearest bee between `0` and `3` places away
- Effect: stuns the target bee for `1` turn

### `NinjaAnt`

- Effect: does not block bees and can damage bees that pass through its place

### `BodyguardAnt`

- Effect: protects another ant placed in the same tunnel tile

### `QueenAnt`

- Effect: marks the real queen ant and supports queen-specific win/loss behavior

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
- Difficulty levels and level progression are implemented
- Pause, resume, reset, save, load, score, and per-user game history are implemented
- Water places are implemented
- Core ant types listed above are implemented and covered by JUnit tests

What is still partial:

- The current test coverage focuses on the model layer rather than the JavaFX UI
