package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

/**
 * Event fired when a player moves from one tile to another.
 * <p>
 * This event contains detailed information about the player movement, including the source and
 * destination tiles, the dice value that triggered the movement, and the actual number of steps
 * moved. UI components can observe this event to animate player movements on the board.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @see edu.ntnu.idi.idatt.boardgame.model.Tile
 * @since v2.0.0
 */
public class PlayerMovedEvent implements GameEvent {

  private final Player player;
  private final Tile fromTile;
  private final Tile toTile;
  private final int diceValue;
  private final int stepsMoved;

  /**
   * Creates a new PlayerMovedEvent.
   *
   * @param player     the player who moved
   * @param fromTile   the tile the player moved from
   * @param toTile     the tile the player moved to
   * @param diceValue  the value rolled on the dice
   * @param stepsMoved the number of steps the player actually moved
   */
  public PlayerMovedEvent(Player player, Tile fromTile, Tile toTile, int diceValue,
      int stepsMoved) {
    this.player = player;
    this.fromTile = fromTile;
    this.toTile = toTile;
    this.diceValue = diceValue;
    this.stepsMoved = stepsMoved;
  }

  /**
   * Gets the player who moved.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the tile the player moved from.
   *
   * @return the source tile
   */
  public Tile getFromTile() {
    return fromTile;
  }

  /**
   * Gets the tile the player moved to.
   *
   * @return the destination tile
   */
  public Tile getToTile() {
    return toTile;
  }

  /**
   * Gets the value rolled on the dice.
   *
   * @return the dice value
   */
  public int getDiceValue() {
    return diceValue;
  }

  /**
   * Gets the number of steps the player actually moved.
   *
   * @return the number of steps moved
   */
  public int getStepsMoved() {
    return stepsMoved;
  }
}
