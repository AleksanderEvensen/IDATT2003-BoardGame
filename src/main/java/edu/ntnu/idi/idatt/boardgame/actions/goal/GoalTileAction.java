package edu.ntnu.idi.idatt.boardgame.actions.goal;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Player;

public class GoalTileAction implements TileAction {

  @Override
  public boolean perform(Player player) {
    return true;
  }
}
