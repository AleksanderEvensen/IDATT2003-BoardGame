package edu.ntnu.idi.idatt.boardgame.javafx.animation;

import edu.ntnu.idi.idatt.boardgame.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.javafx.components.PlayerBlipView;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;
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
 * This class provides methods to animate player blips moving along paths, following tile
 * connections, and special animations for game events like ladders.
 * </p>
 *
 * @version v1.0.0
 * @since v3.0.0
 */
public class PlayerMovementAnimator {

  private static final Logger logger = Logger.getLogger(PlayerMovementAnimator.class.getName());
  private static final int DEFAULT_ANIMATION_DURATION = 100;
  private static final double LADDER_DURATION_MODIFIER = 1.5;
  private static final int DIRECT_ANIMATION_DURATION = 300;

  public static Animation createPathAnimation(GameBoard gameBoard, Player player, Tile startTile,
      Tile endTile) {
    PlayerBlipView blipView = gameBoard.getPlayerBlipView(player);
    if (blipView == null) {
      logger.warning("No blip view found for player: " + player.getName());
      return new Timeline();
    }

    if (startTile == null || startTile.equals(endTile)) {
      Point2D endPos = getTileCenter(gameBoard, endTile);
      if (endPos != null) {
        blipView.setLayoutX(endPos.getX() - blipView.getPrefWidth() / 2);
        blipView.setLayoutY(endPos.getY() - blipView.getPrefHeight() / 2);
      }
      return new Timeline();
    }

    List<Point2D> pathPoints = new ArrayList<>();
    Point2D startPoint = getTileCenter(gameBoard, startTile);
    if (startPoint == null) {
      startPoint = new Point2D(blipView.getLayoutX() + blipView.getPrefWidth() / 2,
          blipView.getLayoutY() + blipView.getPrefHeight() / 2);
    }
    pathPoints.add(startPoint);

    List<Tile> tilePath = calculateTilePath(startTile, endTile);

    for (int i = 1; i < tilePath.size(); i++) {
      Point2D point = getTileCenter(gameBoard, tilePath.get(i));
      if (point != null) {
        pathPoints.add(point);
      }
    }

    if (pathPoints.size() == 1) {
      Point2D endPoint = getTileCenter(gameBoard, endTile);
      if (endPoint != null) {
        pathPoints.add(endPoint);
      }
    }

    final Point2D finalDestination = pathPoints.getLast();

    Path path = createPath(pathPoints, blipView);

    PathTransition pathTransition = new PathTransition();
    pathTransition.setPath(path);
    pathTransition.setNode(blipView);
    pathTransition.setDuration(
        pathPoints.size() > 1 ? Duration.millis(DEFAULT_ANIMATION_DURATION * pathPoints.size())
            : Duration.millis(DIRECT_ANIMATION_DURATION));
    pathTransition.setCycleCount(1);
    pathTransition.setAutoReverse(false);

    Timeline positionFixer = new Timeline(new KeyFrame(Duration.millis(1), event -> {
      blipView.setTranslateX(0);
      blipView.setTranslateY(0);
      double finalX = finalDestination.getX() - blipView.getPrefWidth() / 2;
      double finalY = finalDestination.getY() - blipView.getPrefHeight() / 2;
      blipView.setLayoutX(finalX);
      blipView.setLayoutY(finalY);
    }));
    return new SequentialTransition(pathTransition, positionFixer);
  }

  public static Animation createLadderAnimation(GameBoard gameBoard, Player player, Tile fromTile,
      Tile toTile) {
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

    final double blipCenterX = blipView.getPrefWidth() / 2;
    final double blipCenterY = blipView.getPrefHeight() / 2;

    final double startX = start.getX() - blipCenterX;
    final double startY = start.getY() - blipCenterY;
    final double endX = end.getX() - blipCenterX;
    final double endY = end.getY() - blipCenterY;

    boolean isGoingUp = end.getY() < start.getY();
    double distance = start.distance(end);
    double adjustedDuration = (distance * LADDER_DURATION_MODIFIER);

    Timeline ladderAnimation = new Timeline();
    int steps = 30;
    double arcHeight = isGoingUp ? -50 : 50;

    for (int i = 0; i <= steps; i++) {
      double t = (double) i / steps;
      double x = startX + (endX - startX) * t;
      double sineCurve = Math.sin(t * Math.PI) * arcHeight;
      double y = startY + (endY - startY) * t + sineCurve;

      KeyFrame keyFrame = new KeyFrame(Duration.millis(adjustedDuration * t),
          new javafx.animation.KeyValue(blipView.layoutXProperty(), x),
          new javafx.animation.KeyValue(blipView.layoutYProperty(), y));
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
    }));

    return new SequentialTransition(growEffect, ladderAnimation, finalPositionFixer, shrinkEffect);
  }

  private static List<Tile> calculateTilePath(Tile startTile, Tile endTile) {
    List<Tile> path = new ArrayList<>();
    path.add(startTile);

    if (startTile.equals(endTile)) {
      return path;
    }

    Tile currentTile = startTile;
    boolean pathFound = false;

    // Determine the search direction based on tile IDs
    boolean searchForward = endTile.getTileId() > startTile.getTileId();

    while (!pathFound) {
      Optional<Tile> nextTile =
          searchForward ? currentTile.getNextTile() : currentTile.getPreviousTile();
      if (nextTile.isPresent() && !path.contains(nextTile.get())) {
        currentTile = nextTile.get();
        path.add(currentTile);
        if (currentTile.equals(endTile)) {
          pathFound = true;
        }
      } else {
        break;
      }
    }

    if (!pathFound) {
      path.clear();
      path.add(startTile);
      path.add(endTile);
    }

    return path;
  }

  public static Point2D getTileCenter(GameBoard gameBoard, Tile tile) {
    var tileComponent = gameBoard.getTileComponent(tile.getTileId());
    if (tileComponent == null) {
      logger.warning("Tile component not found for tile: " + tile.getTileId());
      return null;
    }

    Bounds tileBounds = tileComponent.localToScene(tileComponent.getBoundsInLocal());
    var overlayPane = gameBoard.getOverlayPane();
    Bounds overlayBounds = overlayPane.sceneToLocal(tileBounds);

    return new Point2D(overlayBounds.getMinX() + overlayBounds.getWidth() / 2,
        overlayBounds.getMinY() + overlayBounds.getHeight() / 2);
  }

  private static Path createPath(List<Point2D> points, PlayerBlipView blipView) {
    Path path = new Path();

    if (!points.isEmpty()) {
      path.getElements().add(new MoveTo(points.getFirst().getX() - blipView.getLayoutX(),
          points.getFirst().getY() - blipView.getLayoutY()));

      IntStream.range(1, points.size()).mapToObj(
          i -> new LineTo(points.get(i).getX() - blipView.getLayoutX(),
              points.get(i).getY() - blipView.getLayoutY())).forEach(path.getElements()::add);
    }

    return path;
  }
}