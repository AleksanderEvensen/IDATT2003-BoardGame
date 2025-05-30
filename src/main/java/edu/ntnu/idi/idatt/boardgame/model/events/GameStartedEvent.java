package edu.ntnu.idi.idatt.boardgame.model.events;

import edu.ntnu.idi.idatt.boardgame.model.GameEngine;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import java.util.List;

/**
 * Event fired when a game is started with players.
 * <p>
 * This event provides information about the game that was started and the players participating. UI
 * components can observe this event to initialize the game view.
 * </p>
 *
 * @see GameEngine
 * @see Game
 * @see Player
 * @since v2.0.0
 */
public record GameStartedEvent(
    Game game,
    List<Player> players
) implements GameEvent {

}
