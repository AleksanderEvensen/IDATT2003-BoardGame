package edu.ntnu.idi.idatt.boardgame.model;

import java.io.Serializable;
import java.util.Optional;

import edu.ntnu.idi.idatt.boardgame.actions.HasTileReferenceResolver;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;

/**
 * A class representing a tile on the playing board, with an optional grid
 * location.
 * <p>
 * Each tile can have an action associated with it, and references to the next,
 * previous, and last tiles.
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
     * Private constructor used by the Builder.
     *
     * @param builder the builder with configuration
     */
    private Tile(Builder builder) {
        this.tileId = builder.tileId;
        this.row = builder.row;
        this.col = builder.col;
        this.action = builder.action;
        this.nextTileId = builder.nextTileId;
        this.lastTileId = builder.lastTileId;
        this.previousTileId = builder.previousTileId;

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
     * @see TileAction
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

    /**
     * Builder class for constructing Tile objects.
     */
    public static class Builder {
        private final int tileId;
        private int row = 0;
        private int col = 0;
        private TileAction action = null;
        private int previousTileId;
        private int nextTileId;
        private int lastTileId;

        /**
         * Constructor with required tileId.
         *
         * @param tileId the tile ID (required)
         */
        public Builder(int tileId) {
            this.tileId = tileId;
        }

        /**
         * Sets the grid position (row and column).
         *
         * @param row the row position
         * @param col the column position
         * @return this builder for method chaining
         */
        public Builder position(int row, int col) {
            this.row = row;
            this.col = col;
            return this;
        }

        /**
         * Sets the action for this tile.
         *
         * @param action the tile action
         * @return this builder for method chaining
         */
        public Builder action(TileAction action) {
            this.action = action;
            return this;
        }

        /**
         * Sets the next tile reference.
         *
         * @param nextTileId the next tile
         * @return this builder for method chaining
         */
        public Builder nextTileId(int nextTileId) {
            this.nextTileId = nextTileId;
            return this;
        }

        /**
         * Sets the previous tile reference.
         *
         * @param previousTileId the previous tile
         * @return this builder for method chaining
         */
        public Builder previousTileId(int previousTileId) {
            this.previousTileId = previousTileId;
            return this;
        }

        /**
         * Sets the last tile id.
         *
         * @param lastTileId the last tile
         * @return this builder for method chaining
         */
        public Builder lastTileId(int lastTileId) {
            this.lastTileId = lastTileId;
            return this;
        }

        /**
         * Builds and returns a new Tile instance with the configured properties.
         *
         * @return a new Tile instance
         */
        public Tile build() {
            return new Tile(this);
        }
    }
}