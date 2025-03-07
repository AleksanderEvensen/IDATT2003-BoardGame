package edu.ntnu.idi.idatt.boardgame.model;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;

import java.io.Serializable;
import java.util.Optional;

/**
 * A class representing a tile on the playing board
 */
public class Tile implements Serializable {
    private final int tileId;
    // Nullable fields
    private TileAction action = null;
    private Tile nextTile = null;
    private Tile lastTile = null;

    /**
     * Initialize a new tile with only the TileId
     * @param tileId the id
     */
    public Tile(int tileId) {
        this.tileId = tileId;
    }

    /**
     * Initialize a new tile with the TileId and the action
     * @param tileId the tile id
     * @param action the action for the tile
     */
    public Tile(int tileId, TileAction action) {
        this(tileId);
        this.action = action;
    }

    /**
     * Initialize a new tile with the TileId, the action and the next tile
     * @param tileId the tile id
     * @param action the action for the tile
     * @param nextTile the next tile
     */
    public Tile(int tileId, TileAction action, Tile nextTile) {
        this(tileId, action);
        this.nextTile = nextTile;
    }

    /**
     * Initialize a new tile with the TileId, the action, the next tile and the last(previous) tile
     * @param tileId the tile id
     * @param action the action for the tile
     * @param nextTile the next tile
     * @param lastTile the last(previous) tile
     */
    public Tile(int tileId, TileAction action, Tile nextTile, Tile lastTile) {
        this(tileId, action, nextTile);
        this.lastTile = lastTile;
    }

    /**
     * Get the next tile
     * @return the next tile, if the next til is null, then the Optional will be empty
     */
    public Optional<Tile> getNextTile() {
        // Safely return the nextTile
        return Optional.ofNullable(nextTile);
    }

    /**
     * Get the last(previous) tile
     * @return the last tile, if the last tile is null, then the Optional will be empty
     */
    public Optional<Tile> getLastTile() {
        // Safely return the lastTile
        return Optional.ofNullable(lastTile);
    }

    public int getTileId() {
        return tileId;
    }

    /**
     * Get the action of the tile
     * @return the action, if the action is null, then the Optional will be empty
     */
    public Optional<TileAction> getAction() {
        // Safely return the action
        return Optional.ofNullable(action);
    }

    /**
     * Sets the next tile
     * @param nextTile the new tile
     */
    public void setNextTile(Tile nextTile) {
        this.nextTile = nextTile;
    }

    /**
     * Sets the last(previous) tile
     * @param lastTile the new tile
     */
    public void setLastTile(Tile lastTile) {
        this.lastTile = lastTile;
    }

    /**
     * Sets the action of the tile
     * @param action the new action
     */
    public void setAction(TileAction action) {
        this.action = action;
    }
}
