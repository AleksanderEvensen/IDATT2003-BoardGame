package edu.ntnu.idi.idatt.boardgame.javafx.style;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.javafx.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.javafx.components.LadderComponent;
import edu.ntnu.idi.idatt.boardgame.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.style.LadderTileStyle;
import edu.ntnu.idi.idatt.boardgame.style.TileStyleApplier;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.paint.Color;

/**
 * JavaFX-specific implementation of the ladder tile style applier. This class applies visual
 * styling to tiles with ladder actions and draws the ladder between them.
 *
 * @version v1.0.0
 * @see TileStyleApplier
 * @since v2.0.0
 */
public class JavaFxLadderStyleApplier implements TileStyleApplier {


  @Override
  public void applyStyle(Tile tile, TileAction action, Object parent) {
    if (!(parent instanceof GameBoard gameBoard)
        || !(action instanceof LadderAction ladderAction)) {
      return;
    }

    TileComponent startTile = findTileComponent(gameBoard, tile.getTileId());
    TileComponent destinationTile =
        findTileComponent(gameBoard, ladderAction.getDestinationTile().getTileId());

    if (startTile == null || destinationTile == null) {
      System.err.println("Could not find tile components for ladder from " +
          tile.getTileId() + " to " + ladderAction.getDestinationTile().getTileId());
      return;
    }

    boolean isPositive = ladderAction.getDestinationTile().getTileId() > tile.getTileId();
    applyTileColors(startTile, destinationTile, isPositive);

    Platform.runLater(() -> {
      gameBoard.applyCss();
      gameBoard.layout();

      List<Double> startCoords = LadderUtils.calculateTileCenter(gameBoard, startTile);
      List<Double> endCoords = LadderUtils.calculateTileCenter(gameBoard, destinationTile);

      Double startX = startCoords.getFirst();
      Double startY = startCoords.get(1);
      Double endX = endCoords.get(0);
      Double endY = endCoords.get(1);

      double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
      int rungs = LadderUtils.calculateRungCount(distance);

      LadderComponent ladderComponent = new LadderComponent(startX, startY, endX, endY, rungs);
      gameBoard.addLadderComponent(startTile, ladderComponent);
    });
  }


  /**
   * Finds a TileComponent by its ID in the GameBoard.
   *
   * @param gameBoard The game board containing the tiles
   * @param tileId    The ID of the tile to find
   * @return The found TileComponent, or null if not found
   */
  private TileComponent findTileComponent(GameBoard gameBoard, int tileId) {
    return gameBoard.getChildren().stream()
        .filter(node -> node instanceof TileComponent)
        .map(node -> (TileComponent) node)
        .filter(tileComponent -> tileComponent.getTileId() == tileId)
        .findFirst()
        .orElse(null);
  }

  /**
   * Applies appropriate colors to start and destination tiles based on ladder direction.
   *
   * @param startTile       The starting tile
   * @param destinationTile The destination tile
   * @param isPositive      Whether the ladder goes up (true) or down (false)
   */
  private void applyTileColors(TileComponent startTile, TileComponent destinationTile,
      boolean isPositive) {
    if (isPositive) {
      startTile.setBackgroundColor(Color.valueOf(LadderTileStyle.START_POSITIVE_COLOR));
      destinationTile.setBackgroundColor(Color.valueOf(LadderTileStyle.POSITIVE_COLOR));
    } else {
      startTile.setBackgroundColor(Color.valueOf(LadderTileStyle.START_NEGATIVE_COLOR));
      destinationTile.setBackgroundColor(Color.valueOf(LadderTileStyle.NEGATIVE_COLOR));
    }
  }


}