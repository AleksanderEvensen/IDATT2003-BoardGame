package edu.ntnu.idi.idatt.boardgame.model.actions;

import edu.ntnu.idi.idatt.boardgame.model.entities.Player;

/**
 * Represents an action that can be performed on a tile.
 * <p>
 * Implementing classes should define the specific action to be performed when a player lands on a
 * tile.
 * </p>
 *
 * @version v1.0.0
 * @see Player
 * @since v0.0.1
 */
public interface TileAction {

  /**
   * Performs the action on the specified player.
   *
   * @param player the player on whom the action is performed
   * @return true if the action was performed successfully, false otherwise
   * @throws IllegalArgumentException if the player is null
   * @see Player
   */
  boolean perform(Player player);
}