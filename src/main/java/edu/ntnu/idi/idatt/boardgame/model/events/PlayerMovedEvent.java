package edu.ntnu.idi.idatt.boardgame.model.events;

import edu.ntnu.idi.idatt.boardgame.model.GameEngine;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;

/**
 * Event fired when a player moves from one tile to another.
 * <p>
 * This event contains detailed information about the player movement, including the source and
 * destination tiles, the dice value that triggered the movement, and the actual number of steps
 * moved. UI components can observe this event to animate player movements on the board.
 * </p>
 *
 * @see GameEngine
 * @see Player
 * @see Tile
 * @since v2.0.0
 */
public record PlayerMovedEvent(
    Player player,
    Tile fromTile,
    Tile toTile,
    int diceValue,
    int stepsMoved
) implements GameEvent {

}
