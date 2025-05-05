package edu.ntnu.idi.idatt.boardgame.actions.freeze;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Player;

public class FreezeAction implements TileAction {

  /**
   * Performs the action on the specified player. If the player is immune, the player will not be
   * frozen. If the player is already frozen, the player will be frozen for an additional turn.
   *
   * @param player the player on whom the action is performed
   * @throws IllegalArgumentException if the player is null
   * @see Player
   */
  @Override
  public boolean perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (player.isImmune()) {
      player.setImmunityTurns(player.getImmunityTurns() - 1);
      return false;
    }
    if (player.isFrozen()) {
      return false;
    }
    player.setFrozenTurns(player.getFrozenTurns() + 1);
    return true;
  }

}
