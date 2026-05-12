package ui;

import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GuideScreen {
    private static final double WIDTH = 1024;
    private static final double HEIGHT = 768;

    private final Scene scene;

    public GuideScreen(Runnable onBack) {
        Label title = new Label("How to Play");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 32));

        VBox guideContent = createGuideContent(860, true);

        ScrollPane scrollPane = new ScrollPane(guideContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(560);

        Button back = new Button("Back");
        back.setPrefWidth(120);
        back.setOnAction(e -> onBack.run());

        HBox buttons = new HBox(back);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(18, title, scrollPane, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        this.scene = new Scene(root, WIDTH, HEIGHT);
    }

    public static VBox createGuideContent(double maxTextWidth, boolean includeImage) {
        VBox guideContent = new VBox(16);
        guideContent.setPadding(new Insets(24));
        guideContent.getChildren().addAll(
                section("Goal",
                        "Stop every bee before it reaches the queen. You win when the hive and tunnels are clear. "
                                + "You lose if any bee reaches the queen."),
                section("Starting a Game",
                        "Choose New Game, pick a difficulty, then click the board to begin. Higher difficulties add "
                                + "tougher waves and harder layouts."),
                section("Controls",
                        "Click an ant in the top selector bar, then click a tunnel tile to place it. Click the remover "
                                + "icon, then click a placed ant to remove it. Press S during a running game to save "
                                + "and return to the menu. Press H or click Help during gameplay to reopen this guide."),
                section("Food",
                        "Food is the resource used to place ants. The number under each ant shows its food cost. "
                                + "Harvester ants generate more food each turn."),
                section("Tunnels and Bees",
                        "Bees enter from the hive on the right and move left through tunnels toward the queen. Ants "
                                + "block, attack, slow, stun, or otherwise affect bees depending on their type."),
                section("Ant Types",
                        "Throwers attack from range. Short and Long Throwers specialize at different distances. "
                                + "Wall ants absorb damage. Fire ants damage bees when they die. Scuba Throwers can "
                                + "survive water. Hungry ants eat bees, then digest. Slow and Stun Throwers control "
                                + "bee movement. Bodyguard ants protect another ant. Queen ants strengthen the colony."));

        for (javafx.scene.Node node : guideContent.getChildren()) {
            if (node instanceof Label label) {
                label.setMaxWidth(maxTextWidth);
            }
        }

        ImageView explanation = includeImage ? loadExplanationImage() : null;
        if (explanation != null) {
            Label imageTitle = new Label("Game Screen");
            imageTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 18));
            guideContent.getChildren().addAll(imageTitle, explanation);
        }

        return guideContent;
    }

    private static Label section(String heading, String body) {
        Label label = new Label(heading + "\n" + body);
        label.setWrapText(true);
        label.setFont(Font.font("SansSerif", 16));
        return label;
    }

    private static ImageView loadExplanationImage() {
        InputStream imageStream = GuideScreen.class.getClassLoader().getResourceAsStream("img/gui_explanation.png");
        if (imageStream == null) {
            return null;
        }

        ImageView imageView = new ImageView(new Image(imageStream));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(640);
        return imageView;
    }

    public Scene getScene() {
        return scene;
    }
}
