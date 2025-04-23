package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import edu.ntnu.idi.idatt.boardgame.ui.javafx.player.JavaFXPlayer;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * A visual representation of a player's position on the game board.
 * <p>
 * This component combines a colored background circle with a pawn image
 * to represent the player's game piece.
 * </p>
 */
public class PlayerBlipView extends StackPane {

    private final JavaFXPlayer player;
    private final Circle background;
    private final ImageView pawnImageView;

    /**
     * Creates a visual blip for the given player.
     *
     * @param player the JavaFX player to represent
     */
    public PlayerBlipView(JavaFXPlayer player) {
        this.player = player;

        setPrefWidth(30);
        setPrefHeight(30);

        background = new Circle(15);
        background.setFill(player.getColor());
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(1.5);

        pawnImageView = new ImageView(player.getPawnImage());
        pawnImageView.setFitHeight(20);
        pawnImageView.setFitWidth(20);
        pawnImageView.setPreserveRatio(true);

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
        this.getChildren().addAll(background, pawnImageView);
    }

    /**
     * Updates the visual appearance of the blip.
     * Call this if player's color or pawn image changes.
     */
    public void updateAppearance() {
        background.setFill(player.getColor());
        pawnImageView.setImage(player.getPawnImage());
    }

    /**
     * Gets the associated player.
     *
     * @return the player
     */
    public JavaFXPlayer getPlayer() {
        return player;
    }
}
