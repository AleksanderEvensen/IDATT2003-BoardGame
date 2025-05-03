package edu.ntnu.idi.idatt.boardgame.ui.javafx.view;

import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.game.GameController;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.router.NavigationContext;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.AnimationQueue;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.DieComponentAnimator;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Button;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Card;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.DieComponent;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Header;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.PlayerBlipView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.enums.Size;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.enums.Weight;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.controllers.GameLobbyController;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class GameLobbyView implements IView {

  private Game game;
  private GameController gameController;
  private List<Player> players;
  private List<DieComponent> diceComponents;

  /// labels
  private Label currentPlayerLabel;
  private Label currentRound;
  private Label lastRollLabel;

  /// button
  @Getter
  private Button rollButton;

  @Getter
  private AnimationQueue animationQueue;
  @Getter
  private GameBoard gameBoard;

  private GameLobbyController gameLobbyController;

  @Override
  public void load(NavigationContext<?> ctx) {
    String gameId = ctx.getParamOrThrow("gameId");
    this.game = Application.getGameManager().getGame(gameId);
    this.players = Application.getPlayerManager().getPlayers();
    this.gameController = new GameController(game);
  }

  @Override
  public void unload() {
    gameLobbyController = null;
    gameController = null;
    game = null;
    players = null;
  }

  @Override
  public Parent createRoot() {
    BorderPane root = new BorderPane();
    root.getStyleClass().add("view-root");
    root.setPadding(new Insets(20));

    gameLobbyController = new GameLobbyController(this, gameController);
    animationQueue = new AnimationQueue();


    GameBoard gameBoard = createGameBoard();
    gameBoard.setPadding(new Insets(0, 10, 0, 10));
    gameBoard.setAlignment(Pos.TOP_CENTER);
    root.setCenter(gameBoard);

    VBox playersCard = createLeftSection();
    playersCard.setPrefWidth(300);
    root.setLeft(playersCard);

    VBox controlPanel = createControlPanel();
    root.setRight(controlPanel);

    gameController.startGame(players);



    return root;
  }

  private GameBoard createGameBoard() {
    GameBoard gameBoard = new GameBoard.Builder(gameController, animationQueue)
        .addTiles()
        .resolveActionStyles()
        .addPlayers(players)
        .build();

    this.gameBoard = gameBoard;
    return gameBoard;
  }

  private VBox createLeftSection() {

    VBox leftSection = new VBox(10);

    VBox playersList = new VBox(10);
    playersList.setPadding(new Insets(10));

    Card playersCard = new Card();

    playersCard.setPrefWidth(300);
    playersCard.setPadding(new Insets(15));

    Header playersHeader = new Header("Players:");
    playersHeader.withType(Header.HeaderType.H4).withFontWeight(Weight.SEMIBOLD);
    playersCard.setTop(playersHeader);

    createPlayerList(playersList);
    playersCard.setCenter(playersList);

    Button backToMenuButton = new Button("Back to Menu");
    backToMenuButton.setOnAction(e -> {
      gameLobbyController.exitGame();

    });
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    leftSection.getChildren().addAll(playersCard, spacer, backToMenuButton);

    return leftSection;
  }

  private VBox createControlPanel() {

    /// Dice Control
    VBox controlPanel = new VBox(10);
    controlPanel.setPrefSize(300, 0);

    Header diceHeader = new Header("Dice Control");
    diceHeader.withType(HeaderType.H4).withFontWeight(Weight.SEMIBOLD);

    VBox diceControlPanel = new VBox(10);
    diceControlPanel.setPadding(new Insets(10));
    diceControlPanel.setAlignment(Pos.CENTER);

    HBox diceContainer = new HBox(10);
    diceContainer.setAlignment(Pos.CENTER);

    diceComponents = new ArrayList<>();
    IntStream.range(0, game.getNumberOfDice()).forEach(i -> {
        DieComponent dieComponent = new DieComponent();
        dieComponent.setValue(1);
        diceComponents.add(dieComponent);
    });

    diceContainer.getChildren().addAll(diceComponents);
    diceControlPanel.getChildren().add(diceContainer);


    /// last roll
    HBox lastRollContainer = new HBox(10);
    lastRollContainer.setAlignment(Pos.CENTER_LEFT);

    Label lastRollPlaceholder = new Label("Last roll:");
    lastRollPlaceholder.setStyle("-fx-font-weight: bold;");
    HBox.setHgrow(lastRollPlaceholder, Priority.ALWAYS);

    lastRollLabel = new Label();
    lastRollLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

    lastRollContainer.getChildren().addAll(lastRollPlaceholder, lastRollLabel);
    diceControlPanel.getChildren().add(lastRollContainer);

    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> {
        if (gameController.isGameStarted() && !gameController.isGameEnded()) {
            gameController.rollDiceAndMoveCurrentPlayer();
        }
    });

    diceControlPanel.getChildren().add(rollButton);

    Card diceCard = new Card();
    diceCard.setPadding(new Insets(15));
    diceCard.setTop(diceHeader);
    diceCard.setCenter(diceControlPanel);

    controlPanel.getChildren().add(diceCard);

    /// Current Game Info
    Header gameInfoHeader = new Header("Game Info");
    gameInfoHeader.withType(HeaderType.H4).withFontWeight(Weight.SEMIBOLD);
    VBox gameInfoPanel = new VBox(10);
    Card gameInfoCard = new Card();
    gameInfoCard.setPadding(new Insets(15));
    gameInfoCard.setTop(gameInfoHeader);
    gameInfoCard.setCenter(gameInfoPanel);

    VBox gameInfoContainer = new VBox(5);
    gameInfoContainer.setPadding(new Insets(10));
    gameInfoCard.setCenter(gameInfoContainer);

    /// Current Player Label
    HBox currentPlayerContainer = new HBox(10);
    currentPlayerContainer.setAlignment(Pos.CENTER_LEFT);

    Label currentPlayerPlaceholder = new Label("Current Player:");
    currentPlayerPlaceholder.setStyle("-fx-font-weight: bold;");
    HBox.setHgrow(currentPlayerPlaceholder, Priority.ALWAYS);

    currentPlayerLabel = new Label();
    currentPlayerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

    currentPlayerContainer.getChildren().addAll(currentPlayerPlaceholder, currentPlayerLabel);

    /// Current Round Label
    HBox currentRoundContainer = new HBox(10);
    currentRoundContainer.setAlignment(Pos.CENTER_LEFT);

    Label currentRoundPlaceholder = new Label("Current Round:");
    currentRoundPlaceholder.setStyle("-fx-font-weight: bold;");
    HBox.setHgrow(currentRoundPlaceholder, Priority.ALWAYS);

    currentRound = new Label("1");
    currentRound.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

    currentRoundContainer.getChildren().addAll(currentRoundPlaceholder, currentRound);

    gameInfoContainer.getChildren().addAll(currentPlayerContainer, currentRoundContainer);

    controlPanel.getChildren().add(gameInfoCard);

    return controlPanel;
}

  private void createPlayerList(VBox playerListContainer) {
    playerListContainer.getChildren().clear();
    players.forEach(player -> {
      HBox playerEntry = new HBox(10);
      playerEntry.setAlignment(Pos.CENTER_LEFT);
      playerEntry.setMaxWidth(Double.MAX_VALUE);
      HBox.setHgrow(playerEntry, Priority.ALWAYS);

      Card playerCard = new Card(playerEntry).withRounded(Size.SM);
      playerCard.setPadding(new Insets(10));

      Label playerNameLabel = new Label(player.getName());
      playerNameLabel.setMaxWidth(Double.MAX_VALUE);
      playerNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

      Region spacer = new Region();
      HBox.setHgrow(spacer, Priority.ALWAYS);

      PlayerBlipView playerBlip = new PlayerBlipView(player);

      playerEntry.getChildren().addAll(playerNameLabel, spacer, playerBlip);
      playerListContainer.getChildren().add(playerCard);
    });
  }


  public void animateDice(List<Integer> values){
    IntStream.range(0, values.size()).forEach(i -> {
      DieComponent die = diceComponents.get(i);
      animationQueue.queue(DieComponentAnimator.animateRoll(die, values.get(i)), "Rolling die");
    });
  }

  public void updateCurrentPlayerLabel(Player player) {
    Timeline animation = new Timeline();
    animation.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1), e -> {
      Platform.runLater(() -> {
        currentPlayerLabel.setText(String.format("%s", player.getName()));
      });
    }));

    animationQueue.queue(animation, "Updating current player label");
  }

  public void updateCurrentRound(int round) {
    Timeline animation = new Timeline();
    animation.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1), e -> {
      Platform.runLater(() -> {
        currentRound.setText(String.format("%d", round));
      });
    }));
    animationQueue.queue(animation, "Updating current round label");
  }

  public void setRollDiceButtonDisabled(boolean disabled) {
   Timeline animation = new Timeline();
    animation.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1), e -> {
        Platform.runLater(() -> {
          rollButton.setDisable(disabled);
        });
      }));
      animationQueue.queue(animation, "Toggling roll dice button deactivation");
  }

  public void updateLastRollLabel(int value) {
    Timeline animation = new Timeline();
    animation.getKeyFrames().add(new KeyFrame(javafx.util.Duration.millis(1), e -> {
      Platform.runLater(() -> {
        lastRollLabel.setText(String.format("%d", value));
      });
    }));
    animationQueue.queue(animation, "Updating last roll label");
  }
}