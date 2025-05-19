package edu.ntnu.idi.idatt.boardgame.javafx.components;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A visual representation of a player's position on the game board.
 * <p>
 * This component combines a colored background circle with a pawn image to represent the player's
 * game piece.
 * </p>
 */
public class PlayerBlipView extends StackPane {

  private final Player player;
  private final Circle background;

  /**
   * Creates a visual blip for the given player.
   *
   * @param player the JavaFX player to represent
   */
  public PlayerBlipView(Player player) {
    this.player = player;

    setPrefWidth(30);
    setPrefHeight(30);

    background = new Circle(15);

    background.setFill(Color.web(player.getColor().toHex()));
    background.setStroke(Color.BLACK);
    background.setStrokeWidth(1.5);

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(3.0);
    dropShadow.setOffsetX(1.0);
    dropShadow.setOffsetY(1.0);
    dropShadow.setColor(Color.color(0, 0, 0, 0.3));
    this.setEffect(dropShadow);

    this.setOnMouseEntered(e -> {
      background.setStrokeWidth(2.5);
      background.setStroke(Color.GOLD);
    });

    this.setOnMouseExited(e -> {
      background.setStrokeWidth(1.5);
      background.setStroke(Color.BLACK);
    });
    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(player.getName());
    javafx.scene.control.Tooltip.install(this, tooltip);
    this.getChildren().addAll(background);
  }

  /**
   * Updates the visual appearance of the blip. Call this if player's color or pawn image changes.
   */
  public void updateAppearance() {
    background.setFill(Color.web(player.getColor().toHex()));
  }

  /**
   * Gets the associated player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }
}
