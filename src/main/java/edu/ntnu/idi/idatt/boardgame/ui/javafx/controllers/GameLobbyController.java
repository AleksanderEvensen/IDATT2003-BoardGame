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
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.AnimationQueue;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.DieComponentAnimator;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.PlayerMovementAnimator;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.QueueableAction;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.audio.GameSoundEffects;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Button;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Card;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.DieComponent;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Header;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.PlayerBlipView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.QuestionDialog;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.enums.Weight;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.view.GameLobbyView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.animation.Animation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

  private final AnimationQueue animationQueue;


  /**
   * Creates a new GameLobbyController.
   *
   * @param gameLobbyView  the game view
   * @param gameController the game controller
   */
  public GameLobbyController(GameLobbyView gameLobbyView, GameController gameController) {
    this.gameLobbyView = gameLobbyView;
    this.gameController = gameController;
    this.animationQueue = new AnimationQueue();

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
    logger.info("Game started with " + event.getPlayers().size() + " players");
    animationQueue.stopAndClear();
    gameLobbyView.getCurrentPlayerProperty().setValue(gameController.getCurrentPlayer());
    gameLobbyView.getCurrentRoundProperty().setValue(gameController.getRoundCount());
  }


  /**
   * Handles player turn change events.
   *
   * @param event the player turn change event
   */
  private void handlePlayerTurnChangeEvent(PlayerTurnChangedEvent event) {
    QueueableAction action = QueueableAction.builder().action(() -> {
      logger.info("Player turn changed to " + event.getCurrentPlayer().getName());
      gameLobbyView.getCurrentPlayerProperty().setValue(event.getCurrentPlayer());
      gameLobbyView.getCurrentRoundProperty().setValue(gameController.getRoundCount());
      gameLobbyView.getRollButtonDisabledProperty().setValue(false);
      this.players.setAll(gameController.getPlayers());
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
        "Player " + event.getPlayer().getName() + " moved from tile " + (event.getFromTile() != null
            ? event.getFromTile().getTileId() : "null") + " to tile " + event.getToTile()
            .getTileId());
    animatePlayerMovement(event.getPlayer(), event.getFromTile(), event.getToTile());
    QueueableAction action = QueueableAction.builder().action(() -> {
      gameLobbyView.getRollButtonDisabledProperty().setValue(false);
    }).build();
    animationQueue.queue(action.timeline(), "Enabling roll button", 0);
  }

  private void handleDiceRolled(DiceRolledEvent event) {
    gameLobbyView.getRollButtonDisabledProperty().setValue(true);
    List<Animation> diceAnimations = new ArrayList<>();
    IntStream.range(0, event.getIndividualRolls().size()).forEach(i -> {
      DieComponent die = gameLobbyView.getDiceComponents().get(i);
      diceAnimations.add(DieComponentAnimator.animateRoll(die, event.getIndividualRolls().get(i)));
    });

    animationQueue.queue(AnimationQueue.combineAnimationsParallel(diceAnimations), "Rolling dice",
        0);

    QueueableAction action = QueueableAction.builder().action(() -> {
      gameLobbyView.getLastRollProperty().setValue(event.getTotalValue());
    }).build();

    animationQueue.queue(action.timeline(), "Updating last roll", 0);
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
        animateLadderMovement(player, startTile, destinationTile);
      } else {
        logger.warning("Invalid tiles for ladder animation");
      }
    }

    if (action instanceof FreezeAction) {
      QueueableAction freezeAudioAction = QueueableAction.builder().action(() -> {
        Application.getAudioManager().playAudio(GameSoundEffects.FREEZE.getName());
      }).build();
      animationQueue.queue(freezeAudioAction.timeline(), "Freeze action", 0);

    }

    if (action instanceof ImmunityAction) {
      QueueableAction immunityAudioAction = QueueableAction.builder().action(() -> {
        Application.getAudioManager().playAudio(GameSoundEffects.IMMUNITY.getName());
      }).build();
      animationQueue.queue(immunityAudioAction.timeline(), "Immunity action", 0);
    }
  }

  /**
   * Creates the game board.
   */
  public GameBoard createGameBoard() {
    return new GameBoard.Builder(gameController).addTiles().resolveActionStyles()
        .addPlayers(gameController.getPlayers()).build();
  }

  /**
   * Handles question asked events.
   *
   * @param event the question asked event
   */
  private void handleQuestionAsked(QuestionAskedEvent event) {
    logger.info("Question asked: " + event.getQuestion());
    QueueableAction action = QueueableAction.builder().action(() -> {
      QuestionDialog dialog =
          new QuestionDialog(gameLobbyView.getRoot(), event.getQuestion(), (answer) -> {
            boolean isCorrect =
                gameController.answerQuestion(event.getQuestion().getAnswers().get(answer));
            if (isCorrect) {
              Application.getAudioManager().playAudio(GameSoundEffects.CORRECT_ANSWER.getName());
            } else {
              Application.getAudioManager().playAudio(GameSoundEffects.INCORRECT_ANSWER.getName());
            }
          });
      dialog.show();
    }).build();
    animationQueue.queue(action.timeline(), "Question asked", 0);
    gameLobbyView.getRollButtonDisabledProperty().setValue(true);
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
        Application.getAudioManager().playAudio(GameSoundEffects.LADDER_CLIMB.getName());
      } else {
        Application.getAudioManager().playAudio(GameSoundEffects.LADDER_FALL.getName());
      }
    }).build();

    String description =
        "Player " + player.getName() + " ladder from tile " + fromTile.getTileId() + " to tile "
            + toTile.getTileId();

    animationQueue.queue(
        AnimationQueue.combineAnimationsParallel(List.of(animation, audioEffectAction.timeline())),
        description, 500);

    logger.fine("Queued ladder animation: " + description);
  }

  /**
   * Handles game ended events.
   *
   * @param event the game ended event
   */
  private void handleGameEnded(GameEndedEvent event) {
    logger.info("Game ended. Winner: " + event.getWinner().getName());
    gameLobbyView.getRollButtonDisabledProperty().setValue(true);
    QueueableAction action = QueueableAction.builder().action(() -> {

      Application.getAudioManager().playAudio("victory");

      StackPane overlay = new StackPane();
      overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
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

      Label winnerLabel = new Label(event.getWinner().getName() + " has won the game!");
      winnerLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

      Button returnButton = new Button("Return to Main Menu");
      returnButton.setOnAction(evt -> {
        exitGame();
      });

      content.getChildren().addAll(header, winnerLabel, returnButton);
      winnerCard.setCenter(content);

      StackPane.setAlignment(winnerCard, Pos.CENTER);
      overlay.getChildren().add(winnerCard);

      gameLobbyView.getRoot().getChildren().add(overlay);
    }).build();
    animationQueue.queue(action.timeline(), "Game ended", 0);
  }
}
