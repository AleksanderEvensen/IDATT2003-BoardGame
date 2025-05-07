package edu.ntnu.idi.idatt.boardgame.ui.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import lombok.Builder;
import lombok.NonNull;

/**
 * A class representing a queueable action that can be executed in a JavaFX timeline.
 * <p>
 *
 * @version v1.0.0
 * @see javafx.animation.Timeline
 * @see AnimationQueue
 * @since v3.0.0
 */
public class QueueableAction {

  private final Timeline timeline;

  @Builder
  private QueueableAction(@NonNull Runnable action, Runnable preAction, Runnable postAction) {

    if (preAction != null) {
      preAction.run();
    }

    KeyFrame mainFrame = new KeyFrame(Duration.millis(1), e -> action.run());

    timeline = new Timeline(mainFrame);

    if (postAction != null) {
      timeline.setOnFinished(e -> postAction.run());
    }
  }

  /**
   * Returns the underlying {@link Timeline}.
   */
  public Timeline timeline() {
    return timeline;
  }
}
