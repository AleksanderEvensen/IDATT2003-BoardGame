package edu.ntnu.idi.idatt.boardgame.game.adapters;


import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import edu.ntnu.idi.idatt.boardgame.actions.LadderAction;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;

/**
 * Adapter for TileAction, registering all subclasses of TileAction.
 */
public final class TileActionAdapter {

  public static RuntimeTypeAdapterFactory<TileAction> getFactory() {
    return RuntimeTypeAdapterFactory
        .of(TileAction.class, "type")
        .registerSubtype(LadderAction.class, "LADDER");
  }

  private TileActionAdapter() {
  }
}
