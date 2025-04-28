package edu.ntnu.idi.idatt.boardgame.model;

import java.io.Serializable;
import java.util.Optional;

import edu.ntnu.idi.idatt.boardgame.actions.HasTileReferenceResolver;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import lombok.Getter;
import lombok.Setter;

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

    private final @Getter int tileId;
    private @Getter @Setter int row;
    private @Getter @Setter int col;
    private TileAction action;
    private int previousTileId;
    private int nextTileId;
    private transient @Setter Tile previousTile;
    private transient @Setter Tile nextTile;

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
        this.previousTileId = builder.previousTileId;

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
     * Sets the action.
     *
     * @param action the tile action
     * @see TileAction
     */
    public void setAction(TileAction action) {
        this.action = action;
    }

    /**
     * Resolves references to other tiles on the board.
     *
     * @param board the board containing the tiles
     * @see Board
     */
    @Override
    public void resolveReferences(Board board) {
        if (nextTileId != 0) {
            nextTile = board.getTile(nextTileId);
        }
        if (previousTileId != 0) {
            previousTile = board.getTile(previousTileId);
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
         * Builds and returns a new Tile instance with the configured properties.
         *
         * @return a new Tile instance
         */
        public Tile build() {
            return new Tile(this);
        }
    }
}