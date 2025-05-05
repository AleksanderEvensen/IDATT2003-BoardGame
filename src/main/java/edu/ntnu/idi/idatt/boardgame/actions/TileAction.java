package edu.ntnu.idi.idatt.boardgame.actions;

import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * Represents an action that can be performed on a tile.
 * <p>
 * Implementing classes should define the specific action to be performed when a player lands on a
 * tile.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v0.0.1
 */
public interface TileAction {

  /**
   * Performs the action on the specified player.
   *
   * @param player the player on whom the action is performed
   * @return true if the action was performed successfully, false otherwise
   * @throws IllegalArgumentException if the player is null
   * @see edu.ntnu.idi.idatt.boardgame.model.Player
   */
  boolean perform(Player player);
}