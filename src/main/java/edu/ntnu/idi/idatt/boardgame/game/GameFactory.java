package edu.ntnu.idi.idatt.boardgame.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.boardgame.game.adapters.GameAdapter;
import edu.ntnu.idi.idatt.boardgame.game.adapters.TileActionAdapter;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Game;


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
    Gson gson = getGson();
    Game game = gson.fromJson(json, Game.class);
    game.resolveReferences(game.getBoard());
    return game;
  }

  /**
   * Create a JSON representation of a game.
   * <p>
   *
   * @param game the game to create a JSON representation of.
   * @return a JSON representation of the game.
   */
  public static String createJson(Game game) {
    Gson gson = getGson();
    return gson.toJson(game);
  }

  private static Gson getGson() {
    return new GsonBuilder()
        .registerTypeAdapter(Game.class, new GameAdapter())
        .registerTypeAdapterFactory(TileActionAdapter.getFactory())
        .create();
  }
}