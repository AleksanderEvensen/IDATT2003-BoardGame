package edu.ntnu.idi.idatt.boardgame.ui.javafx.audio;

import lombok.Getter;

/**
 * Enum representing the sound effects used in the game. Each enum constant corresponds to a
 * specific sound effect.
 */
@Getter
public enum GameSoundEffects {
  CLICK("click"),
  VICTORY("victory"),
  QUESTION("question"),
  CORRECT_ANSWER("correct_answer"),
  INCORRECT_ANSWER("incorrect_answer"),
  FREEZE("freeze"),
  IMMUNITY("immunity"),
  LADDER_CLIMB("ladder_climb"),
  LADDER_FALL("ladder_fall");

  private final String name;

  GameSoundEffects(String name) {
    this.name = name;
  }

}
