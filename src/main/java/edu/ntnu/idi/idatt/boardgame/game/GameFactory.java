package edu.ntnu.idi.idatt.boardgame.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.boardgame.actions.LadderAction;
import edu.ntnu.idi.idatt.boardgame.game.adapters.LadderActionAdapter;
import edu.ntnu.idi.idatt.boardgame.game.adapters.TileActionAdapter;
import edu.ntnu.idi.idatt.boardgame.game.adapters.TileAdapter;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

public class GameFactory {


  public static Game createGame(String json) {
    Game game = GameLoader.loadGame(json);
    Gson gson = getGson(game);
    return gson.fromJson(json, Game.class);
  }

  public static String createJson(Game game) {
    Gson gson = getGson(game);
    return gson.toJson(game);
  }

  private static Gson getGson(Game game) {
    return new GsonBuilder()
        .registerTypeAdapterFactory(TileActionAdapter.getFactory())
        .registerTypeAdapter(Tile.class, new TileAdapter(game.getBoard()))
        .registerTypeAdapter(LadderAction.class, new LadderActionAdapter(game.getBoard()))
        .create();
  }
}