package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.quiz.Question;

/**
 * Represents an event that occurs when a question is asked in the game.
 * <p>
 * This event contains the question being asked and the player who is being asked.
 * </p>
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public record QuestionAskedEvent(
    Question question, Player player
) implements GameEvent {

}
