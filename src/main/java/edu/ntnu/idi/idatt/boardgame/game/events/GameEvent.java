package edu.ntnu.idi.idatt.boardgame.game.events;

/**
 * Base interface for all game events that can be observed by UI components.
 * <p>
 * Different event types extend this interface to provide specific information
 * about
 * various game occurrences, such as dice rolls, player movements, and game
 * state changes.
 * </p>
 * 
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable
 * @since v2.0.0
 */
public interface GameEvent {
}
