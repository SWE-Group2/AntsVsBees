package ui;

import core.DifficultyLevel;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DifficultyScreen {
    private final Scene scene;

    public DifficultyScreen(Consumer<Integer> onStart, Runnable onBack) {
        Label title = new Label("Choose Difficulty");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 32));

        Label subtitle = new Label("Select a level. Higher levels add tougher bees, larger waves, and harder layouts.");
        subtitle.setFont(Font.font("SansSerif", 16));

        ToggleGroup levels = new ToggleGroup();
        GridPane levelGrid = new GridPane();
        levelGrid.setHgap(12);
        levelGrid.setVgap(12);
        levelGrid.setAlignment(Pos.CENTER);

        for (DifficultyLevel level : DifficultyLevel.all()) {
            RadioButton option = new RadioButton(level.displayName() + " - " + level.summary());
            option.setUserData(level.number());
            option.setToggleGroup(levels);
            option.setMinWidth(310);
            option.setFont(Font.font("SansSerif", 16));
            levelGrid.add(option, (level.number() - 1) % 2, (level.number() - 1) / 2);

            if (level.number() == 1) {
                option.setSelected(true);
            }
        }

        Button start = new Button("Start Game");
        start.setPrefWidth(120);
        start.setOnAction(e -> onStart.accept((Integer) levels.getSelectedToggle().getUserData()));

        Button back = new Button("Back");
        back.setPrefWidth(120);
        back.setOnAction(e -> onBack.run());

        HBox buttons = new HBox(10, back, start);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(18, title, subtitle, levelGrid, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        this.scene = new Scene(root, 1024, 768);
    }

    public Scene getScene() {
        return scene;
    }
}
