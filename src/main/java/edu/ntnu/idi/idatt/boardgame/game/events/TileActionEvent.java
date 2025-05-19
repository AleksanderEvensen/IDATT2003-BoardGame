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
public record TileActionEvent(
    Player player,
    Tile tile,
    TileAction tileAction
) implements GameEvent {

}
