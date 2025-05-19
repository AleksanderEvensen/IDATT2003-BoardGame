package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import java.util.List;

/**
 * Event fired when a player rolls the dice.
 * <p>
 * This event contains details about the dice roll, including both the total value rolled and the
 * individual values of each die. This allows UI components to display the dice accurately.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v2.0.0
 */
public record DiceRolledEvent(
    Player player,
    int totalValue,
    List<Integer> individualRolls
) implements GameEvent {

  /**
   * Gets the value from a specific die.
   *
   * @param index the index of the die (0-based)
   * @return the value of the specified die
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public int getDieValue(int index) {
    return this.individualRolls.get(index);
  }
}
