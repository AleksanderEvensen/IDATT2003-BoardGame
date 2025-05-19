package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * Event fired when the game ends (a player reaches the final tile).
 * <p>
 * This event provides information about which game ended and the player who won. UI components can
 * observe this event to display a game over screen.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.model.Game
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v2.0.0
 */
public record GameEndedEvent(
    Game game,
    Player winner
) implements GameEvent {

}
