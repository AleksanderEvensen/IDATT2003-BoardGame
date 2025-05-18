package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * Event fired when a player's turn is skipped (e.g., due to being frozen).
 * <p>
 * This event provides information about the player whose turn was skipped and the reason for
 * skipping. UI components can observe this event to display messages to the user explaining why a
 * turn was skipped.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v2.0.0
 */
public class PlayerSkippedTurnEvent implements GameEvent {

  private final Player player;
  private final String reason;

  /**
   * Creates a new PlayerSkippedTurnEvent.
   *
   * @param player the player whose turn was skipped
   * @param reason the reason for skipping the turn
   */
  public PlayerSkippedTurnEvent(Player player, String reason) {
    this.player = player;
    this.reason = reason;
  }

  /**
   * Gets the player whose turn was skipped.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the reason for skipping the turn.
   *
   * @return the reason
   */
  public String getReason() {
    return reason;
  }
}
