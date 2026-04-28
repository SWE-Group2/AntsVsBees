package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import save.GameSnapshot;
import save.SaveManager;

import java.util.function.Consumer;

public class MenuScreen 
{
    private final Scene scene;

    public MenuScreen(SaveManager saves,
                      Runnable onNewGame,
                      Consumer<GameSnapshot> onLoad) 
    {

        ListView<String> savesList = new ListView<>();
        savesList.getItems().setAll(saves.list());

        Button newGame = new Button("New Game");
        newGame.setOnAction(e -> onNewGame.run());

        Button load = new Button("Load");
        load.setOnAction(e -> {
            String name = savesList.getSelectionModel().getSelectedItem();
            if (name != null) onLoad.accept(saves.load(name));
        });

        VBox root = new VBox(10, new Label("Ants vs. Some-Bees"),
                                  newGame, savesList, load);
        root.setPadding(new Insets(40));
        this.scene = new Scene(root, 1024, 768);
    }

    public Scene getScene() 
    { 
        return scene; 
    }
}
