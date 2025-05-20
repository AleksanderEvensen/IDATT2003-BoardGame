package edu.ntnu.idi.idatt.boardgame.javafx.view;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Button;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Button.ButtonVariant;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Card;
import edu.ntnu.idi.idatt.boardgame.javafx.components.GameCard;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Header;
import edu.ntnu.idi.idatt.boardgame.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.Size;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.Weight;
import edu.ntnu.idi.idatt.boardgame.javafx.controllers.MainMenuController;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;

/**
 * The main menu view of the application. This view displays available games and player management.
 */
public class MainMenuView implements IView {

  private static final Logger logger = Logger.getLogger(MainMenuView.class.getName());

  private final MainMenuController controller;
  private ChangeListener<Pair<String, String>> errorDialogListener;
  private MapChangeListener<String, Game> gamesListener;

  public MainMenuView() {
    this.controller = new MainMenuController();
  }

  @Override
  public void load() {
    this.controller.initialize();
  }

  @Override
  public void unload() {
    if (errorDialogListener != null) {
      controller.getErrorDialog().removeListener(errorDialogListener);
    }
    controller.getErrorDialog().set(null);

    if (gamesListener != null) {
      controller.getGames().removeListener(gamesListener);
    }
  }

  @Override
  public StackPane createRoot() {
    // Create a StackPane as the root container to host both the main content and any dialogs
    StackPane root = new StackPane();
    root.getStyleClass().add("view-root");

    // Main layout
    VBox mainContent = new VBox(20);
    mainContent.setPadding(new Insets(20));

    // Header
    Header titleHeader = new Header("Awesome Board Games");
    titleHeader.withType(HeaderType.H1).withFontWeight(Weight.BOLD);

    Header subHeader = new Header("Add players and select a game to play!");
    subHeader.withType(HeaderType.H5).withFontWeight(Weight.SEMIBOLD);
    subHeader.getStyleClass().add("text-muted");

    VBox headerContainer = new VBox(5, titleHeader, subHeader);
    headerContainer.setAlignment(Pos.CENTER);

    // Content grid
    HBox contentContainer = new HBox(20);
    contentContainer.setAlignment(Pos.CENTER);

    Card playersCard = new Card();
    playersCard.setPrefWidth(300);

    playersCard.setPadding(new Insets(15));

    Header playersHeader = new Header("Players:");
    playersHeader.withType(HeaderType.H4).withFontWeight(Weight.SEMIBOLD);
    playersCard.setTop(playersHeader);

    // Controls
    VBox playersControls = new VBox(10);
    playersControls.setFillWidth(true);
    playersControls.setPadding(new Insets(10, 0, 10, 0));

    // New Player Controls
    HBox playerNameControl = new HBox(10);
    TextField playerNameField = new TextField();
    playerNameField.setPromptText("Enter player name");
    HBox.setHgrow(playerNameField, Priority.ALWAYS);

    Button addPlayerButton =
        new Button("Add Player", BoxiconsRegular.PLUS).withVariant(ButtonVariant.SUCCESS);
    addPlayerButton.setOnAction(e -> {
      if (playerNameField.getText().trim().isBlank()) {
        return;
      }
      var newPlayer = new Player(playerNameField.getText().trim(), Utils.getRandomColor());
      this.controller.addPlayer(newPlayer);
      playerNameField.clear();
    });

    playerNameControl.getChildren().addAll(playerNameField, addPlayerButton);

    // Save players button
    Button savePlayersButton = new Button("Save Players", BoxiconsRegular.SAVE);
    savePlayersButton.setOnAction(e -> this.controller.savePlayersToFile());
    savePlayersButton.setMaxWidth(Double.MAX_VALUE);

    // Players list
    VBox playersList = new VBox(10);
    playersList.setFillWidth(true);

    ListView<Player> playerListView = new ListView<>(this.controller.getPlayers());

    playerListView.setCellFactory(player -> {
      ListCell<Player> cell = new ListCell<>() {
        @Override
        protected void updateItem(Player item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(null);
            setGraphic(null);
          } else {
            setText(null);
            setGraphic(createPlayerEntry(item));
          }
        }
      };
      cell.getStyleClass().clear();
      cell.setPadding(new Insets(0, 0, 10, 0));
      cell.prefWidthProperty().bind(playerListView.widthProperty().subtract(20));
      return cell;
    });
    playerListView.getStyleClass().clear();
    // Make the player list scrollable
    ScrollPane playersScrollPane = new ScrollPane(playerListView);
    playersScrollPane.getStyleClass().clear(); // no style classes
    playersScrollPane.setFitToWidth(true);
    playersScrollPane.setFitToHeight(true);
    playersScrollPane.setPannable(true);

    // Add controls to the card
    playersControls.getChildren().addAll(playerNameControl, savePlayersButton, playersScrollPane);
    playersCard.setCenter(playersControls);

