package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import java.util.List;
import java.util.Objects;

import javafx.scene.image.Image;

/**
 * Represents a die component that can be used in a game.
 * <p>
 * This component can be used to represent a die in a game.
 * </p>
 *
 * @since v2.0.0
 */
public class DieComponent extends javafx.scene.image.ImageView {
    private int value;
    private final List<Image> images;
    private Image currentFace;

    /**
     * Creates a new die component with a default value of 1.
     *
     */
    public DieComponent() {
        this.value = 1;
        this.images = List.of(
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/images/dice/1.png"))),
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/images/dice/2.png"))),
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/images/dice/3.png"))),
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/images/dice/4.png"))),
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/images/dice/5.png"))),
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/images/dice/6.png"))));
        currentFace = images.get(value - 1);
        setImage(currentFace);
        setFitHeight(50);
        setFitWidth(50);
    }

    /**
     * Returns the value of the die.
     *
     * @return the value of the die
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the die.
     *
     * @param value the value of the die
     */
    public void setValue(int value) {
        if (value < 1 || value > 6) {
            throw new IllegalArgumentException("Value must be between 1 and 6");
        }
        this.value = value;
        currentFace = images.get(value - 1);
        setImage(currentFace);
    }
}
