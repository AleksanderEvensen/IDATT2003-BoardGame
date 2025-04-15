package edu.ntnu.idi.idatt.boardgame.ui.javafx.animation;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;

/**
 * A queue for animations to ensure they play one after another.
 * <p>
 * This class handles playing animations in sequence, avoiding
 * overlapping animations that can cause visual glitches.
 * </p>
 */
public class AnimationQueue {
    private static final Logger logger = Logger.getLogger(AnimationQueue.class.getName());

    private final Queue<QueuedAnimation> animationQueue = new LinkedList<>();
    private boolean isPlaying = false;
    private Animation currentAnimation = null;

    /**
     * Adds an animation to the queue and starts it if nothing is currently playing.
     * This is a convenience method that calls queue with a zero delay.
     *
     * @param animation   the animation to queue
     * @param description a description for logging purposes
     */
    public synchronized void queue(Animation animation, String description) {
        queue(animation, description, 0);
    }

    /**
     * Adds an animation to the queue and starts it if nothing is currently playing.
     *
     * @param animation   the animation to queue
     * @param description a description for logging purposes
     * @param delay       the delay before starting the animation (in milliseconds)
     */
    public synchronized void queue(Animation animation, String description, int delay) {
        QueuedAnimation queuedAnimation = new QueuedAnimation(animation, description);

        queuedAnimation.animation.setDelay(Duration.millis(delay));
        animationQueue.add(queuedAnimation);
        logger.fine("Queued animation: " + description + ", queue size: " + animationQueue.size());

        if (!isPlaying) {
            playNext();
        }
    }

    /**
     * Plays the next animation in the queue if available.
     */
    private synchronized void playNext() {
        if (animationQueue.isEmpty()) {
            isPlaying = false;
            currentAnimation = null;
            return;
        }

        isPlaying = true;
        QueuedAnimation queuedAnimation = animationQueue.poll();
        Animation animation = queuedAnimation.animation;
        currentAnimation = animation;

        logger.fine("Starting animation: " + queuedAnimation.description);
        animation.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus == Status.STOPPED) {
                logger.fine("Animation complete: " + queuedAnimation.description);
                javafx.application.Platform.runLater(() -> {
                    playNext();
                });
            }
        });
        animation.play();
    }

    /**
     * Stops the current animation and clears the queue.
     */
    public synchronized void stopAndClear() {
        if (currentAnimation != null && currentAnimation.getStatus() == Animation.Status.RUNNING) {
            currentAnimation.stop();
        }
        animationQueue.clear();
        isPlaying = false;
        currentAnimation = null;
        logger.info("Animation queue stopped and cleared");
    }

    /**
     * Gets the number of animations currently in the queue.
     *
     * @return the queue size
     */
    public synchronized int getQueueSize() {
        return animationQueue.size();
    }

    /**
     * Combines multiple animations into a single sequential animation.
     *
     * @param animations the animations to combine
     * @return a SequentialTransition containing all animations
     */
    public static Animation combineAnimations(Animation... animations) {
        SequentialTransition sequentialTransition = new SequentialTransition();

        for (Animation animation : animations) {
            if (animation != null) {
                sequentialTransition.getChildren().add(animation);
            }
        }

        return sequentialTransition;
    }

    /**
     * Creates a pause animation of the specified duration.
     *
     * @param milliseconds the duration in milliseconds
     * @return a pause animation
     */
    public static Animation createPause(int milliseconds) {
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.millis(milliseconds));
        return pause;
    }

    /**
     * Private class to associate animations with descriptive information.
     */
    private static class QueuedAnimation {
        final Animation animation;
        final String description;

        QueuedAnimation(Animation animation, String description) {
            this.animation = animation;
            this.description = description;
        }
    }
}
