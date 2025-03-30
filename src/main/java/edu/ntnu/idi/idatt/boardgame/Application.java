package edu.ntnu.idi.idatt.boardgame;

import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.DieComponent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.game.GameManager;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The main application class.
 */
public class Application extends javafx.application.Application {
    public List<Player> players = new ArrayList<>();
    public int currentPlayerIndex = 0;

    /**
     * The main entry point for all JavaFX applications.
     * 
     * @param stage the primary stage for this application, onto which the
     *              application scene can be set.
     * @throws IOException if an input or output exception occurs.
     */
    @Override
    public void start(Stage stage) throws IOException {
        LocalFileProvider fileProvider = new LocalFileProvider();
        GameManager gameManager = new GameManager(fileProvider);
        GridPane boardView = new GameBoard.Builder(gameManager.getGame("ladder"))
                .addTiles()
                .resolveActionStyles()
                .build();

        DieComponent die = new DieComponent();
        VBox root = new VBox();
        root.getChildren().addAll(boardView,die);
        Random random = new Random();
        Timeline diceRollTimeline = new Timeline();
        die.setValue(4);

        for (int i = 0; i < 10; i++) {
            KeyFrame keyFrame = new KeyFrame(
                Duration.millis(100 * i),
                event -> {
                    int face = random.nextInt(6); // 0 to 5
                    System.out.println("Rolling die... " + String.valueOf(face + 1));

                    die.setValue(face + 1);
                }
            );
            diceRollTimeline.getKeyFrames().add(keyFrame);
        }
        diceRollTimeline.setCycleCount(1);

        Button rollButton = new Button("Roll");
        rollButton.setOnAction(event -> {
            diceRollTimeline.play();
            int face = random.nextInt(6);
            die.setValue(face + 1);
        });
        root.getChildren().add(rollButton);

        Scene scene = new Scene(root, 800, 800);
        scene.getStylesheets().add("main.css");
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}