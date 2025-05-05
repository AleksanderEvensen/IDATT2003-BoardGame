package edu.ntnu.idi.idatt.boardgame.actions.ladder;

import edu.ntnu.idi.idatt.boardgame.actions.HasTileReferenceResolver;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import java.util.logging.Logger;

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
  private Tile destinationTile;
  private static final Logger logger = Logger.getLogger(LadderAction.class.getName());

  /**
   * Constructs a ladder action with a destination tile.
   *
   * @param destinationTile the destination tile
   * @see edu.ntnu.idi.idatt.boardgame.model.Tile
   */
  public LadderAction(Tile destinationTile) {
    if (destinationTile == null) {
      throw new IllegalArgumentException("Destination tile cannot be null");
    }
    this.destinationTile = destinationTile;
    this.destinationTileId = destinationTile.getTileId();
  }

  /**
   * Constructs a ladder action with a destination tile ID. This constructor is used for
   * deserialization.
   *
   * @param destinationTileId the destination tile ID
   */
  public LadderAction(int destinationTileId) {
    this.destinationTileId = destinationTileId;
  }

  /**
   * Returns the destination tile.
   *
   * @return the destination tile
   * @see edu.ntnu.idi.idatt.boardgame.model.Tile
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
   * Performs the ladder action, moving the player to the destination tile. if the player is immune,
   * the player will not move when the destination tile is lower than the current tile.
   *
   * @param player the player to move
   * @throws IllegalArgumentException if the player is null
   * @see edu.ntnu.idi.idatt.boardgame.model.Player
   * <p>
   * returns true if the action was performed successfully, false otherwise
   */
  @Override
  public boolean perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (player.getCurrentTile() == null) {
      throw new IllegalArgumentException("Player must be on a tile");
    }
    if (player.getCurrentTile().getTileId() > destinationTile.getTileId() && player.isImmune()) {
      player.setImmunityTurns(player.getImmunityTurns() - 1);
      return false;
    }
    player.moveToTile(destinationTile, false);
    logger.info("Player " + player.getName() + " moved to tile " + destinationTile.getTileId());
    return true;
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