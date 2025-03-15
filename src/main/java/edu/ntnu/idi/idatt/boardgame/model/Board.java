package edu.ntnu.idi.idatt.boardgame.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a board with a fixed row and column size. Each Tile must be placed
 * within the valid range when added. The board stores tiles by their tileId.
 */
public class Board {

    private final int rowCount;
    private final int colCount;
    private final Map<Integer, Tile> tiles = new HashMap<>();

    /**
     * Creates a Board with a specified number of rows and columns.
     * @param rowCount the total number of rows
     * @param colCount the total number of columns
     */
    public Board(int rowCount, int colCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
    }

    /**
     * Adds a tile to the board if its (row, col) is within range.
     * @param tile the tile to add
     */
    public void addTile(Tile tile) {
        if (tile.getRow() < 0 || tile.getRow() >= rowCount
            || tile.getCol() < 0 || tile.getCol() >= colCount) {
            throw new IllegalArgumentException(
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
     * @param tileId the tile ID
     * @return the Tile object, or null if not found
     */
    public Tile getTile(int tileId) {
        return tiles.get(tileId);
    }

    /**
     * Returns all tiles stored on the board.
     * @return a Map of tileId to Tile
     */
    public Map<Integer, Tile> getTiles() {
        return tiles;
    }

    /**
     * Returns the number of rows in the board.
     * @return the row count
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Returns the number of columns in the board.
     * @return the column count
     */
    public int getColCount() {
        return colCount;
    }

}
