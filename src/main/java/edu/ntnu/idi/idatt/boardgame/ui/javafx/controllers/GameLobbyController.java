package edu.ntnu.idi.idatt.boardgame.ui.javafx.controllers;

import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.freeze.FreezeAction;
import edu.ntnu.idi.idatt.boardgame.actions.immunity.ImmunityAction;
import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observer;
import edu.ntnu.idi.idatt.boardgame.game.GameController;
import edu.ntnu.idi.idatt.boardgame.game.events.DiceRolledEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEndedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameStartedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerMovedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerTurnChangedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.QuestionAskedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.TileActionEvent;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.audio.GameSoundEffects;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.view.GameLobbyView;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

/**
 * Controller that connects the GameController with the GameBoard UI component.
 * <p>
 * This class observes game events and updates the game board UI accordingly, particularly handling
 * player movement animations.
 * </p>
 */
public class GameLobbyController implements Observer<GameController, GameEvent> {

  private static final Logger logger = Logger.getLogger(GameLobbyController.class.getName());

  private final GameLobbyView gameLobbyView;
  private final GameController gameController;

  @Getter
  private final ObservableList<Player> players;


  /**
   * Creates a new GameLobbyController.
   *
   * @param gameLobbyView  the game view
   * @param gameController the game controller
   */
  public GameLobbyController(GameLobbyView gameLobbyView, GameController gameController) {
    this.gameLobbyView = gameLobbyView;
    this.gameController = gameController;

    this.players = FXCollections.observableArrayList(gameController.getPlayers());

    gameController.addListener(this);

    logger.info("GameLobbyController initialized");
  }

  /**
   * Updates the UI in response to game events.
   *
   * @param event the game event
   */
  @Override
  public void update(GameEvent event) {
    logger.log(Level.INFO, "Received game event: {0}", event.getClass().getSimpleName());

    switch (event) {
      case GameStartedEvent gameStartedEvent -> handleGameStarted(gameStartedEvent);
      case DiceRolledEvent diceRolledEvent -> handleDiceRolled(diceRolledEvent);
      case PlayerMovedEvent playerMovedEvent -> handlePlayerMoved(playerMovedEvent);
      case TileActionEvent tileActionEvent -> handleTileAction(tileActionEvent);
      case PlayerTurnChangedEvent playerTurnChangedEvent ->
          handlePlayerTurnChangeEvent(playerTurnChangedEvent);
      case QuestionAskedEvent questionAskedEvent -> handleQuestionAsked(questionAskedEvent);
      case GameEndedEvent gameEndedEvent -> handleGameEnded(gameEndedEvent);
      default -> logger.warning("Unhandled event type: " + event.getClass().getSimpleName());
    }
  }

  /**
   * Exits the game and stops all animations.
   */
  public void exitGame() {
    gameLobbyView.getGameBoard().getAnimationQueue().stopAndClear();
    gameLobbyView.getGameBoard().setVisible(false);
    gameLobbyView.getGameBoard().setDisable(true);
    Application.router.navigate("/home");
  }

  /**
   * Handles game started events.
   *
   * @param event the game started event
   */
  private void handleGameStarted(GameStartedEvent event) {
    logger.info("Game started with " + event.getPlayers().size() + " players");
    gameLobbyView.getGameBoard().getAnimationQueue().stopAndClear();
    gameLobbyView.updateCurrentPlayerLabel(gameController.getCurrentPlayer());
    gameLobbyView.updateCurrentRound(gameController.getRoundCount());
  }


  /**
   * Handles player turn change events.
   *
   * @param event the player turn change event
   */
  private void handlePlayerTurnChangeEvent(PlayerTurnChangedEvent event) {
    logger.info("Player turn changed to " + event.getCurrentPlayer().getName());
    gameLobbyView.updateCurrentPlayerLabel(event.getCurrentPlayer());
    gameLobbyView.updateCurrentRound(gameController.getRoundCount());
    gameLobbyView.setRollDiceButtonDisabled(false);
    this.players.setAll(gameController.getPlayers());
  }

  /**
   * Handles player moved events.
   *
   * @param event the player moved event
   */
  private void handlePlayerMoved(PlayerMovedEvent event) {
    logger.info(
        "Player " + event.getPlayer().getName() + " moved from tile " + (event.getFromTile() != null
            ? event.getFromTile().getTileId() : "null") + " to tile " + event.getToTile()
            .getTileId());

    gameLobbyView.getGameBoard()
        .animatePlayerMovement(event.getPlayer(), event.getFromTile(), event.getToTile());
    gameLobbyView.setRollDiceButtonDisabled(false);


  }

  private void handleDiceRolled(DiceRolledEvent event) {
    gameLobbyView.setRollDiceButtonDisabled(true);
    gameLobbyView.animateDice(event.getIndividualRolls());
    gameLobbyView.updateLastRollLabel(event.getTotalValue());
  }


  /**
   * Handles tile action events.
   *
   * @param event the tile action event
   */
  private void handleTileAction(TileActionEvent event) {
    TileAction action = event.getTileAction();
    Player player = event.getPlayer();

    if (action instanceof LadderAction ladderAction) {
      Tile startTile = event.getTile();
      Tile destinationTile = ladderAction.getDestinationTile();

      if (startTile != destinationTile) {
        logger.info("Triggering ladder animation");
        gameLobbyView.getGameBoard().animateLadderMovement(player, startTile, destinationTile);
      } else {
        logger.warning("Invalid tiles for ladder animation");
      }
    }

    if (action instanceof FreezeAction) {
      Application.getAudioManager().playAudio(GameSoundEffects.FREEZE.getName());
    }

    if (action instanceof ImmunityAction) {
      Application.getAudioManager().playAudio(GameSoundEffects.IMMUNITY.getName());
    }
  }

  /**
   * Handles question asked events.
   *
   * @param event the question asked event
   */
  private void handleQuestionAsked(QuestionAskedEvent event) {
    logger.info("Question asked: " + event.getQuestion());
    gameLobbyView.showQuestion(event.getQuestion());
    gameLobbyView.setRollDiceButtonDisabled(true);
  }

  /**
   * Handles game ended events.
   *
   * @param event the game ended event
   */
  private void handleGameEnded(GameEndedEvent event) {
    logger.info("Game ended. Winner: " + event.getWinner().getName());
    gameLobbyView.setRollDiceButtonDisabled(true);
    gameLobbyView.showWinnerPopup(event.getWinner());
  }
}
