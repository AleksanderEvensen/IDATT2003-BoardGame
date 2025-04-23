package edu.ntnu.idi.idatt.boardgame.game.events;

import java.util.List;

import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * Event fired when a game is started with players.
 * <p>
 * This event provides information about the game that was started and the
 * players
 * participating. UI components can observe this event to initialize the game
 * view.
 * </p>
 * 
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.model.Game
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v2.0.0
 */
public class GameStartedEvent implements GameEvent {
  private final Game game;
  private final List<Player> players;

  /**
   * Creates a new GameStartedEvent.
   *
   * @param game    the game that was started
   * @param players the list of players participating
   */
  public GameStartedEvent(Game game, List<Player> players) {
    this.game = game;
    this.players = players;
  }

  /**
   * Gets the game that was started.
   *
   * @return the game
   */
  public Game getGame() {
    return game;
  }

  /**
   * Gets the list of players participating in the game.
   *
   * @return the list of players
   */
  public List<Player> getPlayers() {
    return players;
  }
}
