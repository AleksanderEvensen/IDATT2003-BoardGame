package edu.ntnu.idi.idatt.boardgame.ui.javafx.animation;

import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.DieComponent;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * A utility class for animating the rolling of a die in a JavaFX application.
 * <p>
 * This class provides methods to create animations for rolling a die and landing on a final value.
 * </p>
 *
 * @version v1.0.0
 * @since v3.0.0
 */
public class DieComponentAnimator {

  /**
   * Creates an animation to simulate rolling the die and land on the final value.
   *
   * @param die        The DieComponent to animate.
   * @param finalValue The final value (1-6) to set after the animation.
   * @return A Timeline representing the animation.
   */
  public static Timeline animateRoll(DieComponent die, int finalValue) {
    if (finalValue < 1 || finalValue > 6) {
      throw new IllegalArgumentException("Final value must be between 1 and 6");
    }

    Timeline timeline = new Timeline();

    IntStream.range(0, 10).forEach(i -> {
      KeyFrame keyFrame = new KeyFrame(
          Duration.millis(i * 100),
          event -> die.setValue((int) (Math.random() * 6) + 1));
      timeline.getKeyFrames().add(keyFrame);
    });

    KeyFrame finalFrame = new KeyFrame(
        Duration.millis(1000),
        event -> die.setValue(finalValue));
    timeline.getKeyFrames().add(finalFrame);

    return timeline;
  }
}