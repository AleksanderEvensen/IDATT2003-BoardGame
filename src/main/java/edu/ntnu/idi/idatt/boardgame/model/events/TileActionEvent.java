package edu.ntnu.idi.idatt.boardgame.model.events;

import edu.ntnu.idi.idatt.boardgame.model.GameEngine;
import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;

/**
 * Event fired when a player uses a ladder to move between tiles.
 * <p>
 * This event contains information about the ladder traversal, including the player who used the
 * ladder and the source and destination tiles. UI components can observe this event to animate the
 * ladder movement.
 * </p>
 *
 * @see GameEngine
 * @see LadderAction
 * @see Player
 * @see Tile
 * @since v2.0.0
 */
public record TileActionEvent(
    Player player,
    Tile tile,
    TileAction tileAction
) implements GameEvent {

}
