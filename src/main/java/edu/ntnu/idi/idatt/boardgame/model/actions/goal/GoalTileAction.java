package edu.ntnu.idi.idatt.boardgame.model.actions.goal;

import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;

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
