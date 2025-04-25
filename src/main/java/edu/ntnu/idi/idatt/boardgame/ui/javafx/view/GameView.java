package edu.ntnu.idi.idatt.boardgame.ui.javafx.view;

import java.util.List;
import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.game.GameController;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.router.NavigationContext;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.controllers.GameBoardController;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameView implements IView {
    private Game game;
    private GameController gameController;
    private List<Player> players;

    @Override
    public void load(NavigationContext<?> ctx) {
        String gameId = ctx.getParamOrThrow("gameId");

        this.game = Application.getGameManager().getGame(gameId);

        this.players = Application.getPlayerManager().getPlayers();

        this.gameController = new GameController();
    }

    @Override
    public Parent createRoot() {

        GameBoard gameBoard = new GameBoard.Builder(game).addTiles().resolveActionStyles()
                .addPlayers(players).build();



        GameBoardController gameBoardController =
                new GameBoardController(gameBoard, gameController);

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

        return root;
    }

}
