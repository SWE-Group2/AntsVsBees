package core;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * A driver for the Ants vs. Some-Bees game
 */
public class AntsVsSomeBees extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        AntColony colony = new AntColony(3, 8, 0, 2);
        Hive hive = Hive.makeFullHive();
        new AntGame(colony, hive, primaryStage);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}