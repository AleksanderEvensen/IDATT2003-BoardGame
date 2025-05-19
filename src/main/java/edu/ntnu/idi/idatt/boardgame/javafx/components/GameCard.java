package edu.ntnu.idi.idatt.boardgame.javafx.components;

import edu.ntnu.idi.idatt.boardgame.model.Game;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A component representing a game card to display info about a game.
 *
 * @since v2.0.0
 */
public class GameCard extends VBox {

  private final Game game;
  private boolean isSelected = false;

  public GameCard(Game game, Runnable onSelect) {
    this.game = game;

    this.setSpacing(10);
    this.setPadding(new Insets(10));
    this.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8;");
    this.setCursor(Cursor.HAND);
    this.setPrefWidth(200);
    this.setMaxWidth(Double.MAX_VALUE);
    this.getStyleClass().add("game-card");

    StackPane imageWrapper = new StackPane();
    imageWrapper.setPrefHeight(120);
    imageWrapper.setMaxWidth(Double.MAX_VALUE);
    imageWrapper.setStyle("-fx-overflow: hidden;");

    ImageView imageView = new ImageView();
    imageView.setFitHeight(120);
    imageView.setPreserveRatio(false);
    imageView.setSmooth(true);
    imageView.setCache(true);
    imageView.setStyle("-fx-background-color: lightgray;");
    imageView.setImage(
        new Image(
            game.getImagePath().orElse("placeholder.png"),
            200,
            120,
            false,
            true));

    imageView.setFitWidth(200);
    imageWrapper.getChildren().add(imageView);

    Label nameLabel = new Label(game.getName());
    nameLabel.setFont(Font.font("System", 16));
    nameLabel.setStyle("-fx-font-weight: bold;");

    Label descriptionLabel = new Label(game.getDescription());
    descriptionLabel.setWrapText(true);
    descriptionLabel.setFont(Font.font("System", 12));

    Label playersLabel = new Label(
        game.getMinPlayers() == game.getMaxPlayers()
            ? game.getMinPlayers() + " players"
            : game.getMinPlayers() + "-" + game.getMaxPlayers() + " players");
    playersLabel.setFont(Font.font(12));

    Label diceLabel = new Label(game.getNumberOfDice() > 0
        ? game.getNumberOfDice() + " dice"
        : "No dice");
    diceLabel.setFont(Font.font(12));

    VBox infoBox = new VBox(4, playersLabel, diceLabel);
    this.getChildren().addAll(imageWrapper, nameLabel, descriptionLabel, infoBox);

    this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      setSelected(true);
      onSelect.run();
    });

    updateStyle();
  }

  public void setSelected(boolean selected) {
    this.isSelected = selected;
    updateStyle();
  }

  private void updateStyle() {
    if (isSelected) {
      this.setBorder(
          new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(6),
              new BorderWidths(2))));
    } else {
      this.setBorder(null);
    }
  }
}
