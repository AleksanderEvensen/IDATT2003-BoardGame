package edu.ntnu.idi.idatt.boardgame.ui.javafx.view;

import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.game.GameController;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.router.NavigationContext;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.animation.AnimationQueue;
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
import edu.ntnu.idi.idatt.boardgame.ui.style.FreezeStyle;
import edu.ntnu.idi.idatt.boardgame.ui.style.ImmunityStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import lombok.Getter;
import org.kordamp.ikonli.javafx.FontIcon;

public class GameLobbyView implements IView {

  // JavaFX properties
  @Getter
  private final ObjectProperty<Player> currentPlayerProperty = new SimpleObjectProperty<>();
  @Getter
  private final IntegerProperty currentRoundProperty = new SimpleIntegerProperty(1);
  @Getter
  private final IntegerProperty lastRollProperty = new SimpleIntegerProperty(0);
  @Getter
  private final BooleanProperty rollButtonDisabledProperty = new SimpleBooleanProperty(false);
  @Getter
  private StackPane root;
  private GameController gameController;
  @Getter
  private List<DieComponent> diceComponents;
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
    Game game = Application.getGameManager().getGame(gameId);
    this.gameController = new GameController(game, Application.getQuizManager());
    gameController.startGame(Application.getPlayerManager().getPlayers());
    gameLobbyController = new GameLobbyController(this, gameController);
    animationQueue = new AnimationQueue();
    gameBoard = gameLobbyController.createGameBoard();
  }

  @Override
  public void unload() {
    gameLobbyController = null;
    gameController = null;
    gameBoard = null;
  }

  @Override
  public Parent createRoot() {
    BorderPane content = new BorderPane();
    content.setPadding(new Insets(20));

    gameBoard.setPadding(new Insets(0, 10, 0, 10));
    gameBoard.setAlignment(Pos.TOP_CENTER);
    content.setCenter(gameBoard);

    VBox playersCard = createLeftSection();
    playersCard.setPrefWidth(300);
    content.setLeft(playersCard);

    VBox controlPanel = createControlPanel();
    content.setRight(controlPanel);

    this.root = new StackPane(content);
    this.root.getStyleClass().add("view-root");

    initializeProperties();

    return root;
  }

  private void initializeProperties() {
    currentPlayerProperty.set(gameController.getCurrentPlayer());
  }

  private VBox createLeftSection() {
    VBox leftSection = new VBox(10);
    ListView<Player> playersList = getPlayerListView();
    playersList.getStyleClass().clear();

    Card playersCard = new Card();
    playersCard.setPrefWidth(300);
    playersCard.setPadding(new Insets(15));

    Header playersHeader = new Header("Players:");
    playersHeader.withType(Header.HeaderType.H4).withFontWeight(Weight.SEMIBOLD);
    playersCard.setTop(playersHeader);

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

  private ListView<Player> getPlayerListView() {
    ListView<Player> playersList = new ListView<>(gameLobbyController.getPlayers());
    playersList.setPadding(new Insets(10, 0, 0, 0));
    playersList.setCellFactory(list -> {
      var cell = new ListCell<Player>() {
        @Override
        protected void updateItem(Player player, boolean empty) {
          super.updateItem(player, empty);
          setText(null);
          if (empty || player == null) {
            setGraphic(null);
          } else {
            setGraphic(createPlayerListEntry(player));
          }
        }
      };
      cell.setPadding(new Insets(0, 0, 10, 0));
      cell.getStyleClass().clear();

      currentPlayerProperty.addListener((obs, oldPlayer, newPlayer) -> {
        cell.updateItem(cell.getItem(), cell.isEmpty());
      });

      return cell;
    });
    return playersList;
  }

  private VBox createControlPanel() {
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
    IntStream.range(0, gameController.getGame().getNumberOfDice()).forEach(i -> {
      DieComponent dieComponent = new DieComponent();
      dieComponent.setValue(1);
      diceComponents.add(dieComponent);
    });

    diceContainer.getChildren().addAll(diceComponents);
    diceControlPanel.getChildren().add(diceContainer);

    HBox lastRollContainer = new HBox(10);
    lastRollContainer.setAlignment(Pos.CENTER_LEFT);

    Label lastRollPlaceholder = new Label("Last roll:");
    lastRollPlaceholder.setStyle("-fx-font-weight: bold;");
    HBox.setHgrow(lastRollPlaceholder, Priority.ALWAYS);

    Label lastRollLabel = new Label();
    lastRollLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

    lastRollLabel.textProperty().bind(lastRollProperty.asString());

    lastRollContainer.getChildren().addAll(lastRollPlaceholder, lastRollLabel);
    diceControlPanel.getChildren().add(lastRollContainer);

    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> {
      if (gameController.isGameStarted() && !gameController.isGameEnded()) {
        gameController.rollDiceAndMoveCurrentPlayer();
      }
    });

    rollButton.disableProperty().bind(rollButtonDisabledProperty);

    diceControlPanel.getChildren().add(rollButton);

    Card diceCard = new Card();
    diceCard.setPadding(new Insets(15));
    diceCard.setTop(diceHeader);
    diceCard.setCenter(diceControlPanel);

    controlPanel.getChildren().add(diceCard);

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

    HBox currentPlayerContainer = new HBox(10);
    currentPlayerContainer.setAlignment(Pos.CENTER_LEFT);

    Label currentPlayerPlaceholder = new Label("Current Player:");
    currentPlayerPlaceholder.setStyle("-fx-font-weight: bold;");
    HBox.setHgrow(currentPlayerPlaceholder, Priority.ALWAYS);

    Label currentPlayerLabel = new Label();
    currentPlayerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

    currentPlayerProperty.addListener((obs, oldPlayer, newPlayer) -> {
      if (newPlayer != null) {
        currentPlayerLabel.setText(newPlayer.getName());
      }
    });

    currentPlayerContainer.getChildren().addAll(currentPlayerPlaceholder, currentPlayerLabel);

    HBox currentRoundContainer = new HBox(10);
    currentRoundContainer.setAlignment(Pos.CENTER_LEFT);

    Label currentRoundPlaceholder = new Label("Current Round:");
    currentRoundPlaceholder.setStyle("-fx-font-weight: bold;");
    HBox.setHgrow(currentRoundPlaceholder, Priority.ALWAYS);

    Label currentRoundLabel = new Label();
    currentRoundLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

    currentRoundLabel.textProperty().bind(currentRoundProperty.asString());

    currentRoundContainer.getChildren().addAll(currentRoundPlaceholder, currentRoundLabel);

    gameInfoContainer.getChildren().addAll(currentPlayerContainer, currentRoundContainer);

    controlPanel.getChildren().add(gameInfoCard);

    return controlPanel;
  }

  private Card createPlayerListEntry(Player player) {
    HBox playerEntry = new HBox(10);
    playerEntry.setAlignment(Pos.CENTER_LEFT);
    playerEntry.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(playerEntry, Priority.ALWAYS);

    Card playerCard = new Card(playerEntry).withRounded(Size.SM);
    playerCard.setPadding(new Insets(10));

    Label playerNameLabel = new Label(player.getName());
    playerNameLabel.setMaxWidth(Double.MAX_VALUE);
    playerNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

    if (currentPlayerProperty.get() == player) {
      playerNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
    }

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    PlayerBlipView playerBlip = new PlayerBlipView(player);

    playerEntry.getChildren().addAll(playerNameLabel, spacer);

    if (player.isImmune()) {
      FontIcon immunityIcon = new FontIcon("fas-shield-alt");
      immunityIcon.setIconSize(20);
      immunityIcon.setIconColor(Paint.valueOf(ImmunityStyle.BACKGROUND_COLOR));
      playerEntry.getChildren().add(immunityIcon);
    }

    if (player.isFrozen()) {
      FontIcon frozenIcon = new FontIcon("fas-snowflake");
      frozenIcon.setIconSize(20);
      frozenIcon.setIconColor(Paint.valueOf(FreezeStyle.BACKGROUND_COLOR));
      playerEntry.getChildren().add(frozenIcon);
    }

    playerEntry.getChildren().add(playerBlip);
    return playerCard;
  }

}