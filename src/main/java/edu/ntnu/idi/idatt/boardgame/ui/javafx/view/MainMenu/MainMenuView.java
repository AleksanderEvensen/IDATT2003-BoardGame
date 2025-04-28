package edu.ntnu.idi.idatt.boardgame.ui.javafx.view.MainMenu;

import java.util.Random;
import java.util.logging.Logger;
import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;
import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Button;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The main menu view of the application. This view displays available games and player management.
 */
public class MainMenuView implements IView {

    private static final Logger logger = Logger.getLogger(MainMenuView.class.getName());

    private BorderPane root;
    private StackPane rootStack;
    private MainMenuController controller;


    private ListChangeListener<Player> playerSectionListener;

    public MainMenuView(MainMenuController controller) {
        this.controller = controller;
    }

    @Override
    public void load() {
        this.controller.initialize();
        logger.info("Loading MainMenuView");
    }

    @Override
    public void unload() {
        logger.info("Unloading MainMenuView");
        if (this.playerSectionListener != null) {
            this.controller.getPlayers().removeListener(this.playerSectionListener);
        }
    }

    @Override
    public Pane createRoot() {
        // Main layout
        VBox mainLayout = new VBox(20);
        mainLayout.getStyleClass().add("view-root");
        mainLayout.setPadding(new Insets(20));
        mainLayout.setBackground(
                new Background(new BackgroundFill(Color.web("#FFECB3"), null, null))); // From
                                                                                       // amber-50
                                                                                       // to
                                                                                       // amber-100
                                                                                       // (approx.)

        // Header
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("Awesome Board Games");
        titleLabel.setTextFill(Color.web("#E65100")); // amber-800 (approx.)
        Label subtitleLabel = new Label("Select players and a game to begin");
        subtitleLabel.setTextFill(Color.web("#F57C00")); // amber-700 (approx.)
        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Content grid
        GridPane contentGrid = new GridPane();
        contentGrid.setHgap(20);
        contentGrid.setAlignment(Pos.CENTER);

        // Player Management Panel
        VBox playerPanel = new VBox(10);
        playerPanel.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4); -fx-padding: 15;");
        Label playerTitle = new Label("Players");
        // playerTitle.setFont(Font.font("Arial", 20, javafx.scene.text.FontWeight.BOLD));
        playerTitle.setTextFill(Color.web("#E65100")); // amber-800 (approx.)

        HBox addPlayerBox = new HBox(10);
        var newPlayerNameInput = new TextField();
        newPlayerNameInput.setPromptText("Enter player name");
        newPlayerNameInput.setStyle("-fx-border-color: #FFD54F; -fx-focus-color: #FFB300;"); // border-amber-300,
                                                                                             // focus-visible:ring-amber-500
                                                                                             // (approx.)

        Button addButton = new Button("Add");
        addButton.setGraphic(new FontIcon(BoxiconsRegular.PLUS));
        addButton.setStyle("-fx-background-color: #FFA000; -fx-text-fill: white;"); // bg-amber-600
        addButton.setOnMouseEntered(event -> addButton
                .setStyle("-fx-background-color: #FF8F00; -fx-text-fill: white;")); // hover:bg-amber-700
        addButton.setOnMouseExited(event -> addButton
                .setStyle("-fx-background-color: #FFA000; -fx-text-fill: white;"));
        addButton.setOnAction(event -> {
            if (newPlayerNameInput.getText().trim().isEmpty()) {
                return; // Don't add player with empty name
            }
            controller.addPlayer(
                    new Player(newPlayerNameInput.getText(), Utils.toModelColor(Color.LIGHTBLUE)));
            newPlayerNameInput.clear();

        });

        addPlayerBox.getChildren().addAll(newPlayerNameInput, addButton);

        var playerListView = new ListView<Player>(this.controller.getPlayers());
        playerListView.setPrefHeight(200);
        VBox.setVgrow(playerListView, Priority.ALWAYS);

        Label noPlayersLabel = new Label("No players added yet");
        noPlayersLabel
                .setStyle("-fx-text-fill: #F57C00; -fx-font-style: italic; -fx-font-size: 12;"); // text-amber-600,
                                                                                                 // text-sm,
                                                                                                 // italic

        VBox playerListContainer = new VBox(5);
        playerListContainer.getChildren().add(playerListView);
        if (controller.getPlayers().isEmpty()) {
            playerListContainer.getChildren().add(noPlayersLabel);
        }

