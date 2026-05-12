package core;

import ants.ThrowerAnt;
import bees.GhostBee;
import bees.ZombieBee;
import exceptions.InsufficientFoodException;
import exceptions.InvalidPlacementException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import places.WaterPlace;
import save.BeeState;
import save.GameSnapshot;
import save.PlacedAnt;
import save.WaveSpec;
import ui.GuideScreen;

/**
 * A class that controls the graphical game of Ants vs. Some-Bees. Converted from Swing to JavaFX.
 */
public class AntGame {
    private MusicManager musicManager;
    // game models
    private AntColony colony;
    private Hive hive;
    private DifficultyLevel difficultyLevel;
    private static final String ANT_FILE = "antlist.properties";
    private static final String ANT_PKG = "ants";
    private static final String WATER_PLACE_CLASS_NAME = "places.WaterPlace";

    // game clock & speed
    public static final int FPS = 30;
    public static final int TURN_SECONDS = 3;
    public static final double LEAF_SPEED = 0.3;
    private int turn;
    private int frameCount;
    private AnimationTimer clock;
    private long lastFrameTime = 0;

    // ant properties
    private final ArrayList<String> ANT_TYPES;
    private final Map<String, Image> ANT_IMAGES;
    private final Map<String, Color> LEAF_COLORS;

    // images
    private final Image TUNNEL_IMAGE = loadImage("img/tunnel.gif");
    private final Image WATER_TUNNEL_IMAGE = loadImage("img/water-tunnel.png");
    private final Image BEE_IMAGE = loadImage("img/bee.gif");
    private final Image ZOMBIE_BEE_IMAGE = loadImage("img/zombie_bee.gif");
    private final Image GHOST_BEE_IMAGE = loadImage("img/ghost_bee.gif");
    private final Image REMOVER_IMAGE = loadImage("img/remover.gif");

    // positioning constants
    public static final double FRAME_WIDTH = 1224;
    public static final double FRAME_HEIGHT = 900;
    public static final double ANT_IMAGE_WIDTH = 66;
    public static final double ANT_IMAGE_HEIGHT = 71;
    public static final double BEE_IMAGE_WIDTH = 73;
    public static final double BEE_IMAGE_HEIGHT = 78;
    public static final double PANEL_X = 20;
    public static final double PANEL_Y = 40;
    public static final double PANEL_PAD_W = 2;
    public static final double PANEL_PAD_H = 4;
    public static final double PLACE_X = 40;
    public static final double PLACE_Y = 180;
    public static final double PLACE_PAD_W = 10;
    public static final double PLACE_PAD_H = 10;
    public static final double PLACE_MARGIN = 10;
    public static final double HIVE_X = 1075;
    public static final double HIVE_Y = 250;
    public static final double CRYPT_HEIGHT = 650;
    public static final double LEAF_START_X = 30;
    public static final double LEAF_START_Y = 30;
    public static final double LEAF_END_X = 50;
    public static final double LEAF_END_Y = 30;
    public static final double LEAF_SIZE = 40;
    public static final double HELP_X = FRAME_WIDTH - 92;
    public static final double HELP_Y = 5;
    public static final double HELP_WIDTH = 72;
    public static final double HELP_HEIGHT = 32;
    public static final double MUSIC_BUTTON_X = FRAME_WIDTH - 350;
    public static final double MUSIC_BUTTON_Y = 5;
    public static final double MUSIC_BUTTON_WIDTH = 110;
    public static final double MUSIC_BUTTON_HEIGHT = 32;
    public static final double VOLUME_DOWN_X = FRAME_WIDTH - 232;
    public static final double VOLUME_UP_X = FRAME_WIDTH - 130;
    public static final double VOLUME_BUTTON_Y = 5;
    public static final double VOLUME_BUTTON_SIZE = 32;

    // clickable areas
    private Map<double[], Place> colonyAreas;
    private Map<Place, double[]> colonyRects;
    private Map<double[], Ant> antSelectorAreas;
    private double[] removerArea;
    private double[] helpArea;
    private double[] musicToggleArea;
    private double[] volumeDownArea;
    private double[] volumeUpArea;
    private Place tunnelEnd;
    private Ant selectedAnt;
    private Ant hoveredAnt;
    private Bee hoveredBee;

