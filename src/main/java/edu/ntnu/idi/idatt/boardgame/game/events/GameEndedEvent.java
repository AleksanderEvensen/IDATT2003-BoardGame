package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * Event fired when the game ends (a player reaches the final tile).
 * <p>
 * This event provides information about which game ended and the player who
 * won.
 * UI components can observe this event to display a game over screen.
 * </p>
 * 
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.model.Game
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v2.0.0
 */
public class GameEndedEvent implements GameEvent {
  private final Game game;
  private final Player winner;

  /**
   * Creates a new GameEndedEvent.
   *
   * @param game   the game that ended
   * @param winner the player who won
   */
  public GameEndedEvent(Game game, Player winner) {
    this.game = game;
    this.winner = winner;
  }

  /**
   * Gets the game that ended.
   *
   * @return the game
   */
  public Game getGame() {
    return game;
  }

  /**
   * Gets the player who won.
   *
   * @return the winner
   */
  public Player getWinner() {
    return winner;
  }
}
