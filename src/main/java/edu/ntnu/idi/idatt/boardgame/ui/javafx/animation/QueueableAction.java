package edu.ntnu.idi.idatt.boardgame.ui.javafx.animation;

import java.util.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * <p>
 * A class representing a queueable action that can be executed in a JavaFX timeline.
 *
 * @version v1.0.0
 * @see javafx.animation.Timeline
 * @see AnimationQueue
 * @since v3.0.0
 */
public class QueueableAction {

  private final Timeline timeline;

  private QueueableAction(Runnable action, Runnable preAction, Runnable postAction) {
    Objects.requireNonNull(action, "Action cannot be null");

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
   * Creates a new builder instance for {@link QueueableAction}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Returns the underlying {@link Timeline}.
   */
  public Timeline timeline() {
    return timeline;
  }

  /**
   * Builder class for {@link QueueableAction}.
   */
  public static class Builder {

    private Runnable action;
    private Runnable preAction;
    private Runnable postAction;

    /**
     * Sets the action to be executed.
     *
     * @param action action to be executed
     * @return this
     */
    public Builder action(Runnable action) {
      this.action = action;
      return this;
    }

    /**
     * Sets the pre-action to be executed before the main action.
     *
     * @param preAction action that runs before the main action
     * @return this
     */
    public Builder preAction(Runnable preAction) {
      this.preAction = preAction;
      return this;
    }


    /**
     * Sets the post-action to be executed after the main action.
     *
     * @param postAction action that runs after the main action
     * @return this
     */
    public Builder postAction(Runnable postAction) {
      this.postAction = postAction;
      return this;
    }

    /**
     * Builds the {@link QueueableAction} instance.
     *
     * @return the built QueueableAction
     */
    public QueueableAction build() {
      return new QueueableAction(action, preAction, postAction);
    }
  }
}