    // animations
    private Map<Bee, AnimPosition> allBeePositions;
    private ArrayList<AnimPosition> leaves;

    // JavaFX
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private boolean musicEnabled = true;

    // error message to show on screen when placement fails
    private String errorMessage = null;
    private int errorMessageTimer = 0;

    // Saving callback
    private final BiConsumer<String, GameSnapshot> onSaveRequested;
    private final Consumer<DifficultyLevel> onLevelCompleted;

    /** Creates a new AntGame with JavaFX rendering */
    public AntGame(AntColony colony, Hive hive, DifficultyLevel difficultyLevel, Stage stage,
            BiConsumer<String, GameSnapshot> saveRequested) {
        this(colony, hive, difficultyLevel, stage, saveRequested, null);
    }

    /** Creates a new AntGame with JavaFX rendering */
    public AntGame(AntColony colony, Hive hive, DifficultyLevel difficultyLevel, Stage stage,
            BiConsumer<String, GameSnapshot> saveRequested, Consumer<DifficultyLevel> levelCompleted) {
        this.colony = colony;
        this.hive = hive;
        this.difficultyLevel = difficultyLevel;
        this.frameCount = 0;
        this.turn = 0;
        this.onSaveRequested = saveRequested;
        this.onLevelCompleted = levelCompleted;

        ANT_TYPES = new ArrayList<>();
        ANT_IMAGES = new HashMap<>();
        LEAF_COLORS = new HashMap<>();
        initializeAnts();

        allBeePositions = new HashMap<>();
        initializeBees();
        leaves = new ArrayList<>();

        antSelectorAreas = new HashMap<>();
        colonyAreas = new HashMap<>();
        colonyRects = new HashMap<>();
        initializeAntSelector();
        initializeColony();

        // setup canvas
        canvas = new Canvas(FRAME_WIDTH, FRAME_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // mouse click handling
        canvas.setOnMousePressed(e -> {
            boolean shouldStartGame = handleClick(e.getX(), e.getY());
            if (shouldStartGame && !gameStarted) {
                gameStarted = true;
                clock.start();
                startMusic();
            }
            render();
        });
        canvas.setOnMouseMoved(e -> {
            hoveredAnt = getHoveredAnt(e.getX(), e.getY());
            hoveredBee = hoveredAnt == null ? getHoveredBee(e.getX(), e.getY()) : null;
            render();
        });
        canvas.setOnMouseExited(e -> {
            hoveredAnt = null;
            hoveredBee = null;
            render();
        });

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, FRAME_WIDTH, FRAME_HEIGHT);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.S && gameStarted && !gameOver) {
                if (onSaveRequested != null) {
                    GameSnapshot snapshot = capture();
                    System.out.println("[SAVE] " + snapshot);
                    onSaveRequested.accept("save-turn-" + turn, snapshot);
                }
            } else if (e.getCode() == KeyCode.H) {
                showHelpDialog();
            }
        });

        stage.setTitle("Ants vs. Some-Bees");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            if (clock != null)
                clock.stop();
            if (musicManager != null)
                musicManager.stopMusic();
            Platform.exit();
        });
        stage.show();

        musicManager = new MusicManager();

        // setup game loop
        clock = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    return;
                }
                // throttle to FPS
                if (now - lastFrameTime >= 1_000_000_000L / FPS) {
                    lastFrameTime = now;
                    nextFrame();
                }
            }
        };

        render(); // draw initial screen
    }

    // -------------------------
    // GAME LOGIC
    // -------------------------

    private void nextFrame() {
        if (gameOver)
            return;

        if (frameCount == 0) {
            System.out.println("TURN: " + turn);

            // ants act
            for (Ant ant : colony.getAllAnts()) {
                if (ant instanceof ThrowerAnt) {
                    Bee target = ((ThrowerAnt) ant).getTarget();
                    if (target != null)
                        createLeaf(ant, target);
                }
                ant.action(colony);
            }

            // bees act
            for (Bee bee : colony.getAllBees()) {
                bee.action(colony);
                startAnimation(bee);
            }

            // new invaders
            Bee[] invaders = hive.invade(colony, turn);
            for (Bee bee : invaders)
                startAnimation(bee);
        }

        if (frameCount == (int) (LEAF_SPEED * FPS)) {
            for (Map.Entry<Bee, AnimPosition> entry : allBeePositions.entrySet()) {
                if (entry.getKey().getArmor() <= 0) {
                    AnimPosition pos = entry.getValue();
                    pos.animateTo((int) pos.x, (int) CRYPT_HEIGHT, FPS * TURN_SECONDS);
                }
            }
        }

        // step animations
        for (AnimPosition pos : allBeePositions.values()) {
            if (pos.framesLeft > 0)
                pos.step();
        }
        Iterator<AnimPosition> iter = leaves.iterator();
        while (iter.hasNext()) {
            AnimPosition leaf = iter.next();
            if (leaf.framesLeft > 0)
                leaf.step();
            else
                iter.remove();
        }

        // count down error message timer
        if (errorMessageTimer > 0)
            errorMessageTimer--;
        else
            errorMessage = null;

        render();

        frameCount++;
        if (frameCount == FPS * TURN_SECONDS) {
            turn++;
            frameCount = 0;
        }

        // check end conditions halfway through turn
        if (frameCount == (int) (TURN_SECONDS * FPS / 2)) {
            if (colony.queenHasBees()) {
                gameOver = true;
                clock.stop();
                musicManager.stopMusic();
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Bzzzzz!");
                    alert.setHeaderText(null);
                    alert.setContentText("The ant queen has perished! Please try again.");
                    alert.showAndWait();
                    Platform.exit();
                });
            } else if (hive.getBees().length + colony.getAllBees().size() == 0) {
                gameOver = true;
                clock.stop();
                musicManager.stopMusic();
                difficultyLevel.next().ifPresentOrElse(nextLevel -> Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Level Complete");
                    alert.setHeaderText(difficultyLevel.displayName() + " complete!");
                    alert.setContentText("Get ready for " + nextLevel.displayName() + ".");
                    alert.showAndWait();
                    if (onLevelCompleted != null) {
                        onLevelCompleted.accept(nextLevel);
                    }
                }), () -> Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Yaaaay!");
                    alert.setHeaderText(null);
                    alert.setContentText("All levels are complete. You win!");
                    alert.showAndWait();
                    Platform.exit();
                }));
            }
        }
    }

    // -------------------------
    // RENDERING
    // -------------------------

    private void render() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        drawAntSelector();
        drawColony();
        drawBees();
        drawLeaves();
        drawHelpButton();
        drawMusicControls();
        drawHoveredGuide();

        // text
        String antString = "none";
        if (selectedAnt != null) {
            antString = selectedAnt.getClass().getSimpleName();
            antString = antString.substring(0, antString.length() - 3);
        }
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("SansSerif", 14));
        gc.fillText("Ant selected: " + antString, 20, 20);
        gc.fillText("Food: " + colony.getFood() + ", Turn: " + turn + ", Difficulty: " + difficultyLevel.displayName(),
                20, 140);

        // show error message on screen if there is one
        if (errorMessage != null) {
            gc.setFill(Color.RED);
            gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 16));
            gc.fillText(errorMessage, 20, 165);
        }

        if (!gameStarted) {
            gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 32));
            gc.setFill(Color.RED);
            gc.fillText("CLICK TO START", 350, 550);
            gc.setFont(Font.font("SansSerif", 16));
            gc.fillText("Press H or click Help for the guide", 350, 585);
            gc.fillText("Hover ants or bees to see abilities and advice", 350, 612);
        }
    }

    private void drawHelpButton() {
        gc.setFill(Color.WHITE);
        gc.fillRect(helpArea[0], helpArea[1], helpArea[2], helpArea[3]);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(helpArea[0], helpArea[1], helpArea[2], helpArea[3]);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));
        gc.fillText("Help", helpArea[0] + 18, helpArea[1] + 21);
    }

    private void drawMusicControls() {
        gc.setFill(gameStarted ? Color.WHITE : Color.LIGHTGRAY);
        gc.fillRect(musicToggleArea[0], musicToggleArea[1], musicToggleArea[2], musicToggleArea[3]);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(musicToggleArea[0], musicToggleArea[1], musicToggleArea[2], musicToggleArea[3]);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
        gc.fillText(musicManager != null && musicManager.isPlaying() ? "Pause Music" : "Play Music",
                musicToggleArea[0] + 22, musicToggleArea[1] + 21);

        drawVolumeButton(volumeDownArea, "-");
        drawVolumeButton(volumeUpArea, "+");

        gc.setFont(Font.font("SansSerif", 13));
        int volumePercent = musicManager == null ? 50 : (int) Math.round(musicManager.getVolume() * 100);
        gc.fillText("Vol " + volumePercent + "%", volumeDownArea[0] + 38, volumeDownArea[1] + 21);
    }

    private void drawVolumeButton(double[] area, String label) {
        gc.setFill(Color.WHITE);
        gc.fillRect(area[0], area[1], area[2], area[3]);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(area[0], area[1], area[2], area[3]);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));
        gc.fillText(label, area[0] + 11, area[1] + 22);
    }

    private void drawHoveredGuide() {
        if (hoveredAnt == null && hoveredBee == null) {
            return;
        }

        GameGuideData.GuideEntry guide = hoveredAnt != null
                ? GameGuideData.ANT_GUIDES.get(hoveredAnt.getClass().getSimpleName())
                : GameGuideData.BEE_GUIDES.get(hoveredBee.getClass().getSimpleName());
        if (guide == null) {
            return;
        }

        double x = 20;
        double y = FRAME_HEIGHT - 132;
        double width = 640;
        double height = 112;

        gc.setFill(Color.rgb(255, 255, 245));
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, width, height);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));
        String heading = hoveredAnt != null
                ? guide.name + " - Cost: " + hoveredAnt.getFoodCost()
                : guide.name + " - Armor: " + hoveredBee.getArmor();
        gc.fillText(heading, x + 16, y + 28);

        gc.setFont(Font.font("SansSerif", 14));
        gc.fillText("Ability: " + guide.ability, x + 16, y + 56);
        gc.fillText((hoveredAnt != null ? "Use: " : "Threat: ") + guide.use, x + 16, y + 82);
    }

    private void drawColony() {
        for (Map.Entry<double[], Place> entry : colonyAreas.entrySet()) {
            double[] rect = entry.getKey();
            Place place = entry.getValue();

            gc.setStroke(Color.BLACK);
            gc.strokeRect(rect[0], rect[1], rect[2], rect[3]);

            if (place != tunnelEnd && TUNNEL_IMAGE != null) {
                if (place.getClass().getName() == WATER_PLACE_CLASS_NAME) {
                    gc.drawImage(WATER_TUNNEL_IMAGE, rect[0], rect[1]);
                } else {
                    gc.drawImage(TUNNEL_IMAGE, rect[0], rect[1]);
                }
            }

            Ant ant = place.getAnt();
            if (ant != null) {
                if (ant instanceof ants.BodyguardAnt) {
                    ants.BodyguardAnt bodyguard = (ants.BodyguardAnt) ant;

                    // draw the protected ant first (behind the bodyguard)
                    Ant containedAnt = bodyguard.getContainedAnt();
                    if (containedAnt != null) {
                        Image containedImg = ANT_IMAGES.get(containedAnt.getClass().getName());
                        if (containedImg != null)
                            gc.drawImage(containedImg, rect[0] + PLACE_PAD_W + 8, rect[1] + PLACE_PAD_H);
                    }

                    // draw the bodyguard ant on top (slightly offset the other way)
                    Image bodyguardImg = ANT_IMAGES.get(ant.getClass().getName());
                    if (bodyguardImg != null)
                        gc.drawImage(bodyguardImg, rect[0] + PLACE_PAD_W - 8, rect[1] + PLACE_PAD_H);
                } else {
                    // normal ant - draw normally
                    Image img = ANT_IMAGES.get(ant.getClass().getName());
                    if (img != null)
                        gc.drawImage(img, rect[0] + PLACE_PAD_W, rect[1] + PLACE_PAD_H);
                }
            }
        }
    }

    private void drawBees() {
        for (Map.Entry<Bee, AnimPosition> entry : allBeePositions.entrySet()) {
            AnimPosition pos = entry.getValue();
            Bee bee = entry.getKey();
            if (bee instanceof GhostBee) {
                // draw ghost bee with ghost image
                if (GHOST_BEE_IMAGE != null)
                    gc.drawImage(GHOST_BEE_IMAGE, pos.x, pos.y);
            } else if (bee instanceof ZombieBee) {
                // draw zombie bee with zombie image
                if (ZOMBIE_BEE_IMAGE != null)
                    gc.drawImage(ZOMBIE_BEE_IMAGE, pos.x, pos.y);
                else if (BEE_IMAGE != null)
                    gc.drawImage(BEE_IMAGE, pos.x, pos.y);
            } else {
                // normal bee
                if (BEE_IMAGE != null)
                    gc.drawImage(BEE_IMAGE, pos.x, pos.y);
            }
        }
    }

    private void drawLeaves() {
        for (AnimPosition leafPos : leaves) {
            double angle = leafPos.framesLeft * Math.PI / 8;
            gc.setFill(leafPos.color != null ? leafPos.color : Color.GREEN);
            gc.save();
            gc.translate(leafPos.x, leafPos.y);
            gc.rotate(Math.toDegrees(angle));
            gc.fillOval(-LEAF_SIZE / 2, -LEAF_SIZE / 4, LEAF_SIZE, LEAF_SIZE / 2);
            gc.restore();
        }
    }

    private void drawAntSelector() {
        for (Map.Entry<double[], Ant> entry : antSelectorAreas.entrySet()) {
            double[] rect = entry.getKey();
            Ant ant = entry.getValue();

            if (ant.getFoodCost() > colony.getFood())
                gc.setFill(Color.GRAY);
            else if (ant == selectedAnt)
                gc.setFill(Color.BLUE);
            else
                gc.setFill(Color.WHITE);

            gc.fillRect(rect[0], rect[1], rect[2], rect[3]);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(rect[0], rect[1], rect[2], rect[3]);

            Image img = ANT_IMAGES.get(ant.getClass().getName());
            if (img != null)
                gc.drawImage(img, rect[0] + PANEL_PAD_W, rect[1] + PANEL_PAD_H);

            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("SansSerif", 12));
            gc.fillText("" + ant.getFoodCost(), rect[0] + rect[2] / 2, rect[1] + ANT_IMAGE_HEIGHT + 4 + PANEL_PAD_H);
        }

        // remover
        if (selectedAnt == null)
            gc.setFill(Color.BLUE);
        else
            gc.setFill(Color.WHITE);
        gc.fillRect(removerArea[0], removerArea[1], removerArea[2], removerArea[3]);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(removerArea[0], removerArea[1], removerArea[2], removerArea[3]);
        if (REMOVER_IMAGE != null)
            gc.drawImage(REMOVER_IMAGE, removerArea[0] + PANEL_PAD_W, removerArea[1] + PANEL_PAD_H);
    }

    // -------------------------
    // CLICK HANDLING
    // -------------------------

    private synchronized boolean handleClick(double mouseX, double mouseY) {
        if (contains(helpArea, mouseX, mouseY)) {
            showHelpDialog();
            return false;
        }
        if (contains(musicToggleArea, mouseX, mouseY)) {
            toggleMusic();
            return false;
        }
        if (contains(volumeDownArea, mouseX, mouseY)) {
            changeMusicVolume(-0.1);
            return false;
        }
        if (contains(volumeUpArea, mouseX, mouseY)) {
            changeMusicVolume(0.1);
            return false;
        }

        // check colony areas
        for (Map.Entry<double[], Place> entry : colonyAreas.entrySet()) {
            double[] rect = entry.getKey();
            if (contains(rect, mouseX, mouseY)) {
                if (selectedAnt == null) {
                    colony.removeAnt(entry.getValue());
                } else {
                    Ant deployable = buildAnt(selectedAnt.getClass().getName());
                    try {
                        // try to deploy the ant - throws custom exceptions if it fails
                        colony.deployAnt(entry.getValue(), deployable);
                    } catch (InsufficientFoodException e) {
                        // not enough food - show message on screen
                        showError("Not enough food! Need " + selectedAnt.getFoodCost() + ", have " + colony.getFood());
                        System.out.println("Cannot place ant: " + e.getMessage());
                    } catch (InvalidPlacementException e) {
                        // invalid placement - show message on screen
                        showError("Cannot place here: " + e.getMessage());
                        System.out.println("Cannot place ant here: " + e.getMessage());
                    }
                }
                return true;
            }
        }

        // check ant selector
        for (Map.Entry<double[], Ant> entry : antSelectorAreas.entrySet()) {
            double[] rect = entry.getKey();
            if (contains(rect, mouseX, mouseY)) {
                selectedAnt = entry.getValue();
                return true;
            }
        }

        // check remover
        if (contains(removerArea, mouseX, mouseY)) {
            selectedAnt = null;
        }
        return true;
    }

    private Ant getHoveredAnt(double mouseX, double mouseY) {
        for (Map.Entry<double[], Ant> entry : antSelectorAreas.entrySet()) {
            if (contains(entry.getKey(), mouseX, mouseY)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private Bee getHoveredBee(double mouseX, double mouseY) {
        for (Map.Entry<Bee, AnimPosition> entry : allBeePositions.entrySet()) {
            AnimPosition pos = entry.getValue();
            double[] beeRect = {pos.x, pos.y, BEE_IMAGE_WIDTH, BEE_IMAGE_HEIGHT};
            if (contains(beeRect, mouseX, mouseY)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Shows an error message on screen for a short time.
     * 
     * @param message
     *            The error message to show
     */
    private void showError(String message) {
        this.errorMessage = message;
        // show for 3 seconds (FPS * 3)
        this.errorMessageTimer = FPS * 3;
    }

    private boolean contains(double[] rect, double x, double y) {
        return x >= rect[0] && x <= rect[0] + rect[2] && y >= rect[1] && y <= rect[1] + rect[3];
    }

    // -------------------------
    // ANIMATIONS
    // -------------------------

    private void startAnimation(Bee b) {
        AnimPosition anim = allBeePositions.get(b);
        if (anim == null)
            return;
        if (anim.framesLeft == 0) {
            double[] rect = colonyRects.get(b.getPlace());
            if (rect != null && !contains(rect, anim.x, anim.y))
                anim.animateTo((int) (rect[0] + PLACE_PAD_W), (int) (rect[1] + PLACE_PAD_H), FPS * TURN_SECONDS);
        }
    }

    private void createLeaf(Ant source, Bee target) {
        double[] antRect = colonyRects.get(source.getPlace());
        double[] beeRect = colonyRects.get(target.getPlace());
        if (antRect == null || beeRect == null)
            return;

        int startX = (int) (antRect[0] + LEAF_START_X);
        int startY = (int) (antRect[1] + LEAF_START_Y);
        int endX = (int) (beeRect[0] + LEAF_END_Y);
        int endY = (int) (beeRect[1] + LEAF_END_Y);

        AnimPosition leaf = new AnimPosition(startX, startY);
        leaf.animateTo(endX, endY, (int) (LEAF_SPEED * FPS));
        leaf.color = LEAF_COLORS.get(source.getClass().getName());
        leaves.add(leaf);
    }

    // -------------------------
    // INITIALIZATION
    // -------------------------

    private void initializeAnts() {
        InputStream antStream = AntGame.class.getClassLoader().getResourceAsStream(ANT_FILE);
        if (antStream == null) {
            System.out.println("Error loading ant properties: resource not found: " + ANT_FILE);
            return;
        }

        try (Scanner sc = new Scanner(antStream, StandardCharsets.UTF_8)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.matches("\\w.*")) {
                    String[] parts = line.split(",");
                    String antType = ANT_PKG + "." + parts[0].trim();
                    try {
                        Class.forName(antType);
                        ANT_TYPES.add(antType);
                        ANT_IMAGES.put(antType, loadImage(parts[1].trim()));
                        if (parts.length > 2) {
                            int rgb = Integer.parseInt(parts[2].trim());
                            ANT_IMAGES.put(antType + "_color", null);
                            LEAF_COLORS.put(antType, intToColor(rgb));
                        }
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        }
    }

    private void initializeBees() {
        Bee[] bees = this.hive.getBees();
        for (Bee bee : bees) {
            if (bee instanceof GhostBee) {
                allBeePositions.put(bee, new AnimPosition((int) (HIVE_X), (int) (HIVE_Y + (100))));
            } else if (bee instanceof ZombieBee) {
                allBeePositions.put(bee, new AnimPosition((int) (HIVE_X), (int) (HIVE_Y + (200))));
            } else {
                allBeePositions.put(bee, new AnimPosition((int) (HIVE_X), (int) (HIVE_Y + (300))));
            }

        }
    }

    private void initializeColony() {
        double posX = PLACE_X;
        double posY = PLACE_Y;
        double width = BEE_IMAGE_WIDTH + 2 * PLACE_PAD_W;
        double height = BEE_IMAGE_HEIGHT + 2 * PLACE_PAD_H;
        int row = 0;
        posX += (width + PLACE_MARGIN) / 2;

        for (Place place : colony.getPlaces()) {
            if (place.getExit() == colony.getQueenPlace()) {
                posX = PLACE_X + (width + PLACE_MARGIN) / 2;
                posY = PLACE_Y + row * (height + PLACE_MARGIN);
                row++;
            }

            double[] clickable = {posX, posY, width, height};
            colonyAreas.put(clickable, place);
            colonyRects.put(place, clickable);
            posX += width + PLACE_MARGIN;
        }

        // queen location
        double[] queenRect = {0, PLACE_Y + (row - 1) * (height + PLACE_MARGIN) / 2, 0, 0};
        tunnelEnd = colony.getQueenPlace();
        colonyAreas.put(queenRect, tunnelEnd);
        colonyRects.put(tunnelEnd, queenRect);
    }

    private void initializeAntSelector() {
        double posX = PANEL_X;
        double posY = PANEL_Y;
        double width = ANT_IMAGE_WIDTH + 2 * PANEL_PAD_W;
        double height = ANT_IMAGE_HEIGHT + 2 * PANEL_PAD_H;

        helpArea = new double[]{HELP_X, HELP_Y, HELP_WIDTH, HELP_HEIGHT};
        musicToggleArea = new double[]{MUSIC_BUTTON_X, MUSIC_BUTTON_Y, MUSIC_BUTTON_WIDTH, MUSIC_BUTTON_HEIGHT};
        volumeDownArea = new double[]{VOLUME_DOWN_X, VOLUME_BUTTON_Y, VOLUME_BUTTON_SIZE, VOLUME_BUTTON_SIZE};
        volumeUpArea = new double[]{VOLUME_UP_X, VOLUME_BUTTON_Y, VOLUME_BUTTON_SIZE, VOLUME_BUTTON_SIZE};
        removerArea = new double[]{posX, posY, width, height};
        posX += width + 2;

        for (String antType : ANT_TYPES) {
            double[] clickable = {posX, posY, width, height};
            Ant ant = buildAnt(antType);
            if (ant != null)
                antSelectorAreas.put(clickable, ant);
            posX += width + 2;
        }
    }

    // -------------------------
    // UTILITIES
    // -------------------------

    private Image loadImage(String filename) {
        try (InputStream imageStream = AntGame.class.getClassLoader().getResourceAsStream(filename)) {
            if (imageStream != null)
                return new Image(imageStream);
        } catch (Exception e) {
            System.err.println("Error loading image: " + filename);
        }
        System.err.println("Image resource not found: " + filename);
        return null;
    }

    private Color intToColor(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return Color.rgb(r, g, b);
    }

    private void showHelpDialog() {
        boolean wasRunning = gameStarted && !gameOver && clock != null;
        if (wasRunning) {
            clock.stop();
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("How to Play");
        alert.setHeaderText("How to Play");
        alert.getButtonTypes().setAll(ButtonType.CLOSE);

        ScrollPane guide = new ScrollPane(GuideScreen.createGuideContent(620, false));
        guide.setFitToWidth(true);
        guide.setPrefViewportWidth(700);
        guide.setPrefViewportHeight(520);
        alert.getDialogPane().setContent(guide);
        alert.showAndWait();

        if (wasRunning) {
            lastFrameTime = 0;
            clock.start();
        }
        render();
    }

    private void startMusic() {
        if (musicEnabled && musicManager != null) {
            musicManager.playBackgroundMusic("audio/consumerism.mp3");
        }
    }

    private void toggleMusic() {
        if (!gameStarted || musicManager == null) {
            return;
        }

        if (musicManager.isPlaying()) {
            musicManager.pauseMusic();
            musicEnabled = false;
        } else {
            musicEnabled = true;
            musicManager.resumeMusic();
            if (!musicManager.isPlaying()) {
                musicManager.playBackgroundMusic("audio/consumerism.mp3");
            }
        }
    }

    private void changeMusicVolume(double amount) {
        if (musicManager != null) {
            musicManager.setVolume(musicManager.getVolume() + amount);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Ant buildAnt(String antType) {
        try {
            Class antClass = Class.forName(antType);
            Constructor constructor = antClass.getConstructor();
            return (Ant) constructor.newInstance();
        } catch (Exception e) {
        }
        return null;
    }

    // capture current state
    public GameSnapshot capture() {
        List<PlacedAnt> ants = colony.getAllAnts().stream()
                .map(a -> new PlacedAnt(a.getPlace().getName(), a.getClass().getName())).toList();
        List<BeeState> bees = colony.getAllBees().stream()
                .map(b -> new BeeState(b.getPlace().getName(), b.getArmor(), b.getSlowedTurns(), b.getStunnedTurns()))
                .toList();
        List<WaveSpec> remaining = hive.getRemainingWaves(turn);
        List<String> water = Arrays.stream(colony.getPlaces()).filter(p -> p instanceof WaterPlace).map(Place::getName)
                .toList();

        int numTunnels = colony.getBeeEntrances().length;
        int tunnelLength = colony.getPlaces().length / numTunnels;

        return new GameSnapshot(difficultyLevel.number(), numTunnels, tunnelLength, water, colony.getFood(), turn, ants,
                bees, remaining, hive.getBeeArmor());
    }

    // reconstruct from snapshot — alternative constructor
    public static AntGame fromSnapshot(GameSnapshot snapshot, Stage stage, BiConsumer<String, GameSnapshot> onSave) {
        return fromSnapshot(snapshot, stage, onSave, null);
    }

    // reconstruct from snapshot — alternative constructor
    public static AntGame fromSnapshot(GameSnapshot snapshot, Stage stage, BiConsumer<String, GameSnapshot> onSave,
            Consumer<DifficultyLevel> onLevelCompleted) {
        DifficultyLevel level = DifficultyLevel.forNumber(snapshot.difficultyLevel());
        AntColony colony = level.createColony();
        Hive hive = level.createHive();
        AntGame game = new AntGame(colony, hive, level, stage, onSave, onLevelCompleted);
        game.turn = snapshot.turn();
        game.render();
        return game;
    }

    // -------------------------
    // ANIMATION HELPER CLASS
    // -------------------------

    private static class AnimPosition {
        double x, y;
        double dx, dy;
        int framesLeft;
        Color color;

        public AnimPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void step() {
            x += dx;
            y += dy;
            framesLeft--;
        }

        public void animateTo(int nx, int ny, int frames) {
            framesLeft = frames;
            dx = (nx - x) / framesLeft;
            dy = (ny - y) / framesLeft;
        }
    }
}
