package edu.ntnu.idi.idatt.boardgame.model.events;

import edu.ntnu.idi.idatt.boardgame.model.GameEngine;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;

/**
 * Event fired when the turn changes to another player.
 * <p>
 * This event contains information about the player whose turn it now is. UI components can observe
 * this event to update the display to indicate whose turn it is and enable controls for that
 * player.
 * </p>
 *
 * @see GameEngine
 * @see Player
 * @since v2.0.0
 */
public record PlayerTurnChangedEvent(
    Player currentPlayer
) implements GameEvent {

}
