package ui;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import save.GameSnapshot;
import save.SaveManager;

public class MenuScreen {
    private final Scene scene;

    public MenuScreen(SaveManager saves, Runnable onNewGame, Runnable onGuide, Consumer<GameSnapshot> onLoad) {

        ListView<String> savesList = new ListView<>();
        savesList.getItems().setAll(saves.list());
        savesList.setPrefHeight(160);

        ListView<String> historyList = new ListView<>();
        historyList.getItems().setAll(saves.history().stream().map(Object::toString).toList());
        historyList.setPrefHeight(180);

        Button newGame = new Button("New Game");
        newGame.setOnAction(e -> onNewGame.run());

        Button guide = new Button("How to Play");
        guide.setOnAction(e -> onGuide.run());

        Button load = new Button("Load");
        load.setOnAction(e -> {
            String name = savesList.getSelectionModel().getSelectedItem();
            if (name != null)
                onLoad.accept(saves.load(name));
        });

        VBox root = new VBox(10, new Label("Ants vs. Some-Bees"), newGame, guide, new Label("Saved Games"), savesList,
                load, new Label("Game History"), historyList);
        root.setPadding(new Insets(40));
        this.scene = new Scene(root, 1024, 768);
    }

    public Scene getScene() {
        return scene;
    }
}
