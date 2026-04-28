package ui;

import auth.UserStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

public class LoginScreen {
    public static final double WIDTH = 1024;
    public static final double HEIGHT = 768;

    private final Scene scene;

    public LoginScreen(UserStore users, Consumer<String> onLoggedIn) {
        Label title = new Label("Ants vs. Some-Bees");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 32));

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setMaxWidth(240);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setMaxWidth(240);

        Label status = new Label("");

        Button login = new Button("Login");
        login.setPrefWidth(120);
        login.setOnAction(e -> {
            if (users.authenticate(username.getText(), password.getText())) {
                onLoggedIn.accept(username.getText());
            } else {
                status.setTextFill(Color.RED);
                status.setText("Invalid username or password");
            }
        });

        Button register = new Button("Create Account");
        register.setPrefWidth(120);
        register.setOnAction(e -> {
            if (users.register(username.getText(), password.getText())) {
                status.setTextFill(Color.GREEN);
                status.setText("Account created. You can now log in.");
            } else {
                status.setTextFill(Color.RED);
                status.setText("Username taken or invalid input");
            }
        });

        HBox buttons = new HBox(10, login, register);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(12, title, username, password, buttons, status);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        this.scene = new Scene(root, WIDTH, HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}
