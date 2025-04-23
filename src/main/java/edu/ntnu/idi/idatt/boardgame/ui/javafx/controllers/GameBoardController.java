package edu.ntnu.idi.idatt.boardgame.ui.javafx.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observer;
import edu.ntnu.idi.idatt.boardgame.game.GameController;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEndedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameStartedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerMovedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.TileActionEvent;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;

/**
 * Controller that connects the GameController with the GameBoard UI component.
 * <p>
 * This class observes game events and updates the game board UI accordingly,
 * particularly handling player movement animations.
 * </p>
 */
public class GameBoardController implements Observer<GameController, GameEvent> {
    private static final Logger logger = Logger.getLogger(GameBoardController.class.getName());
    private static final int DEFAULT_ANIMATION_DURATION = 500;
    private static final int LADDER_ANIMATION_DURATION = 800;

    private final GameBoard gameBoard;
    private final GameController gameController;

    /**
     * Creates a new GameBoardController.
     *
     * @param gameBoard      the game board UI component
     * @param gameController the game controller
     */
    public GameBoardController(GameBoard gameBoard, GameController gameController) {
        this.gameBoard = gameBoard;
        this.gameController = gameController;

        gameController.addListener(this);

        logger.info("GameBoardController initialized");
    }

    /**
     * Updates the UI in response to game events.
     *
     * @param event the game event
     */
    @Override
    public void update(GameEvent event) {
        logger.log(Level.INFO, "Received game event: {0}", event.getClass().getSimpleName());

        if (event instanceof GameStartedEvent) {
            handleGameStarted((GameStartedEvent) event);
        } else if (event instanceof PlayerMovedEvent) {
            handlePlayerMoved((PlayerMovedEvent) event);
        } else if (event instanceof TileActionEvent) {
            handleTileAction((TileActionEvent) event);
        } else if (event instanceof GameEndedEvent) {
            handleGameEnded((GameEndedEvent) event);
        }
    }

    /**
     * Handles game started events.
     *
     * @param event the game started event
     */
    private void handleGameStarted(GameStartedEvent event) {
        logger.info("Game started with " + event.getPlayers().size() + " players");
        gameBoard.getAnimationQueue().stopAndClear();
    }

    /**
     * Handles player moved events.
     *
     * @param event the player moved event
     */
    private void handlePlayerMoved(PlayerMovedEvent event) {
        logger.info("Player " + event.getPlayer().getName() + " moved from tile " +
                (event.getFromTile() != null ? event.getFromTile().getTileId() : "null") +
                " to tile " + event.getToTile().getTileId());

        gameBoard.animatePlayerMovement(
                event.getPlayer(),
                event.getFromTile(),
                event.getToTile(),
                DEFAULT_ANIMATION_DURATION);
    }

    /**
     * Handles tile action events.
     *
     * @param event the tile action event
     */
    private void handleTileAction(TileActionEvent event) {
        TileAction action = event.getTileAction();
        Player player = event.getPlayer();

        if (action instanceof LadderAction) {
            LadderAction ladderAction = (LadderAction) action;
            Tile startTile = event.getTile();
            Tile destinationTile = ladderAction.getDestinationTile();

            System.out.println("Ladder action from " + startTile.getTileId() + " to " + destinationTile.getTileId());

            if (startTile != destinationTile) {
                logger.info("Triggering ladder animation");
                gameBoard.animateLadderMovement(
                        player,
                        startTile,
                        destinationTile,
                        LADDER_ANIMATION_DURATION);
            } else {
                logger.warning("Invalid tiles for ladder animation");
            }
        } else {
            logger.info("Non-ladder tile action: " + action.getClass().getSimpleName());
        }
    }

    /**
     * Handles game ended events.
     *
     * @param event the game ended event
     */
    private void handleGameEnded(GameEndedEvent event) {
        logger.info("Game ended. Winner: " + event.getWinner().getName());

        // TODO: Handle game end UI updates
    }
}
