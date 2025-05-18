package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * Event fired when the turn changes to another player.
 * <p>
 * This event contains information about the player whose turn it now is. UI components can observe
 * this event to update the display to indicate whose turn it is and enable controls for that
 * player.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v2.0.0
 */
public class PlayerTurnChangedEvent implements GameEvent {

  private final Player currentPlayer;

  /**
   * Creates a new PlayerTurnChangedEvent.
   *
   * @param currentPlayer the player whose turn it now is
   */
  public PlayerTurnChangedEvent(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  /**
   * Gets the player whose turn it is.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
