package edu.ntnu.idi.idatt.boardgame.actions.immunity;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Player;

public class ImmunityAction implements TileAction {

  /**
   * Performs the action on the specified player.
   *
   * @param player the player on whom the action is performed
   * @throws IllegalArgumentException if the player is null
   * @see edu.ntnu.idi.idatt.boardgame.model.Player
   */
  @Override
  public void perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    player.setImmunityTurns(player.getImmunityTurns() + 1);
  }

}
