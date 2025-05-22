package edu.ntnu.idi.idatt.boardgame.model.factories.adapters;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.freeze.FreezeAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.goal.GoalTileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.immunity.ImmunityAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.quiz.QuizTileAction;

/**
 * Adapter for TileAction, registering all subclasses of TileAction.
 * <p>
 * This adapter uses RuntimeTypeAdapterFactory to handle the serialization and deserialization of
 * TileAction and its subclasses.
 * </p>
 *
 * @see TileAction
 * @since v1.0.0
 */
public final class TileActionAdapter {

  /**
   * Returns a RuntimeTypeAdapterFactory for TileAction.
   * <p>
   * This factory registers the LadderAction subclass with the type name "LADDER".
   * </p>
   *
   * @return a RuntimeTypeAdapterFactory for TileAction
   */
  public static RuntimeTypeAdapterFactory<TileAction> getFactory() {
    return RuntimeTypeAdapterFactory
        .of(TileAction.class, "type")
        .registerSubtype(LadderAction.class, "LADDER")
        .registerSubtype(FreezeAction.class, "FREEZE")
        .registerSubtype(QuizTileAction.class, "QUIZ")
        .registerSubtype(GoalTileAction.class, "GOAL")
        .registerSubtype(ImmunityAction.class, "IMMUNITY");

  }
}