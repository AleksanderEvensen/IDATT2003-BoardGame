package edu.ntnu.idi.idatt.boardgame.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.boardgame.game.adapters.GameAdapter;
import edu.ntnu.idi.idatt.boardgame.game.adapters.TileActionAdapter;
import edu.ntnu.idi.idatt.boardgame.model.Game;

public class GameLoader {
  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Game.class, new GameAdapter())
      .registerTypeAdapterFactory(TileActionAdapter.getFactory())
      .create();

  public static Game loadGame(String json) {
    return gson.fromJson(json, Game.class);
  }
}
