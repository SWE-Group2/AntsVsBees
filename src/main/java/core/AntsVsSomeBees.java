package core;

import auth.UserStore;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.stage.Stage;
import save.GameSnapshot;
import save.SaveManager;
import ui.DifficultyScreen;
import ui.GuideScreen;
import ui.LoginScreen;
import ui.MenuScreen;

/**
 * A driver for the Ants vs. Some-Bees game. Coordinates between login, menu, and game; owns the JavaFX Stage.
 */
public class AntsVsSomeBees extends Application {
    private final UserStore userStore = new UserStore();
    private final Map<String, SaveManager> savesByUser = new HashMap<>();
    private Stage stage;
    private String currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Ants vs. Some-Bees");
        stage.setResizable(false);
        showLogin();
        stage.show();
    }

    private void showLogin() {
        currentUser = null;
        LoginScreen login = new LoginScreen(userStore, this::onLoggedIn);
        stage.setScene(login.getScene());
    }

    private void onLoggedIn(String username) {
        this.currentUser = username;
        showMenu();
    }

    private SaveManager savesForCurrentUser() {
        return savesByUser.computeIfAbsent(currentUser, u -> new SaveManager());
    }

    private void showMenu() {
        MenuScreen menu = new MenuScreen(savesForCurrentUser(), this::showDifficulty, this::showGuide,
                this::startLoadedGame);
        stage.setScene(menu.getScene());
    }

    private void showGuide() {
        GuideScreen guide = new GuideScreen(this::showMenu);
        stage.setScene(guide.getScene());
    }

    private void showDifficulty() {
        DifficultyScreen difficulty = new DifficultyScreen(this::startNewGame, this::showMenu);
        stage.setScene(difficulty.getScene());
    }

    private void startNewGame(int difficulty) {
        DifficultyLevel level = DifficultyLevel.forNumber(difficulty);
        startLevel(level);
    }

    private void startLevel(DifficultyLevel level) {
        AntColony colony = level.createColony();
        Hive hive = level.createHive();
        new AntGame(colony, hive, level, stage, (name, snapshot) -> {
            savesForCurrentUser().save(name, snapshot);
            showMenu();
        }, this::startLevel);
    }

    private void startLoadedGame(GameSnapshot s) {
        AntGame.fromSnapshot(s, stage, (name, snapshot) -> {
            savesForCurrentUser().save(name, snapshot);
            showMenu();
        }, this::startLevel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
