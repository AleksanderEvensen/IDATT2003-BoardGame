package edu.ntnu.idi.idatt.boardgame.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.boardgame.actions.LadderAction;
import edu.ntnu.idi.idatt.boardgame.game.adapters.LadderActionAdapter;
import edu.ntnu.idi.idatt.boardgame.game.adapters.TileActionAdapter;
import edu.ntnu.idi.idatt.boardgame.game.adapters.TileAdapter;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Tile;


/**
 * Factory for creating games from JSON and creating JSON from games.
 */
public class GameFactory {


  /**
   * Create a game from a JSON representation.
   *
   * @param json the JSON representation of the game.
   * @return the game.
   */
  public static Game createGame(String json) {
    Game game = GameLoader.loadGame(json);
    Gson gson = getGson(game);
    return gson.fromJson(json, Game.class);
  }

  /**
   * Create a JSON representation of a game.
   * <p>
   *
   * @param game the game to create a JSON representation of.
   * @return a JSON representation of the game.
   */
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