        playerListView.setCellFactory(param -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);
                if (empty || player == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox row = new HBox(10);
                    row.setAlignment(Pos.CENTER_LEFT);
                    Label playerNameLabel = new Label(player.getName());

                    Button removeButton = new Button();

                    removeButton.setGraphic(new FontIcon(BoxiconsRegular.X));
                    removeButton
                            .setStyle("-fx-background-color: transparent; -fx-text-fill: #F57C00;"); // variant="ghost",
                                                                                                     // text-amber-700
                    removeButton.setOnMouseEntered(event -> removeButton
                            .setStyle("-fx-background-color: #FFCDD2; -fx-text-fill: #E53935;")); // hover:bg-red-50,
                                                                                                  // hover:text-red-600
                    removeButton.setOnMouseExited(event -> removeButton.setStyle(
                            "-fx-background-color: transparent; -fx-text-fill: #F57C00;"));
                    removeButton.setOnAction(event -> {
                        int index = getIndex();
                        if (index >= 0 && index < controller.getPlayers().size()) {
                            // removePlayer(index);
                        }
                    });
                    Tooltip removeTooltip = new Tooltip("Remove " + player);
                    removeButton.setTooltip(removeTooltip);

                    row.getChildren().addAll(playerNameLabel, removeButton);
                    HBox container = new HBox();
                    container.setStyle(
                            "-fx-background-color: #FFF8E1; -fx-background-radius: 5; -fx-padding: 5;"); // bg-amber-50,
                                                                                                         // rounded-lg
                    container.getChildren().add(row);
                    HBox.setHgrow(playerNameLabel, Priority.ALWAYS);
                    setGraphic(container);
                }
            }
        });

        Button exitButton = new Button("Exit to Desktop");
        exitButton.setOnAction(e -> {
            controller.exitToDesktop();
        });

        exitButton.setGraphic(new FontIcon(BoxiconsRegular.EXIT));
        exitButton.setStyle(
                "-fx-border-color: #FFD54F; -fx-text-fill: #E65100; -fx-background-color: transparent;"); // variant="outline",
                                                                                                          // border-amber-300,
                                                                                                          // text-amber-800
        exitButton.setOnMouseEntered(event -> exitButton.setStyle(
                "-fx-border-color: #FFD54F; -fx-text-fill: #F57C00; -fx-background-color: #FFF8E1;")); // hover:bg-amber-100,
                                                                                                       // hover:text-amber-900
        exitButton.setOnMouseExited(event -> exitButton.setStyle(
                "-fx-border-color: #FFD54F; -fx-text-fill: #E65100; -fx-background-color: transparent;"));
        exitButton.setMaxWidth(Double.MAX_VALUE);

        playerPanel.getChildren().addAll(playerTitle, addPlayerBox, playerListContainer,
                exitButton);
        contentGrid.add(playerPanel, 0, 0);

        // Game Selection Panel
        VBox gamePanel = new VBox(10);
        gamePanel.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4); -fx-padding: 15;");
        Label gameTitle = new Label("Select a Game");
        gameTitle.setTextFill(Color.web("#E65100")); // amber-800 (approx.)

        FlowPane gameGrid = new FlowPane(10, 10);
        gameGrid.setPrefWrapLength(600); // Approximate width for 2-3 columns

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
            ImageView gameImageView = new ImageView(new Image("images/ladder.png"));
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
        gameScrollPane.setFitToWidth(true);
        gameScrollPane.setPrefHeight(500);

        gamePanel.getChildren().addAll(gameTitle, gameScrollPane);
        contentGrid.add(gamePanel, 1, 0);

        mainLayout.getChildren().addAll(header, contentGrid);

        return mainLayout;
    }

    private VBox createPlayersSection() {
        VBox playersSection = new VBox(20);
        playersSection.setPadding(new Insets(10));
        playersSection.setPrefWidth(400);

        Label playersLabel = new Label("Players");
        playersLabel.setFont(Font.font("System", FontWeight.BOLD, 24));


        // Create player entries in a scrollable container
        VBox playerEntries = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(playerEntries);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        if (this.playerSectionListener != null) {
            this.controller.getPlayers().removeListener(this.playerSectionListener);
        }

        this.playerSectionListener = e -> {
            createPlayerEntries(playerEntries, this.controller.getPlayers());
        };
        this.controller.getPlayers().addListener(playerSectionListener);

        createPlayerEntries(playerEntries, this.controller.getPlayers());

        // Create "Add New Player" button and section
        Button addPlayerButton = new Button("Add New Player");
        addPlayerButton.setPrefWidth(380);
        addPlayerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addPlayerButton.setOnAction(e -> showAddPlayerPane(playerEntries));

        // Create Save Players button
        Button savePlayersButton = new Button("Save Players");
        savePlayersButton.setPrefWidth(380);
        savePlayersButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        savePlayersButton.setOnAction(e -> this.controller.savePlayersToFile());

        playersSection.getChildren().addAll(playersLabel, scrollPane, addPlayerButton,
                savePlayersButton);
        return playersSection;
    }

    private void createPlayerEntries(VBox container, ObservableList<Player> players) {
        container.getChildren().clear();

        players.forEach(player -> {
            HBox playerEntry = new HBox(10);

            playerEntry.setAlignment(Pos.CENTER_LEFT);
            playerEntry.setPadding(new Insets(5));
            playerEntry.setStyle("-fx-background-color: #333333; -fx-background-radius: 5;");

            // Player name field
            TextField playerNameField = new TextField(player.getName());
            playerNameField.setPrefWidth(120);
            playerNameField.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");

            ColorPicker colorPicker = new ColorPicker(Utils.toJFXColor(player.getColor()));
            // Remove player button
            Button saveButton = new Button();
            var saveIcon = new FontIcon(BoxiconsRegular.SAVE);
            saveIcon.setFill(Color.WHITE);
            saveButton.setGraphic(saveIcon);
            saveButton.setStyle(
                    "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 15; -fx-min-width: 30px; -fx-min-height: 30px;");
            saveButton.setOnAction(e -> {
                this.controller.updatePlayer(player, playerNameField.getText(),
                        Utils.toModelColor(colorPicker.getValue()));
            });
            // Remove player button
            Button removeButton = new Button();
            var removeIcon = new FontIcon(BoxiconsRegular.TRASH);
            removeIcon.setFill(Color.WHITE);
            removeButton.setGraphic(removeIcon);
            removeButton.setStyle(
                    "-fx-background-color: #e74c3c; -fx-fill: white; -fx-background-radius: 15; -fx-min-width: 30px; -fx-min-height: 30px;");
            removeButton.setOnAction(e -> {
                this.controller.removePlayer(player);
            });

            // Add all components to the player entry
            playerEntry.getChildren().addAll(playerNameField, colorPicker, saveButton,
                    removeButton);
            container.getChildren().add(playerEntry);
        });
    }


    private void showAddPlayerPane(VBox playerEntries) {
        // Create a semi-transparent dark overlay
        Rectangle overlay = new Rectangle();
        overlay.setWidth(2000); // Make it large enough to cover the entire view
        overlay.setHeight(2000);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        // Create a form to add a new player
        VBox addPlayerForm = new VBox(10);
        addPlayerForm.setPadding(new Insets(20));
        addPlayerForm.setMaxWidth(400);
        addPlayerForm.setMaxHeight(500);
        addPlayerForm.setStyle(
                "-fx-background-color: #333333; -fx-border-color: #666666; -fx-border-width: 2px; -fx-background-radius: 5;");

        // Center the form in the stack pane
        StackPane.setAlignment(addPlayerForm, Pos.CENTER);

        Label formTitle = new Label("Add New Player");
        formTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        formTitle.setStyle("-fx-text-fill: white;");

        TextField nameField = new TextField();
        nameField.setPromptText("Player Name");
        nameField.setPrefWidth(360);
        nameField.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");

        Label nameLabel = new Label("Player Name:");
        nameLabel.setStyle("-fx-text-fill: white;");

        // Color selection
        Label colorLabel = new Label("Player Color:");
        colorLabel.setStyle("-fx-text-fill: white;");

        ColorPicker colorPicker = new ColorPicker(javafx.scene.paint.Color.LIGHTBLUE);
        colorPicker.setPrefWidth(360);

        // Icon selection
        Label iconLabel = new Label("Player Icon:");
        iconLabel.setStyle("-fx-text-fill: white;");

        Label previewLabel = new Label("Preview:");
        previewLabel.setStyle("-fx-text-fill: white;");

        HBox previewBox = new HBox(10);
        previewBox.setAlignment(Pos.CENTER);
        previewBox.getChildren().addAll(previewLabel);

        // Add player button
        Button addButton = new Button("Add Player");
        addButton.setPrefWidth(360);
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            if (nameField.getText().trim().isEmpty()) {
                return; // Don't add player with empty name
            }

            var newPlayer =
                    new Player(nameField.getText(), Utils.toModelColor(colorPicker.getValue()));
            this.controller.addPlayer(newPlayer);

            // Remove both the form and overlay
            rootStack.getChildren().removeAll(overlay, addPlayerForm);
        });

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(360);
        cancelButton.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> {
            rootStack.getChildren().removeAll(overlay, addPlayerForm);
        });

        addPlayerForm.getChildren().addAll(formTitle, nameLabel, nameField, colorLabel, colorPicker,
                iconLabel, previewBox, new VBox(10), // Spacer
                addButton, cancelButton);
        // Add both the overlay and form to the stack pane
        // Adding the overlay first ensures it appears behind the form
        rootStack.getChildren().addAll(overlay, addPlayerForm);
    }
}