    VBox bottomControls = new VBox(10);
    // Exit Button
    Button exitButton = new Button("Exit to Desktop", BoxiconsRegular.EXIT);
    exitButton.withVariant(ButtonVariant.SECONDARY);
    exitButton.setMaxWidth(Double.MAX_VALUE);
    exitButton.setStyle("-fx-font-size: 16px;");
    exitButton.setOnAction(e -> {
      controller.exitToDesktop();
    });

    Button toggleThemeButton = new Button("Toggle Dark/Light Theme", BoxiconsRegular.TONE);
    toggleThemeButton.withVariant(ButtonVariant.SECONDARY);
    toggleThemeButton.setMaxWidth(Double.MAX_VALUE);
    toggleThemeButton.setStyle("-fx-font-size: 16px;");
    toggleThemeButton.setOnAction(e -> {
      controller.toggleTheme();
    });

    Button loadCustomGameButton = new Button("Load Custom JSON Game", BoxiconsRegular.UPLOAD);
    loadCustomGameButton.withVariant(ButtonVariant.SECONDARY);
    loadCustomGameButton.setMaxWidth(Double.MAX_VALUE);
    loadCustomGameButton.setStyle("-fx-font-size: 16px;");
    loadCustomGameButton.setOnAction(e -> {
      this.controller.loadGameFromFile();
    });

    bottomControls.getChildren().addAll(toggleThemeButton, loadCustomGameButton, exitButton);

    playersCard.setBottom(bottomControls);

    // Game Section Card
    Card gamesCard = new Card();
    gamesCard.setMaxWidth(Double.MAX_VALUE);
    gamesCard.setPadding(new Insets(15));

    // Games header
    Header gamesHeader = new Header("Games:");
    gamesHeader.withType(HeaderType.H4).withFontWeight(Weight.SEMIBOLD);
    gamesCard.setTop(gamesHeader);

    // Games list
    FlowPane gameGrid = new FlowPane(10, 10);
    gameGrid.setPadding(new Insets(10, 0, 0, 0));
    gameGrid.setPrefWrapLength(600);

    if (gamesListener != null) {
      controller.getGames().removeListener(gamesListener);
    }
    gamesListener = (change) -> {
      createGameEntries(gameGrid);
    };
    controller.getGames().addListener(gamesListener);

    createGameEntries(gameGrid);

    ScrollPane gameScrollPane = new ScrollPane(gameGrid);
    gameScrollPane.getStyleClass().clear();
    gameScrollPane.setFitToWidth(true);
    gameScrollPane.setPrefHeight(500);

    gamesCard.setCenter(gameScrollPane);

    contentContainer.getChildren().addAll(playersCard, gamesCard);

    mainContent.getChildren().addAll(headerContainer, contentContainer);

    root.getChildren().add(mainContent);

    return root;
  }

  private Card createPlayerEntry(Player player) {
    HBox playerEntry = new HBox(10);
    Card playerCard = new Card(playerEntry).withRounded(Size.SM);

    playerCard.setPadding(new Insets(10));

    playerEntry.setAlignment(Pos.CENTER_LEFT);

    Button saveButton = new Button(BoxiconsRegular.CHECK).withVariant(ButtonVariant.SUCCESS);
    saveButton.setDisable(true);

    TextField playerNameField = new TextField(player.getName());
    playerNameField.setMaxWidth(Double.MAX_VALUE);
    playerNameField.getStyleClass().add("player-name-field");

    ColorPicker colorPicker = new ColorPicker(Utils.toJFXColor(player.getColor()));
    colorPicker.getStyleClass().add("button");

    var validateUserInput = new Object() {
      public void handle() {
        var color = Utils.toModelColor(colorPicker.getValue());

        saveButton.setDisable((playerNameField.getText().isBlank() || playerNameField.getText()
            .equals(player.getName())) && color.equals(player.getColor()));
      }
    };

    playerNameField.textProperty().addListener(e -> validateUserInput.handle());
    colorPicker.setOnAction(e -> validateUserInput.handle());

    saveButton.setOnAction(e -> {
      this.controller.updatePlayer(player, playerNameField.getText(),
          Utils.toModelColor(colorPicker.getValue()));
    });

    // Remove player button
    Button removeButton = new Button(BoxiconsRegular.TRASH).withVariant(ButtonVariant.DESTRUCTIVE);
    removeButton.setOnAction(e -> {
      this.controller.removePlayer(player);
    });

    // Add all components to the player entry
    playerEntry.getChildren().addAll(playerNameField, colorPicker, saveButton, removeButton);
    return playerCard;
  }

  private void createGameEntries(Pane gameContainer) {
    gameContainer.getChildren().clear();
    controller.getGames().forEach((gameId, game) -> {
      GameCard gameCard = new GameCard(game);
      gameCard.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        this.controller.startGame(gameId);
      });
      gameContainer.getChildren().add(gameCard);
    });
  }
}
