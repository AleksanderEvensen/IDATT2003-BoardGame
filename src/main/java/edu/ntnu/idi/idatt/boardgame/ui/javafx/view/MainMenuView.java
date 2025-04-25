package edu.ntnu.idi.idatt.boardgame.ui.javafx.view;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.IView;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.player.JavaFXPlayer;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.player.PawnAssetManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    private List<JavaFXPlayer> players = new ArrayList<>();
    private BorderPane root;
    private StackPane rootStack;

    @Override
    public void load() {
        logger.info("Loading MainMenuView");

        players.clear();
        Application.getPlayerManager().getPlayers().forEach(player -> {
            if (player instanceof JavaFXPlayer) {
                players.add((JavaFXPlayer) player);
            } else {
                players.add(new JavaFXPlayer(player.getPlayerId(), player.getName()));
            }
        });
    }

    @Override
    public Pane createRoot() {
        // Create the root layout with a StackPane as main container for modals/dialogs
        rootStack = new StackPane();
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // Create the title header
        var titleLabel = new Label("Awesome Board Games");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 40));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);

        // Create players section on the left
        var playersSection = createPlayersSection();
        root.setLeft(playersSection);

        var rightSection = new VBox(20);
        rightSection.setPrefWidth(300);
        root.setRight(rightSection);

        // Create games section on the right
        var menuButtons = new VBox(20);
        var ladderGameButton = new Button("Ladder Game");
        ladderGameButton.setPrefWidth(400);
        ladderGameButton.setPrefHeight(60);
        ladderGameButton.setOnAction(e -> Application.router.navigate("/game/ladder"));

        var exitButton = new Button("Exit to Desktop");
        exitButton.setPrefWidth(400);
        exitButton.setPrefHeight(60);
        exitButton.setOnAction(e -> Application.closeApplication());

        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.getChildren().addAll(ladderGameButton, exitButton);

        root.setCenter(menuButtons);

        // Add the main layout to the stack
        rootStack.getChildren().add(root);

        return rootStack;
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

        updatePlayerEntries(playerEntries);

        // Create "Add New Player" button and section
        Button addPlayerButton = new Button("Add New Player");
        addPlayerButton.setPrefWidth(380);
        addPlayerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addPlayerButton.setOnAction(e -> showAddPlayerPane(playerEntries));

        // Create Save Players button
        Button savePlayersButton = new Button("Save Players");
        savePlayersButton.setPrefWidth(380);
        savePlayersButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        savePlayersButton.setOnAction(e -> savePlayers());

        playersSection.getChildren().addAll(playersLabel, scrollPane, addPlayerButton,
                savePlayersButton);
        return playersSection;
    }

    private void updatePlayerEntries(VBox container) {
        container.getChildren().clear();

        for (int i = 0; i < players.size(); i++) {
            JavaFXPlayer player = players.get(i);
            final int playerIndex = i;

            HBox playerEntry = new HBox(10);
            playerEntry.setAlignment(Pos.CENTER_LEFT);
            playerEntry.setPadding(new Insets(5));
            playerEntry.setStyle("-fx-background-color: #333333; -fx-background-radius: 5;");

            // Player icon/pawn preview
            ImageView pawnView = new ImageView(player.getPawnImage());
            pawnView.setFitHeight(30);
            pawnView.setFitWidth(30);
            pawnView.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 15;",
                    colorToHex(player.getColor())));

            // Player name field
            TextField playerNameField = new TextField(player.getName());
            playerNameField.setPrefWidth(120);
            playerNameField.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");
            playerNameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    player.setName(playerNameField.getText());
                }
            });

            // Color picker button
            ColorPicker colorPicker = new ColorPicker(player.getColor());
            colorPicker.setPrefWidth(80);
            colorPicker.setOnAction(e -> {
                Color newColor = colorPicker.getValue();
                player.setColor(newColor);
                pawnView.setStyle(
                        String.format("-fx-background-color: %s; -fx-background-radius: 15;",
                                colorToHex(newColor)));
            });

            // Icon selector button
            Button iconButton = new Button("Icon");
            iconButton.setOnAction(e -> showIconSelector(player, pawnView));

            // Remove player button
            Button removeButton = new Button("✕");
            removeButton.setStyle(
                    "-fx-background-color: #FF5555; -fx-text-fill: white; -fx-background-radius: 15; -fx-min-width: 30px; -fx-min-height: 30px;");
            removeButton.setOnAction(e -> {
                players.remove(playerIndex);
                updatePlayerEntries(container);
            });

            // Add all components to the player entry
            playerEntry.getChildren().addAll(pawnView, playerNameField, colorPicker, iconButton,
                    removeButton);
            container.getChildren().add(playerEntry);
        }
    }

    private void showIconSelector(JavaFXPlayer player, ImageView pawnView) {
        // Create a semi-transparent dark overlay
        Rectangle overlay = new Rectangle();
        overlay.setWidth(2000); // Make it large enough to cover the entire view
        overlay.setHeight(2000);
        overlay.setFill(Color.rgb(0, 0, 0, 0.7)); // Semi-transparent black

        // Create a dialog for icon selection
        BorderPane iconDialog = new BorderPane();
        iconDialog.setPadding(new Insets(10));
        iconDialog.setPrefSize(400, 300);
        iconDialog.setStyle(
                "-fx-background-color: #333333; -fx-border-color: #666666; -fx-border-width: 2px; -fx-background-radius: 5;");

        // Center the dialog in the stack pane
        StackPane.setAlignment(iconDialog, Pos.CENTER);

        Label titleLabel = new Label("Select Player Icon");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: white;");
        iconDialog.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(10));

        // Create a grid of available icons
        VBox selectionContainer = new VBox(10);
        selectionContainer.setPadding(new Insets(10));
        selectionContainer.setAlignment(Pos.CENTER);

        PawnAssetManager pawnManager = PawnAssetManager.getInstance();
        List<String> pawnImages = pawnManager.getAvailablePawnImages();

        ComboBox<String> pawnSelector =
                new ComboBox<>(FXCollections.observableArrayList(pawnImages));
        pawnSelector.setValue(player.getPawnImageName());

        // Preview of selected pawn
        ImageView previewImage = new ImageView(player.getPawnImage());
        previewImage.setFitHeight(50);
        previewImage.setFitWidth(50);

        pawnSelector.setOnAction(e -> {
            String selectedPawn = pawnSelector.getValue();
            if (player.setPawnImage(selectedPawn)) {
                previewImage.setImage(player.getPawnImage());
                pawnView.setImage(player.getPawnImage());
            }
        });

        Label previewLabel = new Label("Preview:");
        previewLabel.setStyle("-fx-text-fill: white;");

        HBox previewBox = new HBox(10);
        previewBox.setAlignment(Pos.CENTER);
        previewBox.getChildren().addAll(previewLabel, previewImage);

        Label iconLabel = new Label("Available Icons:");
        iconLabel.setStyle("-fx-text-fill: white;");

        // Buttons for confirm/cancel
        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        confirmButton.setOnAction(e -> {
            // Remove both the dialog and overlay
            rootStack.getChildren().removeAll(overlay, iconDialog);
        });

        selectionContainer.getChildren().addAll(iconLabel, pawnSelector, previewBox, new VBox(20), // Spacer
                confirmButton);

        iconDialog.setCenter(selectionContainer);

        // Add both the overlay and dialog to the stack pane
        // Adding the overlay first ensures it appears behind the dialog
        rootStack.getChildren().addAll(overlay, iconDialog);
    }

    private void showAddPlayerPane(VBox playerEntries) {
        // Create a semi-transparent dark overlay
        Rectangle overlay = new Rectangle();
        overlay.setWidth(2000); // Make it large enough to cover the entire view
        overlay.setHeight(2000);
        overlay.setFill(Color.rgb(0, 0, 0, 0.7)); // Semi-transparent black

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

        ColorPicker colorPicker = new ColorPicker(Color.LIGHTBLUE);
        colorPicker.setPrefWidth(360);

        // Icon selection
        Label iconLabel = new Label("Player Icon:");
        iconLabel.setStyle("-fx-text-fill: white;");

        ComboBox<String> iconSelector = new ComboBox<>(FXCollections
                .observableArrayList(PawnAssetManager.getInstance().getAvailablePawnImages()));
        iconSelector.setValue(PawnAssetManager.getInstance().getDefaultPawnImageName());
        iconSelector.setPrefWidth(360);

        // Preview
        ImageView previewImage = new ImageView(PawnAssetManager.getInstance()
                .getPawnImage(PawnAssetManager.getInstance().getDefaultPawnImageName()));
        previewImage.setFitHeight(50);
        previewImage.setFitWidth(50);

        iconSelector.setOnAction(e -> {
            previewImage
                    .setImage(PawnAssetManager.getInstance().getPawnImage(iconSelector.getValue()));
        });

        Label previewLabel = new Label("Preview:");
        previewLabel.setStyle("-fx-text-fill: white;");

        HBox previewBox = new HBox(10);
        previewBox.setAlignment(Pos.CENTER);
        previewBox.getChildren().addAll(previewLabel, previewImage);

        // Add player button
        Button addButton = new Button("Add Player");
        addButton.setPrefWidth(360);
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            if (nameField.getText().trim().isEmpty()) {
                return; // Don't add player with empty name
            }

            // Create new player and add to list
            JavaFXPlayer newPlayer = new JavaFXPlayer(players.size() + 1, nameField.getText(),
                    colorPicker.getValue(), iconSelector.getValue());

            players.add(newPlayer);
            updatePlayerEntries(playerEntries);

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
                iconLabel, iconSelector, previewBox, new VBox(10), // Spacer
                addButton, cancelButton);

        // Add both the overlay and form to the stack pane
        // Adding the overlay first ensures it appears behind the form
        rootStack.getChildren().addAll(overlay, addPlayerForm);
    }

    private void savePlayers() {
        // Save players to the PlayerManager
        Application.getPlayerManager().savePlayers("players.csv", new ArrayList<>(players));
        logger.info("Saved " + players.size() + " players");
    }

    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }
}
