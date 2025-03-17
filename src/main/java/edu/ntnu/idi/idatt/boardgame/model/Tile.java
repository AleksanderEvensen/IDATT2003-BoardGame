package edu.ntnu.idi.idatt.boardgame.model;

import edu.ntnu.idi.idatt.boardgame.actions.HasTileReferenceResolver;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;

import java.io.Serializable;
import java.util.Optional;

/**
 * A class representing a tile on the playing board, with an optional grid location.
 * <p>
 * Each tile can have an action associated with it, and references to the next, previous, and last tiles.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
 */
public class Tile implements Serializable, HasTileReferenceResolver {

    private final int tileId;
    private int row;
    private int col;
    private TileAction action;
    private int previousTileId;
    private int nextTileId;
    private int lastTileId;
    private transient Tile previousTile;
    private transient Tile nextTile;
    private transient Tile lastTile;

    /**
     * Creates a tile with only an ID.
     *
     * @param tileId the tile ID
     */
    public Tile(int tileId) {
        this.tileId = tileId;
    }

    /**
     * Creates a tile with an ID and a grid position.
     *
     * @param tileId the tile ID
     * @param row the row position
     * @param col the column position
     */
    public Tile(int tileId, int row, int col) {
        this.tileId = tileId;
        this.row = row;
        this.col = col;
    }

    /**
     * Creates a tile with an ID and an action.
     *
     * @param tileId the tile ID
     * @param action the tile action
     * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
     */
    public Tile(int tileId, TileAction action) {
        this(tileId);
        this.action = action;
    }

    /**
     * Creates a tile with an ID, a grid position, and an action.
     *
     * @param tileId the tile ID
     * @param row the row position
     * @param col the column position
     * @param action the tile action
     * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
     */
    public Tile(int tileId, int row, int col, TileAction action) {
        this(tileId, row, col);
        this.action = action;
    }

    /**
     * Creates a tile with an ID, action, and a reference to the next tile.
     *
     * @param tileId the tile ID
     * @param action the tile action
     * @param nextTile the next tile
     * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
     */
    public Tile(int tileId, TileAction action, Tile nextTile) {
        this(tileId, action);
        this.nextTile = nextTile;
    }

    /**
     * Creates a tile with an ID, grid position, action, and a reference to the next tile.
     *
     * @param tileId the tile ID
     * @param row the row position
     * @param col the column position
     * @param action the tile action
     * @param nextTile the next tile
     * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
     */
    public Tile(int tileId, int row, int col, TileAction action, Tile nextTile) {
        this(tileId, row, col, action);
        this.nextTile = nextTile;
    }

    /**
     * Creates a tile with an ID, action, next tile, and last tile.
     *
     * @param tileId the tile ID
     * @param action the tile action
     * @param nextTile the next tile
     * @param lastTile the last tile
     * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
     */
    public Tile(int tileId, TileAction action, Tile nextTile, Tile lastTile) {
        this(tileId, action, nextTile);
        this.lastTile = lastTile;
    }

    /**
     * Creates a tile with an ID, grid position, action, next tile, and last tile.
     *
     * @param tileId the tile ID
     * @param row the row position
     * @param col the column position
     * @param action the tile action
     * @param nextTile the next tile
     * @param lastTile the last tile
     * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
     */
    public Tile(int tileId, int row, int col, TileAction action, Tile nextTile, Tile lastTile) {
        this(tileId, row, col, action, nextTile);
        this.lastTile = lastTile;
    }

    /**
     * Returns the tile's ID.
     *
     * @return the tile ID
     */
    public int getTileId() {
        return tileId;
    }

    /**
     * Returns the row position of this tile.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column position of this tile.
     *
     * @return the column
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the row of this tile.
     *
     * @param row the new row value
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the column of this tile.
     *
     * @param col the new column value
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Returns the next tile if present.
     *
     * @return an Optional containing the next tile if not null
     */
    public Optional<Tile> getNextTile() {
        return Optional.ofNullable(nextTile);
    }

    /**
     * Returns the last tile if present.
     *
     * @return an Optional containing the last tile if not null
     */
    public Optional<Tile> getLastTile() {
        return Optional.ofNullable(lastTile);
    }

    /**
     * Returns the previous tile if present.
     *
     * @return an Optional containing the previous tile if not null
     */
    public Optional<Tile> getPreviousTile() {
        return Optional.ofNullable(previousTile);
    }

    /**
     * Returns the action if present.
     *
     * @return an Optional containing the tile action if not null
     * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
     */
    public Optional<TileAction> getAction() {
        return Optional.ofNullable(action);
    }

    /**
     * Sets the next tile reference.
     *
     * @param nextTile the next tile
     */
    public void setNextTile(Tile nextTile) {
        this.nextTile = nextTile;
    }

    /**
     * Sets the last tile reference.
     *
     * @param lastTile the last tile
     */
    public void setLastTile(Tile lastTile) {
        this.lastTile = lastTile;
    }

    /**
     * Sets the previous tile reference.
     *
     * @param previousTile the previous tile
     */
    public void setPreviousTile(Tile previousTile) {
        this.previousTile = previousTile;
    }

    /**
     * Sets the action.
     *
     * @param action the tile action
     * @see edu.ntnu.idi.idatt.boardgame.actions.TileAction
     */
    public void setAction(TileAction action) {
        this.action = action;
    }

    /**
     * Resolves references to other tiles on the board.
     *
     * @param board the board containing the tiles
     * @see edu.ntnu.idi.idatt.boardgame.model.Board
     */
    @Override
    public void resolveReferences(Board board) {
        if (nextTileId != 0) {
            nextTile = board.getTile(nextTileId);
        }
        if (previousTileId != 0) {
            previousTile = board.getTile(previousTileId);
        }
        if (lastTileId != 0) {
            lastTile = board.getTile(lastTileId);
        }
        if (action instanceof HasTileReferenceResolver) {
            ((HasTileReferenceResolver) action).resolveReferences(board);
        }
    }
}