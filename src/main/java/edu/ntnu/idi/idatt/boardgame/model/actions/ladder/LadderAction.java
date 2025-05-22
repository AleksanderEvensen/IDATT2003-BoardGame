package edu.ntnu.idi.idatt.boardgame.model.actions.ladder;

import edu.ntnu.idi.idatt.boardgame.model.actions.HasTileReferenceResolver;
import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Board;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import java.util.logging.Logger;
import lombok.Getter;

/**
 * Represents an action where a player moves to a destination tile via a ladder.
 * <p>
 * This action is triggered when a player lands on a tile with a ladder.
 * </p>
 *
 * @version v1.0.0
 * @see Tile
 * @see Player
 * @since v0.0.1
 */
public class LadderAction implements TileAction, HasTileReferenceResolver {

  @Getter
  private final int destinationTileId;
  @Getter
  private Tile destinationTile;
  private static final Logger logger = Logger.getLogger(LadderAction.class.getName());

  /**
   * Constructs a ladder action with a destination tile.
   *
   * @param destinationTile the destination tile
   * @see Tile
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
   * Performs the ladder action, moving the player to the destination tile. If the player is immune,
   * the player will not move when the destination tile is lower than the current tile.
   *
   * @param player the player to move
   * @return true if the action was performed successfully, false otherwise
   * @throws IllegalArgumentException if the player is null
   * @see Player
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
    return String.format("LadderAction{destinationTile=%s}", destinationTile);
  }

  /**
   * Resolves references to other tiles on the board.
   *
   * @param board the board containing the tiles
   * @see Board
   */
  @Override
  public void resolveReferences(Board board) {
    destinationTile = board.getTile(destinationTileId);
  }

}