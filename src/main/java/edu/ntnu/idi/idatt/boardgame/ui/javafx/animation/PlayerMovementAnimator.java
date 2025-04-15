package edu.ntnu.idi.idatt.boardgame.ui.javafx.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.PlayerBlipView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * Creates smooth animations for player movement on the game board.
 * <p>
 * This class provides methods to animate player blips moving along paths,
 * following tile connections, and special animations for game events like
 * ladders.
 * </p>
 */
public class PlayerMovementAnimator {
    private static final Logger logger = Logger.getLogger(PlayerMovementAnimator.class.getName());
    private static final int DEFAULT_ANIMATION_DURATION = 500;
    private static final int DIRECT_ANIMATION_DURATION = 300;

    /**
     * Creates an animation that moves a player along the path from start to end
     * tile.
     * This method has been enhanced to ensure consistent animation, even when
     * rapid animation requests occur.
     *
     * @param gameBoard the game board containing the tiles
     * @param player    the player to animate
     * @param startTile the starting tile (can be null if unknown)
     * @param endTile   the destination tile
     * @param duration  the maximum duration for the animation
     * @return an animation that can be played
     */
    public static Animation createPathAnimation(GameBoard gameBoard, Player player,
            Tile startTile, Tile endTile, int duration) {
        PlayerBlipView blipView = gameBoard.getPlayerBlipView(player);
        if (blipView == null) {
            logger.warning("No blip view found for player: " + player.getName());
            return new Timeline();
        }

        if (startTile == null || startTile.equals(endTile)) {
            // Handle direct placement to a tile (no animation needed)
            Point2D endPos = getTileCenter(gameBoard, endTile);
            if (endPos != null) {
                blipView.setLayoutX(endPos.getX() - blipView.getPrefWidth() / 2);
                blipView.setLayoutY(endPos.getY() - blipView.getPrefHeight() / 2);
            }
            return new Timeline();
        }

        // Calculate path points
        List<Point2D> pathPoints = new ArrayList<>();

        // Add the start point
        Point2D startPoint = getTileCenter(gameBoard, startTile);
        if (startPoint == null) {
            // Fall back to current position if we can't find the start tile
            startPoint = new Point2D(blipView.getLayoutX() + blipView.getPrefWidth() / 2,
                    blipView.getLayoutY() + blipView.getPrefHeight() / 2);
        }
        pathPoints.add(startPoint);

        // Try to find a path via next/previous tiles
        List<Tile> tilePath = calculateTilePath(startTile, endTile);
        for (int i = 1; i < tilePath.size(); i++) {
            // Skip the first tile since it's already added as the start point
            Point2D point = getTileCenter(gameBoard, tilePath.get(i));
            if (point != null) {
                pathPoints.add(point);
            }
        }

        // If we couldn't find a path with tiles, add the end point directly
        if (pathPoints.size() == 1) {
            Point2D endPoint = getTileCenter(gameBoard, endTile);
            if (endPoint != null) {
                pathPoints.add(endPoint);
            }
        }

        // Get final destination point (for correct final positioning)
        final Point2D finalDestination = pathPoints.get(pathPoints.size() - 1);

        // Create path for animation
        Path path = createPath(pathPoints, blipView);

        // Create a sequential animation that includes the path animation
        // followed by explicit final positioning
        PathTransition pathTransition = new PathTransition();
        pathTransition.setPath(path);
        pathTransition.setNode(blipView);
        pathTransition.setDuration(Duration.millis(duration));
        pathTransition.setOrientation(PathTransition.OrientationType.NONE);

        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(false);

        // Create a timeline that explicitly sets the final position when the animation
        // completes
        Timeline positionFixer = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            blipView.setTranslateX(0);
            blipView.setTranslateY(0);

            double finalX = finalDestination.getX() - blipView.getPrefWidth() / 2;
            double finalY = finalDestination.getY() - blipView.getPrefHeight() / 2;
            blipView.setLayoutX(finalX);
            blipView.setLayoutY(finalY);

            logger.fine("Final position set for " + player.getName() +
                    " at tile " + endTile.getTileId() +
                    " (x=" + finalX + ", y=" + finalY + ")");
        }));

        // Combine the animations

      return new SequentialTransition(
              pathTransition,
              positionFixer);
    }

    /**
     * Creates an animation for ladder movement.
     * This method has been enhanced to ensure consistent animation, even when
     * rapid animation requests occur.
     *
     * @param gameBoard the game board
     * @param player    the player to animate
     * @param fromTile  the starting tile
     * @param toTile    the ending tile
     * @param duration  the animation duration
     * @return an animation that can be played
     */
    public static Animation createLadderAnimation(GameBoard gameBoard, Player player,
            Tile fromTile, Tile toTile, int duration) {
        PlayerBlipView blipView = gameBoard.getPlayerBlipView(player);
        if (blipView == null) {
            logger.warning("No blip view found for player: " + player.getName());
            return new Timeline();
        }

        Point2D start = getTileCenter(gameBoard, fromTile);
        Point2D end = getTileCenter(gameBoard, toTile);

        if (start == null || end == null) {
            logger.warning("Could not determine ladder animation points");
            return new Timeline();
        }

        // Instead of animating with a path, we'll use a timeline to directly set
        // positions
        // This avoids coordinate space conversion issues that cause the offsets

        // Calculate the center of the player blip
        final double blipCenterX = blipView.getPrefWidth() / 2;
        final double blipCenterY = blipView.getPrefHeight() / 2;

        // Get current position (starting position for the animation)
        final double startX = start.getX() - blipCenterX;
        final double startY = start.getY() - blipCenterY;

        // Get end position
        final double endX = end.getX() - blipCenterX;
        final double endY = end.getY() - blipCenterY;

        boolean isGoingUp = end.getY() < start.getY();

        double distance = start.distance(end);
        int adjustedDuration = (int) (duration * Math.max(0.8, Math.min(1.5, distance / 300)));

        // Create timeline animation
        Timeline ladderAnimation = new Timeline();

        // Create keyframes for the animation
        int steps = 30;
        double arcHeight = isGoingUp ? -50 : 50;

        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double x = startX + (endX - startX) * t;

            // Add a sine curve to the y movement to create a curved path
            double sineCurve = Math.sin(t * Math.PI) * arcHeight;
            double y = startY + (endY - startY) * t + sineCurve;

            // Create keyframe
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(adjustedDuration * t),
                    event -> {
                    },
                    new javafx.animation.KeyValue(
                            blipView.layoutXProperty(),
                            x,
                            javafx.animation.Interpolator.EASE_BOTH),
                    new javafx.animation.KeyValue(
                            blipView.layoutYProperty(),
                            y,
                            javafx.animation.Interpolator.EASE_BOTH));

            ladderAnimation.getKeyFrames().add(keyFrame);
        }

        ScaleTransition growEffect = new ScaleTransition(Duration.millis(120), blipView);
        growEffect.setFromX(1.0);
        growEffect.setFromY(1.0);
        growEffect.setToX(1.4);
        growEffect.setToY(1.4);

        ScaleTransition shrinkEffect = new ScaleTransition(Duration.millis(100), blipView);
        shrinkEffect.setFromX(1.4);
        shrinkEffect.setFromY(1.4);
        shrinkEffect.setToX(1.0);
        shrinkEffect.setToY(1.0);

        Timeline finalPositionFixer = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            blipView.setLayoutX(endX);
            blipView.setLayoutY(endY);

            blipView.setTranslateX(0);
            blipView.setTranslateY(0);

            logger.fine("Final ladder position set for " + player.getName() +
                    " at tile " + toTile.getTileId() +
                    " (x=" + endX + ", y=" + endY + ")");
        }));

      return new SequentialTransition(
              ladderAnimation,
              finalPositionFixer,
              growEffect,
              shrinkEffect);
    }

    /**
     * Calculates a path of tiles from start to end.
     *
     * @param startTile the starting tile
     * @param endTile   the destination tile
     * @return list of tiles forming a path
     */
    private static List<Tile> calculateTilePath(Tile startTile, Tile endTile) {
        List<Tile> path = new ArrayList<>();
        path.add(startTile);

        // If tiles are the same, just return the single tile
        if (startTile.equals(endTile)) {
            return path;
        }

        // Try to find forward path
        Tile current = startTile;
        boolean foundPath = false;

        while (!current.equals(endTile)) {
            Optional<Tile> next = current.getNextTile();
            if (next.isEmpty()) {
                break;
            }

            current = next.get();
            path.add(current);

            if (current.equals(endTile)) {
                foundPath = true;
                break;
            }
        }

        // If forward path failed, try backward path
        if (!foundPath) {
            path.clear();
            path.add(startTile);
            current = startTile;

            while (!current.equals(endTile)) {
                Optional<Tile> prev = current.getPreviousTile();
                if (prev.isEmpty()) {
                    break;
                }

                current = prev.get();
                path.add(current);

                if (current.equals(endTile)) {
                    foundPath = true;
                    break;
                }
            }
        }

        // If we still couldn't find a path, just use start->end directly
        if (!foundPath) {
            path.clear();
            path.add(startTile);
            path.add(endTile);
        }

        return path;
    }

    /**
     * Gets the center point of a tile in screen coordinates.
     *
     * @param gameBoard the game board
     * @param tile      the tile to get the center of
     * @return the center point or null if tile not found
     */
    public static Point2D getTileCenter(GameBoard gameBoard, Tile tile) {
        var tileComponent = gameBoard.getTileComponent(tile.getTileId());
        if (tileComponent == null) {
            logger.warning("Tile component not found for tile: " + tile.getTileId());
            return null;
        }

        // Get bounds in scene coordinates
        Bounds tileBounds = tileComponent.localToScene(tileComponent.getBoundsInLocal());

        // Convert scene coordinates to overlay coordinates
        var overlayPane = gameBoard.getOverlayPane();
        Bounds overlayBounds = overlayPane.sceneToLocal(tileBounds);

        // Return center point
        return new Point2D(
                overlayBounds.getMinX() + overlayBounds.getWidth() / 2,
                overlayBounds.getMinY() + overlayBounds.getHeight() / 2);
    }

    /**
     * Creates a path from a list of points.
     *
     * @param points   the list of points to create a path through
     * @param blipView the player blip to be animated
     * @return a path object for animation
     */
    private static Path createPath(List<Point2D> points, PlayerBlipView blipView) {
        if (points.isEmpty()) {
            return new Path();
        }

        Path path = new Path();

        // First point is a move
        Point2D first = points.get(0);
        path.getElements().add(new MoveTo(
                first.getX() - blipView.getLayoutX(),
                first.getY() - blipView.getLayoutY()));

        // Add line segments to remaining points
        for (int i = 1; i < points.size(); i++) {
            Point2D point = points.get(i);
            path.getElements().add(new LineTo(
                    point.getX() - blipView.getLayoutX(),
                    point.getY() - blipView.getLayoutY()));
        }

        return path;
    }
}
