package edu.ntnu.idi.idatt.boardgame.javafx.components;

import edu.ntnu.idi.idatt.boardgame.model.Game;
import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * A component representing a game card to display info about a game.
 *
 * @since v2.0.0
 */
public class GameCard extends VBox {


  private static Image GAME_FALLBACK_IMAGE = new Image("images/not_found.png");


  public GameCard(Game game) {
    super(5);
    this.setStyle(
        "-fx-border-color: -fx-color-border; -fx-border-radius: 5; -fx-padding: 5; -fx-cursor: hand;");
    this.setOnMouseEntered(event -> this.setStyle(
        "-fx-border-color: -fx-color-accent; -fx-border-radius: 5; -fx-padding: 5; -fx-cursor: hand;"));
    this.setOnMouseExited(event -> this.setStyle(
        "-fx-border-color: -fx-color-border; -fx-border-radius: 5; -fx-padding: 5; -fx-cursor: hand;"));

    ImageView gameImageView = new ImageView(GAME_FALLBACK_IMAGE);
    game.getImagePath().ifPresent(path -> {
      var file = new File(path);
      if (file.exists()) {
        gameImageView.setImage(new Image(file.toURI().toString()));
      }
    });
    gameImageView.setFitHeight(120);
    gameImageView.setFitWidth(180);

    StackPane imagePane = new StackPane();
    imagePane.getChildren().add(gameImageView);

    Header gameNameLabel = new Header(game.getName());

    VBox cardContent = new VBox(5);
    cardContent.setPadding(new Insets(10));
    cardContent.getChildren().add(gameNameLabel);

    this.getChildren().addAll(imagePane, cardContent);
  }
}
