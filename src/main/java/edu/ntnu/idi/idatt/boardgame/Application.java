package edu.ntnu.idi.idatt.boardgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.boardgame.board.GameBoardBuilder;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.game.GameManager;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
        GridPane boardView = new GameBoardBuilder(gameManager.getGame("ladder"))
                .addTiles()
                .resolveActionStyles()
                .build();

        Scene scene = new Scene(boardView, 800, 800);
        scene.getStylesheets().add("main.css");
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}