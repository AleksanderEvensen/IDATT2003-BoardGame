package edu.ntnu.idi.idatt.boardgame.javafx.style;

import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.freeze.FreezeAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.goal.GoalTileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.immunity.ImmunityAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.quiz.QuizTileAction;
import edu.ntnu.idi.idatt.boardgame.model.style.TileStyleApplier;
import edu.ntnu.idi.idatt.boardgame.model.style.TileStyleApplierFactory;

/**
 * JavaFX implementation of the TileStyleApplierFactory. This factory creates JavaFX-specific style
 * appliers for each action type.
 *
 * @see TileStyleApplier
 * @since v2.0.0
 */
public class JavaFxTileStyleApplierFactory implements TileStyleApplierFactory {

  private static final JavaFxTileStyleApplierFactory INSTANCE = new JavaFxTileStyleApplierFactory();

  private final JavaFxLadderStyleApplier ladderStyleApplier = new JavaFxLadderStyleApplier();
  private final JavaFxFreezeStyleApplier freezeStyleApplier = new JavaFxFreezeStyleApplier();
  private final JavaFxImmunityStyleApplier immunityStyleApplier = new JavaFxImmunityStyleApplier();
  private final JavaFxQuizStyleApplier quizStyleApplier = new JavaFxQuizStyleApplier();
  private final JavaFxGoalStyleApplier goalStyleApplier = new JavaFxGoalStyleApplier();

  private JavaFxTileStyleApplierFactory() {
  }

  /**
   * Get the singleton instance of this factory.
   *
   * @return The factory instance
   */
  public static JavaFxTileStyleApplierFactory getInstance() {
    return INSTANCE;
  }

  @Override
  public TileStyleApplier getStyleApplier(TileAction action) {
    switch (action) {
      case LadderAction ladderAction -> {
        return ladderStyleApplier;
      }
      case FreezeAction freezeAction -> {
        return freezeStyleApplier;
      }
      case ImmunityAction immunityAction -> {
        return immunityStyleApplier;
      }
      case QuizTileAction quizTileAction -> {
        return quizStyleApplier;
      }
      case GoalTileAction goalTileAction -> {
        return goalStyleApplier;
      }
      default -> {
        return null;
      }
    }
  }
}