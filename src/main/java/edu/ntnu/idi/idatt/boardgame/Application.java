package edu.ntnu.idi.idatt.boardgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.game.GameController;
import edu.ntnu.idi.idatt.boardgame.game.GameManager;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.controllers.GameBoardController;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.player.JavaFXPlayer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The main application class.
 */
public class Application extends javafx.application.Application {
    public List<JavaFXPlayer> players = new ArrayList<>();
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
        Game game = gameManager.getGame("ladder");

        GameController gameController = new GameController();
        JavaFXPlayer player1 = new JavaFXPlayer(1, "Player 1", Color.RED, "default_pawn.png");
        JavaFXPlayer player2 = new JavaFXPlayer(2, "Player 2", Color.BLUE, "crown_pawn.png");
        List<Player> players = List.of(player1, player2);

        GameBoard gameBoard = new GameBoard.Builder(game)
                .addTiles()
                .resolveActionStyles()
                .addPlayers(players)
                .build();



        GameBoardController gameBoardController = new GameBoardController(gameBoard, gameController);

        gameController.startGame(game, players);

        VBox root = new VBox(10);
        root.getChildren().add(gameBoard);

        HBox controlPanel = new HBox(10);
        Button rollButton = new Button("Roll Dice");
        rollButton.setOnAction(e -> {
            if (gameController.isGameStarted() && !gameController.isGameEnded()) {
                gameController.rollDiceAndMoveCurrentPlayer();
            }
        });
        controlPanel.getChildren().add(rollButton);
        root.getChildren().add(controlPanel);

        Scene scene = new Scene(root, 1200, 1000);
        stage.setFullScreen(true);
        stage.setTitle("Board Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}