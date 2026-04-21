# Ants vs Some-Bees

`Ants vs Some-Bees` is a small Java/JavaFX strategy game based on the classic tower-defense style ant colony assignment. The player places ants in tunnels to stop waves of invading bees before they reach the queen.

The repository currently contains a working core game loop, JavaFX GUI, bee invasion logic, and two implemented ant types:

- `HarvesterAnt`
- `ThrowerAnt`

Only ant classes that exist in `src/ants` are loaded into the in-game selector, so the game will run even though `antlist.properties` lists additional ant types that have not been implemented yet.

## Project Structure

```text
AVB/
├── src/
│   ├── ants/        # Concrete ant implementations
│   └── core/        # Core game engine, model, and JavaFX UI
├── img/             # Sprite and UI image assets
├── lib/             # JavaFX and other bundled JAR/native libraries
├── antlist.properties
├── runfx.sh
└── README.md
```

## Requirements

- A JDK with `java` and `javac` available on your path
- JavaFX libraries are already bundled in `lib/`

Notes:

- The Eclipse project metadata is configured for `JavaSE-25` in `.classpath`.
- The project compiled successfully from source with the bundled dependencies.

## Build

From the project root:

```bash
mkdir -p bin
javac --module-path lib --add-modules javafx.controls,javafx.fxml -cp "lib/*" -d bin $(find src -name '*.java')
```

## Run

After compiling, launch the game with:

```bash
./runfx.sh
```

Equivalent command:

```bash
java -cp "bin:lib/*" --module-path lib --add-modules javafx.controls,javafx.fxml core.AntsVsSomeBees
```

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

`antlist.properties` defines ant classes, image assets, and optional projectile colors for the UI. The game attempts to load each listed ant class reflectively from the `ants` package. If a class is missing, it is skipped silently.

This makes the file useful as a simple extension point:

1. Add a new ant class under `src/ants/`
2. Add its entry to `antlist.properties`
3. Recompile and run the game

## Main Entry Points

- [`src/core/AntsVsSomeBees.java`](src/core/AntsVsSomeBees.java): JavaFX application launcher
- [`src/core/AntGame.java`](src/core/AntGame.java): game loop, rendering, input handling, and reflective ant loading
- [`src/core/AntColony.java`](src/core/AntColony.java): colony layout, food management, and insect lookup
- [`src/core/Hive.java`](src/core/Hive.java): bee wave scheduling and invasion logic

## Current Status

What works:

- Source compiles successfully
- JavaFX launcher and GUI code are present
- Bee waves, ant placement, food tracking, and win/loss conditions are implemented

What is still partial:

- Most ant types referenced in `antlist.properties` are not implemented in `src/ants`
- There are no automated tests in the repository yet
