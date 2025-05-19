package edu.ntnu.idi.idatt.boardgame.javafx.view;

import edu.ntnu.idi.idatt.boardgame.game.GameController;
import edu.ntnu.idi.idatt.boardgame.game.GameManager;
import edu.ntnu.idi.idatt.boardgame.game.PlayerManager;
import edu.ntnu.idi.idatt.boardgame.game.QuizManager;
import edu.ntnu.idi.idatt.boardgame.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.javafx.animation.AnimationQueue;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Button;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Card;
import edu.ntnu.idi.idatt.boardgame.javafx.components.DieComponent;
import edu.ntnu.idi.idatt.boardgame.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Header;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.javafx.components.PlayerBlipView;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.Size;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.Weight;
import edu.ntnu.idi.idatt.boardgame.javafx.controllers.GameLobbyController;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.router.NavigationContext;
import edu.ntnu.idi.idatt.boardgame.style.FreezeStyle;
import edu.ntnu.idi.idatt.boardgame.style.ImmunityStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
    Game game = GameManager.getInstance().getGame(gameId);
    this.gameController = new GameController(game, QuizManager.getInstance());
    gameController.startGame(PlayerManager.getInstance().getPlayers());
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

    return root;
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

      this.gameLobbyController.getCurrentPlayerProperty()
          .addListener((obs, oldPlayer, newPlayer) -> {
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

    Header lastRollPlaceholder = new Header("Last roll:").withFontWeight(Weight.BOLD);
    HBox.setHgrow(lastRollPlaceholder, Priority.ALWAYS);

    Header lastRollLabel = new Header("").withFontSize(14);

    lastRollLabel.textProperty().bind(this.gameLobbyController.getLastRollProperty().asString());

    lastRollContainer.getChildren().addAll(lastRollPlaceholder, lastRollLabel);
    diceControlPanel.getChildren().add(lastRollContainer);

    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> {
      if (gameController.isGameStarted() && !gameController.isGameEnded()) {
        gameController.rollDiceAndMoveCurrentPlayer();
      }
    });

    rollButton.disableProperty().bind(this.gameLobbyController.getRollButtonDisabledProperty());

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

    Header currentPlayerPlaceholder = new Header("Current Player:").withFontWeight(Weight.BOLD);
    HBox.setHgrow(currentPlayerPlaceholder, Priority.ALWAYS);

    Header currentPlayerLabel = new Header("").withFontSize(14);

    this.gameLobbyController.getCurrentPlayerProperty().addListener((obs, oldPlayer, newPlayer) -> {
      if (newPlayer != null) {
        currentPlayerLabel.setText(newPlayer.getName());
      }
    });

    currentPlayerContainer.getChildren().addAll(currentPlayerPlaceholder, currentPlayerLabel);

    HBox currentRoundContainer = new HBox(10);
    currentRoundContainer.setAlignment(Pos.CENTER_LEFT);

    Header currentRoundPlaceholder = new Header("Current Round:").withFontWeight(Weight.BOLD);
    HBox.setHgrow(currentRoundPlaceholder, Priority.ALWAYS);

    Header currentRoundLabel = new Header("").withFontSize(14);

    currentRoundLabel.textProperty()
        .bind(this.gameLobbyController.getCurrentRoundProperty().asString());

    currentRoundContainer.getChildren().addAll(currentRoundPlaceholder, currentRoundLabel);

    gameInfoContainer.getChildren().addAll(currentPlayerContainer, currentRoundContainer);

    Header description = new Header(gameController.getGame().getDescription())
        .withFontSize(16);
    description.setWrapText(true);
    description.setMaxWidth(Double.MAX_VALUE);
    var descriptionContainer = new VBox(
        10,
        new Header("Description").withType(HeaderType.H4).withFontWeight(Weight.SEMIBOLD),
        description
    );
    descriptionContainer.setPadding(new Insets(10, 0, 0, 0));
    gameInfoCard.setBottom(descriptionContainer);

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

    Header playerNameLabel = new Header(player.getName()).withFontSize(14);
    playerNameLabel.setMaxWidth(Double.MAX_VALUE);

    if (this.gameLobbyController.getCurrentPlayerProperty().get().equals(player)) {
      playerNameLabel.withFontWeight(Weight.BOLD);
    } else {
      playerNameLabel.withFontWeight(Weight.NORMAL);
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