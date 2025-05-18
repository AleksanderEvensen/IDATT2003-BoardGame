package edu.ntnu.idi.idatt.boardgame.ui.javafx.style;

import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.TileComponent;
import java.util.List;
import javafx.geometry.Bounds;

/**
 * Utility class for ladder-related calculations and coordinate extraction.
 */
public class LadderUtils {

  private static final int DEFAULT_RUNGS_PER_100_PIXELS = 3;
  private static final double MIN_RUNGS = 2;
  private static final double MAX_RUNGS = 10;

  /**
   * Calculates the center coordinates of a tile in the overlay pane.
   *
   * @param gameBoard     The game board containing the overlay pane
   * @param tileComponent The tile component to calculate coordinates for
   * @return A double array containing the x and y coordinates [x, y]
   */
  public static List<Double> calculateTileCenter(GameBoard gameBoard, TileComponent tileComponent) {
    Bounds tileBounds = tileComponent.localToScene(tileComponent.getBoundsInLocal());
    Bounds overlayBounds = gameBoard.getOverlayPane().sceneToLocal(tileBounds);

    double centerX = overlayBounds.getMinX() + overlayBounds.getWidth() / 2;
    double centerY = overlayBounds.getMinY() + overlayBounds.getHeight() / 2;

    return List.of(centerX, centerY);
  }

  /**
   * Calculates an appropriate number of rungs based on ladder length.
   *
   * @param distance The distance between the start and end points
   * @return The number of rungs to draw
   */
  public static int calculateRungCount(double distance) {
    int calculatedRungs = (int) (distance * DEFAULT_RUNGS_PER_100_PIXELS / 100);
    return (int) Math.max(MIN_RUNGS, Math.min(calculatedRungs, MAX_RUNGS));
  }
}