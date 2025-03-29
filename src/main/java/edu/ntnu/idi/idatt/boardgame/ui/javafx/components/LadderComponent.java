package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.effect.DropShadow;

/**
 * A component representing a ladder on the game board.
 */
public class LadderComponent extends Pane {

  private final Color railColor = Color.SADDLEBROWN;
  private final Color rungColor = Color.BURLYWOOD;

  /**
   * Creates a ladder between two points with specified number of rungs.
   *
   * @param startX The x-coordinate of the start point
   * @param startY The y-coordinate of the start point
   * @param endX The x-coordinate of the end point
   * @param endY The y-coordinate of the end point
   * @param rungs The number of rungs on the ladder
   */
  public LadderComponent(double startX, double startY, double endX, double endY, int rungs) {
    double dx = endX - startX;
    double dy = endY - startY;
    double length = Math.sqrt(dx * dx + dy * dy);


    double perpX = -dy / length;
    double perpY = dx / length;

    double railWidth = 10.0;

    double rail1StartX = startX - perpX * railWidth/2;
    double rail1StartY = startY - perpY * railWidth/2;
    double rail1EndX = endX - perpX * railWidth/2;
    double rail1EndY = endY - perpY * railWidth/2;

    double rail2StartX = startX + perpX * railWidth/2;
    double rail2StartY = startY + perpY * railWidth/2;
    double rail2EndX = endX + perpX * railWidth/2;
    double rail2EndY = endY + perpY * railWidth/2;

    Line rail1 = new Line(rail1StartX, rail1StartY, rail1EndX, rail1EndY);
    Line rail2 = new Line(rail2StartX, rail2StartY, rail2EndX, rail2EndY);

    rail1.setStrokeWidth(3);
    rail2.setStrokeWidth(3);
    rail1.setStroke(railColor);
    rail2.setStroke(railColor);


    this.getChildren().addAll(rail1, rail2);

    // Create and add rungs - include one at start and end positions
    for (int i = 0; i <= rungs; i++) {
      double t = (double) i / rungs;

      // Calculate position along the ladder
      double x1 = rail1StartX + (rail1EndX - rail1StartX) * t;
      double y1 = rail1StartY + (rail1EndY - rail1StartY) * t;
      double x2 = rail2StartX + (rail2EndX - rail2StartX) * t;
      double y2 = rail2StartY + (rail2EndY - rail2StartY) * t;

      Line rung = new Line(x1, y1, x2, y2);
      rung.setStrokeWidth(2);
      rung.setStroke(rungColor);
      this.getChildren().add(rung);
    }

    // Make the ladder not interfere with mouse events on underlying components
    this.setPickOnBounds(false);
    this.setMouseTransparent(true);
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