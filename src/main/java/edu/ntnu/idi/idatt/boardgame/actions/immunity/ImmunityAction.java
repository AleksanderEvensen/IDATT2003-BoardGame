package edu.ntnu.idi.idatt.boardgame.actions.immunity;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * The ImmunityAction class implements the TileAction interface and provides functionality to
 * increase the immunity turns of a player. This action is typically associated with tiles that
 * grant immunity to players.
 *
 * @version v1.0.0
 * @since v1.0.0
 */
public class ImmunityAction implements TileAction {

  /**
   * Performs the action on the specified player.
   *
   * @param player the player on whom the action is performed
   * @throws IllegalArgumentException if the player is null
   * @see edu.ntnu.idi.idatt.boardgame.model.Player
   */
  @Override
  public boolean perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    player.setImmunityTurns(player.getImmunityTurns() + 1);
    return true;
  }

}
