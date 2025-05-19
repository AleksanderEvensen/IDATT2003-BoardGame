package edu.ntnu.idi.idatt.boardgame.model;

import edu.ntnu.idi.idatt.boardgame.actions.HasTileReferenceResolver;
import edu.ntnu.idi.idatt.boardgame.model.tiles.BoardRangeException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Represents a board with a fixed row and column size. Each Tile must be placed within the valid
 * range when added. The board stores tiles by their tileId.
 */
public class Board implements HasTileReferenceResolver {

  @Getter
  private final int rowCount;
  @Getter
  private final int colCount;
  @Getter
  private final Map<Integer, Tile> tiles = new HashMap<>();

  /**
   * Creates a Board with a specified number of rows and columns.
   *
   * @param rowCount the total number of rows
   * @param colCount the total number of columns
   */
  public Board(int rowCount, int colCount) {
    this.rowCount = rowCount;
    this.colCount = colCount;
  }

  /**
   * Adds a tile to the board if its (row, col) is within range.
   *
   * @param tile the tile to add
   */
  public void addTile(Tile tile) {
    if (tile.getRow() < 0 || tile.getRow() >= rowCount
        || tile.getCol() < 0 || tile.getCol() >= colCount) {
      throw new BoardRangeException(
          "Invalid tile location (row=" + tile.getRow()
              + ", col=" + tile.getCol() + ")."
              + " Must be within 0 <= row < " + rowCount
              + " and 0 <= col < " + colCount + "."
      );
    }
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Retrieves a tile by its tileId.
   *
   * @param tileId the tile ID
   * @return the Tile object, or null if not found
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  @Override
  public void resolveReferences(Board board) {
    tiles.values().forEach(tile -> tile.resolveReferences(this));
  }
}
