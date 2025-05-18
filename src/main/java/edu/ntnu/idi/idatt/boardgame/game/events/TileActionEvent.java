package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

/**
 * Event fired when a player uses a ladder to move between tiles.
 * <p>
 * This event contains information about the ladder traversal, including the player who used the
 * ladder and the source and destination tiles. UI components can observe this event to animate the
 * ladder movement.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @see edu.ntnu.idi.idatt.boardgame.model.Tile
 * @since v2.0.0
 */
public class TileActionEvent implements GameEvent {

  private final Player player;
  private final TileAction tileAction;
  private final Tile tile;

  /**
   * Creates a new TileActionEvent.
   *
   * @param player     the player who used the ladder
   * @param tile       the tile where the action was triggered
   * @param tileAction the tile where the ladder starts
   */
  public TileActionEvent(Player player, Tile tile, TileAction tileAction) {
    this.player = player;
    this.tile = tile;
    this.tileAction = tileAction;
  }

  /**
   * Gets the player who used the ladder.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the tile where the ladder starts.
   *
   * @return the source tile
   */
  public TileAction getTileAction() {
    return tileAction;
  }

  /**
   * Gets the tile where the action was triggered.
   *
   * @return the start tile
   */
  public Tile getTile() {
    return tile;
  }
}
