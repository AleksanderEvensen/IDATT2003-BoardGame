package edu.ntnu.idi.idatt.boardgame.game.events;

import java.util.List;

import edu.ntnu.idi.idatt.boardgame.model.Player;

/**
 * Event fired when a player rolls the dice.
 * <p>
 * This event contains details about the dice roll, including both the total
 * value
 * rolled and the individual values of each die. This allows UI components to
 * display
 * the dice accurately.
 * </p>
 * 
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @since v2.0.0
 */
public class DiceRolledEvent implements GameEvent {
    private final Player player;
    private final int totalValue;
    private final List<Integer> individualRolls;

    /**
     * Creates a new DiceRolledEvent.
     *
     * @param player          the player who rolled the dice
     * @param totalValue      the total value rolled
     * @param individualRolls the individual values of each die
     */
    public DiceRolledEvent(Player player, int totalValue, List<Integer> individualRolls) {
        this.player = player;
        this.totalValue = totalValue;
        this.individualRolls = individualRolls;
    }

    /**
     * Gets the player who rolled the dice.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the total value rolled on all dice.
     *
     * @return the total dice value
     */
    public int getTotalValue() {
        return totalValue;
    }

    /**
     * Gets the individual values rolled on each die.
     *
     * @return a list of values, one per die
     */
    public List<Integer> getIndividualRolls() {
        return individualRolls;
    }

    /**
     * Gets the number of dice that were rolled.
     *
     * @return the number of dice
     */
    public int getDiceCount() {
        return individualRolls.size();
    }

    /**
     * Gets the value from a specific die.
     *
     * @param index the index of the die (0-based)
     * @return the value of the specified die
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public int getDieValue(int index) {
        return individualRolls.get(index);
    }
}
