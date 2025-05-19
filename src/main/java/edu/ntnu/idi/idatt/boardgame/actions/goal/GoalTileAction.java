package edu.ntnu.idi.idatt.boardgame.actions.goal;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * Represents an action where a player lands on a goal tile.
 *
 * @version v1.0.0
 * @since v3.0.0
 */
public class GoalTileAction implements TileAction {

  @Override
  public boolean perform(Player player) {
    return true;
  }
}
