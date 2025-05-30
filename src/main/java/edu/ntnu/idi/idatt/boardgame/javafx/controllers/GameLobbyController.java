package edu.ntnu.idi.idatt.boardgame.javafx.controllers;

import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observer;
import edu.ntnu.idi.idatt.boardgame.javafx.animation.AnimationQueue;
import edu.ntnu.idi.idatt.boardgame.javafx.animation.DieComponentAnimator;
import edu.ntnu.idi.idatt.boardgame.javafx.animation.PlayerMovementAnimator;
import edu.ntnu.idi.idatt.boardgame.javafx.animation.QueueableAction;
import edu.ntnu.idi.idatt.boardgame.javafx.audio.AudioManager;
import edu.ntnu.idi.idatt.boardgame.javafx.audio.GameSoundEffects;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Button;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Card;
import edu.ntnu.idi.idatt.boardgame.javafx.components.DieComponent;
import edu.ntnu.idi.idatt.boardgame.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Header;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.javafx.components.PlayerBlipView;
import edu.ntnu.idi.idatt.boardgame.javafx.components.QuestionDialog;
import edu.ntnu.idi.idatt.boardgame.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.ToastStyle;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.Weight;
import edu.ntnu.idi.idatt.boardgame.javafx.providers.ToastProvider;
import edu.ntnu.idi.idatt.boardgame.javafx.view.GameLobbyView;
import edu.ntnu.idi.idatt.boardgame.model.GameEngine;
import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.freeze.FreezeAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.immunity.ImmunityAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import edu.ntnu.idi.idatt.boardgame.model.events.DiceRolledEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.GameEndedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.GameEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.GameStartedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.PlayerMovedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.PlayerSkippedTurnEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.PlayerTurnChangedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.QuestionAskedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.TileActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.animation.Animation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.Getter;

/**
 * Controller that connects the GameEngine with the GameBoard UI component.
 * <p>
 * This class observes game events and updates the game board UI accordingly, particularly handling
 * player movement animations.
 * </p>
 *
 * @version v1.0.1
 * @since v3.0.0
 */
public class GameLobbyController implements Observer<GameEngine, GameEvent> {

  private static final Logger logger = Logger.getLogger(GameLobbyController.class.getName());

  private final GameLobbyView gameLobbyView;
  private final GameEngine gameEngine;

  @Getter
  private final ObservableList<Player> players;

  private final AnimationQueue animationQueue;

  @Getter
  private final ObjectProperty<Player> currentPlayerProperty = new SimpleObjectProperty<>();
  @Getter
  private final IntegerProperty currentRoundProperty = new SimpleIntegerProperty(1);
  @Getter
  private final IntegerProperty lastRollProperty = new SimpleIntegerProperty(0);
  @Getter
  private final BooleanProperty rollButtonDisabledProperty = new SimpleBooleanProperty(false);

