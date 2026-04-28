package core;

import auth.UserStore;
import javafx.application.Application;
import javafx.stage.Stage;
import save.GameSnapshot;
import save.SaveManager;
import ui.LoginScreen;
import ui.MenuScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * A driver for the Ants vs. Some-Bees game.
 * Coordinates between login, menu, and game; owns the JavaFX Stage.
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
        MenuScreen menu = new MenuScreen(savesForCurrentUser(),
                                         this::startNewGame,
                                         this::startLoadedGame);
        stage.setScene(menu.getScene());
    }

    private void startNewGame() {
        AntColony colony = new AntColony(3, 8, 3, 2);
        Hive hive = Hive.makeFullHive();
        new AntGame(colony, hive, stage,
            (name, snapshot) -> {
                savesForCurrentUser().save(name, snapshot);
                showMenu();
            });
    }

    private void startLoadedGame(GameSnapshot s) {
        AntGame.fromSnapshot(s, stage,
            (name, snapshot) -> {
                savesForCurrentUser().save(name, snapshot);
                showMenu();
            });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
