package edu.ntnu.idi.idatt.boardgame.model.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Represents a quiz question with its category, possible answers, and the correct answer index.
 * <p>
 * This class encapsulates the details of a quiz question, including its text, category, possible
 * answers, and the index of the correct answer.
 * </p>
 *
 * @version 1.0.0
 * @see QuestionCategory
 * @since v3.0.0
 */
public class Question {


  private final Map<Integer, String> answers;

  @Getter
  private final int correctAnswerIndex;

  @Getter
  private final String question;

  @Getter
  private final QuestionCategory category;

  public Question(String question, QuestionCategory category, HashMap<Integer, String> answers,
      int correctAnswerIndex) {
    this.question = question;
    this.category = category;
    this.answers = answers;
    this.correctAnswerIndex = correctAnswerIndex;
  }

  /**
   * Returns the question text.
   *
   * @return the question text
   */
  public List<String> getAnswers() {
    return answers.values().stream().toList();
  }

  /**
   * Returns the correct answer.
   *
   * @return the correct answer
   */
  public String getCorrectAnswer() {
    return answers.get(correctAnswerIndex);
  }

}
