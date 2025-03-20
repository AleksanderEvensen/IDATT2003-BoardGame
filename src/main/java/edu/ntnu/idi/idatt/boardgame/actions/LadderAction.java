package edu.ntnu.idi.idatt.boardgame.actions;

import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

/**
 * Represents an action where a player moves to a destination tile via a ladder.
 * <p>
 * This action is triggered when a player lands on a tile with a ladder.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.model.Tile
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v0.0.1
 */
public class LadderAction implements TileAction, HasTileReferenceResolver {

    private final int destinationTileId;
    private transient Tile destinationTile;

    /**
     * Constructs a LadderAction with the specified destination tile.
     *
     * @param destinationTile the tile to move the player to
     * @throws IllegalArgumentException if the destination tile is null
     */
    public LadderAction(Tile destinationTile) {
        if (destinationTile == null) {
            throw new IllegalArgumentException("Destination tile cannot be null");
        }
        this.destinationTile = destinationTile;
        this.destinationTileId = destinationTile.getTileId();
    }

    /**
     * Returns the destination tile.
     *
     * @return the destination tile
     */
    public Tile getDestinationTile() {
        return destinationTile;
    }

    /**
     * Sets the destination tile.
     *
     * @param destinationTile the tile to set as the destination
     */
    public void setDestinationTile(Tile destinationTile) {
        this.destinationTile = destinationTile;
    }

    /**
     * Performs the ladder action, moving the player to the destination tile.
     *
     * @param player the player to move
     * @throws IllegalArgumentException if the player is null
     * @see edu.ntnu.idi.idatt.boardgame.model.Player
     */
    @Override
    public void perform(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        player.moveToTile(destinationTile, false);
        System.out.printf("Player %s triggered a LadderAction and moved to tile %d\n", player.getName(), destinationTile.getTileId());
    }

    @Override
    public String toString() {
        return "LadderAction{" +
                "destinationTile=" + destinationTile +
                '}';
    }

    /**
     * Resolves references to other tiles on the board.
     *
     * @param board the board containing the tiles
     * @see edu.ntnu.idi.idatt.boardgame.model.Board
     */
    @Override
    public void resolveReferences(Board board) {
        destinationTile = board.getTile(destinationTileId);
    }
}