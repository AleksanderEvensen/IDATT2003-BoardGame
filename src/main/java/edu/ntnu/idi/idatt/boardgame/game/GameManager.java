package edu.ntnu.idi.idatt.boardgame.game;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class GameManager {
  private final HashMap<String, Game> games = new HashMap<>();
  private final LocalFileProvider fileProvider;
  private final Logger logger = Logger.getLogger(GameManager.class.getName());

  public GameManager(LocalFileProvider fileProvider) {
    this.fileProvider = fileProvider;
    loadGame("games/ladder.json");
  }

  public void loadGame(String path) {
    try {
      InputStream inputStream = fileProvider.get(path);
      String json = Utils.bytesToString(inputStream.readAllBytes());
      Game game = GameFactory.createGame(json);
      games.put(game.getId(), game);
    } catch (Exception e) {
      logger.severe("Failed to load game from path: " + path);
    }
  }

  public Game getGame(String id) {
    return games.get(id);
  }

}
