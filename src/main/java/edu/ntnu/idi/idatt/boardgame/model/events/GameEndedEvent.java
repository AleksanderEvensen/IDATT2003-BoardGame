package edu.ntnu.idi.idatt.boardgame.model.events;

import edu.ntnu.idi.idatt.boardgame.model.GameEngine;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;

/**
 * Event fired when the game ends (a player reaches the final tile).
 * <p>
 * This event provides information about which game ended and the player who won. UI components can
 * observe this event to display a game over screen.
 * </p>
 *
 * @see GameEngine
 * @see Game
 * @see Player
 * @since v2.0.0
 */
public record GameEndedEvent(
    Game game,
    Player winner
) implements GameEvent {

}
