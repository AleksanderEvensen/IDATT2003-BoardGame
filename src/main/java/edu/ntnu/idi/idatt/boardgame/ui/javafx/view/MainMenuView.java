package edu.ntnu.idi.idatt.boardgame.ui.javafx.view;

import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Button;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Button.ButtonVariant;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Card;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Header;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.enums.Size;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.enums.Weight;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.controllers.MainMenuController;
import java.io.File;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;

/**
 * The main menu view of the application. This view displays available games and player management.
 */
public class MainMenuView implements IView {

  private static final Logger logger = Logger.getLogger(MainMenuView.class.getName());

  private final MainMenuController controller;


  private ListChangeListener<Player> playerSectionListener;

  public MainMenuView(MainMenuController controller) {
    this.controller = controller;
  }

  public Image GAME_FALLBACK_IMAGE = new Image("images/not_found.png");

  @Override
  public void load() {
    this.controller.initialize();
  }

  @Override
  public void unload() {
    if (this.playerSectionListener != null) {
      this.controller.getPlayers().removeListener(this.playerSectionListener);
    }
  }

  @Override
  public Pane createRoot() {
    // Main layout
    VBox root = new VBox(20);
    root.getStyleClass().add("view-root");
    root.setPadding(new Insets(20));

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

    // Exit Button
    Button exitButton = new Button("Exit to Desktop", BoxiconsRegular.EXIT);
    exitButton.withVariant(ButtonVariant.SECONDARY);
    exitButton.setMaxWidth(Double.MAX_VALUE);
    exitButton.setStyle("-fx-font-size: 16px;");
    exitButton.setOnAction(e -> {
      controller.exitToDesktop();
    });
    playersCard.setBottom(exitButton);

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

    // TODO: Use game card component
    Application.getGameManager().getAvailableGameIds().forEach(gameId -> {
      Game game = Application.getGameManager().getGame(gameId);

      VBox gameCard = new VBox(5);
      gameCard.setStyle(
          "-fx-border-color: #FFE0B2; -fx-border-radius: 5; -fx-padding: 5; -fx-cursor: hand;"); // border-amber-200
      gameCard.setOnMouseEntered(event -> gameCard.setStyle(
          "-fx-border-color: #FFCA28; -fx-border-radius: 5; -fx-padding: 5; -fx-cursor: hand;"));
      // hover:border-amber-400
      gameCard.setOnMouseExited(event -> gameCard.setStyle(
          "-fx-border-color: #FFE0B2; -fx-border-radius: 5; -fx-padding: 5; -fx-cursor: hand;"));

      StackPane imagePane = new StackPane();
      ImageView gameImageView = new ImageView(GAME_FALLBACK_IMAGE);
      game.getImagePath().ifPresent(path -> {
        var file = new File(path);
        if (file.exists()) {
          gameImageView.setImage(new Image(file.toURI().toString()));
        }
      });
      gameImageView.setFitHeight(120); // Approximate height
      gameImageView.setFitWidth(180); // Approximate width
      imagePane.getChildren().add(gameImageView);
      imagePane.setStyle("-fx-background-color: #EEE;"); // Placeholder background

      Label gameNameLabel = new Label(game.getName());
      gameNameLabel.setTextFill(Color.web("#E65100")); // amber-800 (approx.)

      VBox cardContent = new VBox(5);
      cardContent.setPadding(new Insets(10));
      cardContent.getChildren().add(gameNameLabel);

      gameCard.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        this.controller.startGame(gameId);
      });

      gameCard.getChildren().addAll(imagePane, cardContent);
      gameGrid.getChildren().add(gameCard);
    });

    ScrollPane gameScrollPane = new ScrollPane(gameGrid);
    gameScrollPane.getStyleClass().clear();
    gameScrollPane.setFitToWidth(true);
    gameScrollPane.setPrefHeight(500);

    gamesCard.setCenter(gameScrollPane);

    contentContainer.getChildren().addAll(playersCard, gamesCard);

    root.getChildren().addAll(headerContainer, contentContainer);
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
    ColorPicker colorPicker = new ColorPicker(Utils.toJFXColor(player.getColor()));
    colorPicker.getStyleClass().add("button");
    Button removeButton = new Button(BoxiconsRegular.TRASH).withVariant(ButtonVariant.DESTRUCTIVE);

    playerNameField.setMaxWidth(Double.MAX_VALUE);
    playerNameField.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
    var validateUserInput = new Object() {
      public void handle() {
        var color = Utils.toModelColor(colorPicker.getValue());

        saveButton.setDisable((playerNameField.getText().isBlank()
            || playerNameField.getText().equals(player.getName()))
            && color.equals(player.getColor()));
      }
    };

    playerNameField.setOnKeyTyped(e -> validateUserInput.handle());
    colorPicker.setOnAction(e -> {
      validateUserInput.handle();
    });

    saveButton.setOnAction(e -> {
      this.controller.updatePlayer(player, playerNameField.getText(),
          Utils.toModelColor(colorPicker.getValue()));
    });

    // Remove player button
    removeButton.setOnAction(e -> {
      this.controller.removePlayer(player);
    });

    // Add all components to the player entry
    playerEntry.getChildren().addAll(playerNameField, colorPicker, saveButton, removeButton);
    return playerCard;
  }

}
