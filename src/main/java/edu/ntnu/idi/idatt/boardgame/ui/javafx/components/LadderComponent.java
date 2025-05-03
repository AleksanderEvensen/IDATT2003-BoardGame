package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * A component representing a ladder on the game board.
 *
 * @since v2.0.0
 */
public class LadderComponent extends Pane {

  private final Color railColor = Color.BLACK;
  private final Color rungColor = Color.BURLYWOOD;

  private double startX, startY, endX, endY;
  private final int rungs;

  /**
   * Creates a ladder between two points with specified number of rungs.
   *
   * @param startX The x-coordinate of the start point
   * @param startY The y-coordinate of the start point
   * @param endX   The x-coordinate of the end point
   * @param endY   The y-coordinate of the end point
   * @param rungs  The number of rungs on the ladder
   */
  public LadderComponent(double startX, double startY, double endX, double endY, int rungs) {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    this.rungs = rungs;

    drawLadder();
    this.setPickOnBounds(false);
    this.setMouseTransparent(true);
  }

  /**
   * Updates the ladder's coordinates and redraws it.
   *
   * @param startX The new x-coordinate of the start point
   * @param startY The new y-coordinate of the start point
   * @param endX   The new x-coordinate of the end point
   * @param endY   The new y-coordinate of the end point
   */
  public void updateCoordinates(double startX, double startY, double endX, double endY) {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    redraw();
  }

  /**
   * Redraws the ladder based on the current dimensions.
   */
  private void redraw() {
    this.getChildren().clear();
    drawLadder();
  }

  /**
   * Draws the ladder using the current parameters.
   */
  private void drawLadder() {
    double dx = endX - startX;
    double dy = endY - startY;
    double length = Math.sqrt(dx * dx + dy * dy);

    double perpX = -dy / length;
    double perpY = dx / length;

    double railWidth = 40.0;

    double rail1StartX = startX - perpX * railWidth / 2;
    double rail1StartY = startY - perpY * railWidth / 2;
    double rail1EndX = endX - perpX * railWidth / 2;
    double rail1EndY = endY - perpY * railWidth / 2;

    double rail2StartX = startX + perpX * railWidth / 2;
    double rail2StartY = startY + perpY * railWidth / 2;
    double rail2EndX = endX + perpX * railWidth / 2;
    double rail2EndY = endY + perpY * railWidth / 2;

    Line rail1 = new Line(rail1StartX, rail1StartY, rail1EndX, rail1EndY);
    Line rail2 = new Line(rail2StartX, rail2StartY, rail2EndX, rail2EndY);

    rail1.setStrokeWidth(6);
    rail2.setStrokeWidth(6);
    rail1.setStroke(railColor);
    rail2.setStroke(railColor);

    this.getChildren().addAll(rail1, rail2);

    for (int i = 0; i <= rungs; i++) {
      double t = (double) i / rungs;

      double x1 = rail1StartX + (rail1EndX - rail1StartX) * t;
      double y1 = rail1StartY + (rail1EndY - rail1StartY) * t;
      double x2 = rail2StartX + (rail2EndX - rail2StartX) * t;
      double y2 = rail2StartY + (rail2EndY - rail2StartY) * t;

      Line rung = new Line(x1, y1, x2, y2);
      rung.setStrokeWidth(5);
      rung.setStroke(rungColor);
      this.getChildren().add(rung);
    }
  }

  /**
   * Set the color of the ladder rails.
   *
   * @param color The color to use for the rails
   */
  public void setRailColor(Color color) {
    for (int i = 0; i < 2; i++) {
      if (getChildren().get(i) instanceof Line) {
        ((Line) getChildren().get(i)).setStroke(color);
      }
    }
  }

  /**
   * Set the color of the ladder rungs.
   *
   * @param color The color to use for the rungs
   */
  public void setRungColor(Color color) {
    for (int i = 2; i < getChildren().size(); i++) {
      if (getChildren().get(i) instanceof Line) {
        ((Line) getChildren().get(i)).setStroke(color);
      }
    }
  }

  /**
   * Set the width of the ladder rails.
   *
   * @param width The stroke width to use for the rails
   */
  public void setRailWidth(double width) {
    for (int i = 0; i < 2; i++) {
      if (getChildren().get(i) instanceof Line) {
        ((Line) getChildren().get(i)).setStrokeWidth(width);
      }
    }
  }

  /**
   * Set the width of the ladder rungs.
   *
   * @param width The stroke width to use for the rungs
   */
  public void setRungWidth(double width) {
    for (int i = 2; i < getChildren().size(); i++) {
      if (getChildren().get(i) instanceof Line) {
        ((Line) getChildren().get(i)).setStrokeWidth(width);
      }
    }
  }
}