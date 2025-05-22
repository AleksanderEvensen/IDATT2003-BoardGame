package edu.ntnu.idi.idatt.boardgame.model.events;

import edu.ntnu.idi.idatt.boardgame.model.GameEngine;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;

/**
 * Event fired when a player's turn is skipped (e.g., due to being frozen).
 * <p>
 * This event provides information about the player whose turn was skipped and the reason for
 * skipping. UI components can observe this event to display messages to the user explaining why a
 * turn was skipped.
 * </p>
 *
 * @see GameEngine
 * @see Player
 * @since v2.0.0
 */
public record PlayerSkippedTurnEvent(
    Player player,
    String reason
) implements GameEvent {

}
