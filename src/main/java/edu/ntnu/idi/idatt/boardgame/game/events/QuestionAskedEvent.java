package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.quiz.Question;
import lombok.Getter;

/**
 * Represents an event that occurs when a question is asked in the game.
 * <p>
 * This event contains the question being asked and the player who is being asked.
 * </p>
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class QuestionAskedEvent implements GameEvent {


  @Getter
  private final Question question;

  @Getter
  private final Player player;

  /**
   * Constructs a QuestionAskedEvent with the specified question, answers, and correct answer index.
   *
   * @param question the quiz question
   */
  public QuestionAskedEvent(Question question, Player player) {
    this.question = question;
    this.player = player;
  }


}