  /**
   * Creates a new GameLobbyController.
   *
   * @param gameLobbyView the game view
   * @param gameEngine    the game controller
   */
  public GameLobbyController(GameLobbyView gameLobbyView, GameEngine gameEngine) {
    this.gameLobbyView = gameLobbyView;
    this.gameEngine = gameEngine;
    this.animationQueue = new AnimationQueue();

    this.players = FXCollections.observableArrayList(gameEngine.getPlayers());

    gameEngine.addListener(this);
    this.currentPlayerProperty.set(gameEngine.getCurrentPlayer());

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
      case PlayerSkippedTurnEvent playerSkippedTurnEvent ->
          handlePlayerSkippedTurn(playerSkippedTurnEvent);
      case QuestionAskedEvent questionAskedEvent -> handleQuestionAsked(questionAskedEvent);
      case GameEndedEvent gameEndedEvent -> handleGameEnded(gameEndedEvent);
      default -> logger.warning("Unhandled event type: " + event.getClass().getSimpleName());
    }
  }

  /**
   * Exits the game and stops all animations.
   */
  public void exitGame() {
    animationQueue.stopAndClear();
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
    logger.info("Game started with " + event.players().size() + " players");
    animationQueue.stopAndClear();
    currentPlayerProperty.set(gameEngine.getCurrentPlayer());
    currentRoundProperty.set(gameEngine.getRoundCount());
  }

  /**
   * Handles player turn change events.
   *
   * @param event the player turn change event
   */
  private void handlePlayerTurnChangeEvent(PlayerTurnChangedEvent event) {
    QueueableAction action = QueueableAction.builder().action(() -> {
      logger.info("Player turn changed to " + event.currentPlayer().getName());
      currentPlayerProperty.set(event.currentPlayer());
      currentRoundProperty.set(gameEngine.getRoundCount());
      rollButtonDisabledProperty.set(false);
      this.players.setAll(gameEngine.getPlayers());
    }).build();

    animationQueue.queue(action.timeline(), "Player turn changed", 0);
  }

  /**
   * Handles player moved events.
   *
   * @param event the player moved event
   */
  private void handlePlayerMoved(PlayerMovedEvent event) {
    logger.info(
        "Player " + event.player().getName() + " moved from tile " + (event.fromTile() != null
            ? event.fromTile().getTileId() : "null") + " to tile " + event.toTile().getTileId());
    animatePlayerMovement(event.player(), event.fromTile(), event.toTile());
    QueueableAction action =
        QueueableAction.builder().action(() -> rollButtonDisabledProperty.set(false)).build();
    animationQueue.queue(action.timeline(), "Enabling roll button", 0);
  }

  private void handleDiceRolled(DiceRolledEvent event) {
    rollButtonDisabledProperty.set(true);
    Animation[] diceAnimations =
        IntStream.range(0, event.individualRolls().size()).boxed().map(diceId -> {
          DieComponent die = gameLobbyView.getDiceComponents().get(diceId);
          return DieComponentAnimator.animateRoll(die, event.getDieValue(diceId));
        }).toArray(Animation[]::new);

    animationQueue.queue(AnimationQueue.combineAnimationsParallel(diceAnimations), "Rolling dice",
        0);

    QueueableAction action =
        QueueableAction.builder().action(() -> lastRollProperty.set(event.totalValue())).build();

    animationQueue.queue(action.timeline(), "Updating last roll", 0);
  }

  /**
   * Handles tile action events.
   *
   * @param event the tile action event
   */
  private void handleTileAction(TileActionEvent event) {
    TileAction action = event.tileAction();
    Player player = event.player();

    if (action instanceof LadderAction ladderAction) {
      Tile startTile = event.tile();
      Tile destinationTile = ladderAction.getDestinationTile();

      if (startTile != destinationTile) {
        logger.info("Triggering ladder animation");
        animateLadderMovement(player, startTile, destinationTile);
      } else {
        logger.warning("Invalid exceptions for ladder animation");
      }
    }

    if (action instanceof FreezeAction) {
      QueueableAction freezeAudioAction = QueueableAction.builder().action(() -> {
        AudioManager.playAudio(GameSoundEffects.FREEZE);
        ToastProvider.show("You are frozen for 1 turn");
      }).build();
      animationQueue.queue(freezeAudioAction.timeline(), "Freeze action", 0);

    }

    if (action instanceof ImmunityAction) {
      QueueableAction immunityAudioAction = QueueableAction.builder().action(() -> {
        AudioManager.playAudio(GameSoundEffects.IMMUNITY);
        ToastProvider.show(
            "You are immune against negative actions - total: " + event.player().getImmunityTurns()
                + " turns");
      }).build();
      animationQueue.queue(immunityAudioAction.timeline(), "Immunity action", 0);
    }
  }

  /**
   * Creates the game board.
   */
  public GameBoard createGameBoard() {
    return new GameBoard.Builder(gameEngine).addTiles().resolveActionStyles()
        .addPlayers(gameEngine.getPlayers()).build();
  }

  /**
   * Handles question asked events.
   *
   * @param event the question asked event
   */
  private void handleQuestionAsked(QuestionAskedEvent event) {
    logger.info("Question asked: " + event.question());
    QueueableAction action = QueueableAction.builder().action(() -> {
      QuestionDialog dialog =
          new QuestionDialog(gameLobbyView.getRoot(), event.question(), (answer) -> {
            boolean isCorrect =
                gameEngine.answerQuestion(event.question().getAnswers().get(answer));
            if (isCorrect) {
              AudioManager.playAudio(GameSoundEffects.CORRECT_ANSWER);
              ToastProvider.show("Correct! you stay on the current tile", Duration.seconds(5),
                  ToastStyle.SUCCESS);
            } else {
              AudioManager.playAudio(GameSoundEffects.INCORRECT_ANSWER);
              ToastProvider.show("Wrong! go back to your previous tile", Duration.seconds(5),
                  ToastStyle.ERROR);
            }
          });
      dialog.show();
    }).build();
    animationQueue.queue(action.timeline(), "Question asked", 0);
    rollButtonDisabledProperty.set(true);
  }

  /**
   * Handles player skipped turn events.
   *
   * @param event the player skipped turn event
   */
  public void handlePlayerSkippedTurn(PlayerSkippedTurnEvent event) {
    logger.info("Player " + event.player().getName() + " skipped turn: " + event.reason());
    QueueableAction action = QueueableAction.builder().action(() -> ToastProvider.show(
        String.format("Player %s skipped turn, reason: %s", event.player().getName(),
            event.reason()), Duration.seconds(5), ToastStyle.INFO)).build();
    animationQueue.queue(action.timeline(), "Player skipped turn", 0);
  }

  /**
   * Animates a player's movement from one tile to another.
   *
   * @param player   The player to animate.
   * @param fromTile The tile to animate from.
   * @param toTile   The tile to animate to.
   */
  public void animatePlayerMovement(Player player, Tile fromTile, Tile toTile) {
    PlayerBlipView blipView = gameLobbyView.getGameBoard().getPlayerBlipView(player);
    if (blipView == null) {
      logger.warning("Attempted to animate movement of unregistered player: " + player.getName());
      return;
    }

    TileComponent toTileComponent =
        gameLobbyView.getGameBoard().getTileComponent(toTile.getTileId());
    if (toTileComponent == null) {
      logger.warning("No tile component found for destination tile ID: " + toTile.getTileId());
      return;
    }

    var animation =
        PlayerMovementAnimator.createPathAnimation(gameLobbyView.getGameBoard(), player, fromTile,
            toTile);

    String description = "Player " + player.getName() + " moving from tile " + (fromTile != null
        ? fromTile.getTileId() : "null") + " to tile " + toTile.getTileId();

    animationQueue.queue(animation, description, 500);
    logger.fine("Queued animation: " + description);
  }

  /**
   * Animates a player using a ladder from one tile to another.
   *
   * @param player   The player to animate.
   * @param fromTile The ladder start tile.
   * @param toTile   The ladder end tile.
   */
  public void animateLadderMovement(Player player, Tile fromTile, Tile toTile) {
    if (fromTile == null || toTile == null) {
      animatePlayerMovement(player, fromTile, toTile);
      return;
    }

    var animation =
        PlayerMovementAnimator.createLadderAnimation(gameLobbyView.getGameBoard(), player, fromTile,
            toTile);

    boolean isPositiveLadder = toTile.getTileId() > fromTile.getTileId();

    QueueableAction audioEffectAction = QueueableAction.builder().action(() -> {
      if (isPositiveLadder) {
        AudioManager.playAudio(GameSoundEffects.LADDER_CLIMB);
      } else {
        AudioManager.playAudio(GameSoundEffects.LADDER_FALL);
      }
    }).build();

    String description =
        "Player " + player.getName() + " ladder from tile " + fromTile.getTileId() + " to tile "
            + toTile.getTileId();

    animationQueue.queue(
        AnimationQueue.combineAnimationsParallel(animation, audioEffectAction.timeline()),
        description, 500);

    logger.fine("Queued ladder animation: " + description);
  }

  /**
   * Handles game ended events.
   *
   * @param event the game ended event
   */
  private void handleGameEnded(GameEndedEvent event) {
    logger.info("Game ended. Winner: " + event.winner().getName());
    rollButtonDisabledProperty.set(true);
    QueueableAction action = QueueableAction.builder().action(() -> {

      AudioManager.playAudio(GameSoundEffects.VICTORY);

      StackPane overlay = new StackPane();
      overlay.getStyleClass().add("overlay");
      overlay.setPrefSize(gameLobbyView.getRoot().getWidth(), gameLobbyView.getRoot().getHeight());

      Card winnerCard = new Card();
      winnerCard.setPrefWidth(400);
      winnerCard.setPrefHeight(300);
      winnerCard.setPadding(new Insets(20));
      winnerCard.setMaxWidth(Region.USE_PREF_SIZE);
      winnerCard.setMaxHeight(Region.USE_PREF_SIZE);

      VBox content = new VBox(20);
      content.setAlignment(Pos.CENTER);

      Header header = new Header("Game Over");
      header.withType(HeaderType.H2).withFontWeight(Weight.BOLD);

      Label winnerLabel = new Label(event.winner().getName() + " has won the game!");
      winnerLabel.getStyleClass().add("winner-label");

      Button returnButton = new Button("Return to Main Menu");
      returnButton.setOnAction(evt -> exitGame());

      content.getChildren().addAll(header, winnerLabel, returnButton);
      winnerCard.setCenter(content);

      StackPane.setAlignment(winnerCard, Pos.CENTER);
      overlay.getChildren().add(winnerCard);

      gameLobbyView.getRoot().getChildren().add(overlay);
    }).build();
    animationQueue.queue(action.timeline(), "Game ended", 0);
  }
}
