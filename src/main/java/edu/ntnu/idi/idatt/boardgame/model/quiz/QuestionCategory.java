package edu.ntnu.idi.idatt.boardgame.model.quiz;

import lombok.Getter;

/**
 * Enum representing different categories of quiz questions. Each category has a display name for
 * user-friendly representation.
 * <p>
 * since v1.0.0
 *
 * @version 1.0.0
 * @see Question
 */
@Getter
public enum QuestionCategory {
  GENERAL_KNOWLEDGE("General Knowledge"),
  SCIENCE("Science"),
  HISTORY("History"),
  GEOGRAPHY("Geography"),
  ENTERTAINMENT("Entertainment"),
  ART("Art"),
  SPORTS("Sports"),
  RANDOM("Random");

  private final String displayName;

  /**
   * Constructs a QuestionCategory with the specified display name.
   *
   * @param displayName the display name of the category
   */
  QuestionCategory(String displayName) {
    this.displayName = displayName;
  }
  
}
