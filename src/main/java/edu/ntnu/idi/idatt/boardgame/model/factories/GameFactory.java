package edu.ntnu.idi.idatt.boardgame.model.factories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import edu.ntnu.idi.idatt.boardgame.model.factories.adapters.GameAdapter;
import edu.ntnu.idi.idatt.boardgame.model.factories.adapters.TileActionAdapter;


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
   * Create a game from a board.
   */
  private static Gson getGson() {
    return new GsonBuilder()
        .registerTypeAdapter(Game.class, new GameAdapter())
        .registerTypeAdapterFactory(TileActionAdapter.getFactory())
        .create();
  }
}