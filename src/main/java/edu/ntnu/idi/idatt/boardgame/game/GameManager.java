package edu.ntnu.idi.idatt.boardgame.game;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.model.Game;

/**
 * Manages the loading and retrieval of games.
 * <p>
 * This class is responsible for loading games from JSON files and providing
 * access to loaded games.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.model.Game
 * @since v1.0.0
 */
public class GameManager {
  private final HashMap<String, Game> games = new HashMap<>();
  private final LocalFileProvider fileProvider;
  private final Logger logger = Logger.getLogger(GameManager.class.getName());

  /**
   * Constructs a GameManager with the specified file provider.
   *
   * @param fileProvider the file provider for loading game files
   */
  public GameManager(LocalFileProvider fileProvider) {
    this.fileProvider = fileProvider;
    loadGame("games/ladder.json");
    loadGame("games/bezzerwizzer.json");
  }

  /**
   * Loads a game from the specified file path.
   * <p>
   * The game is loaded from a JSON file and added to the games map.
   * </p>
   *
   * @param path the path to the game file
   */
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

  /**
   * Retrieves a game by its ID.
   *
   * @param id the game ID
   * @return the game with the specified ID, or null if not found
   * @see edu.ntnu.idi.idatt.boardgame.model.Game
   */
  public Game getGame(String id) {
    return games.get(id);
  }

  /**
   * Returns the IDs of all available games.
   *
   * @return a set of game IDs
   */
  public Set<String> getAvailableGameIds() {
    return games.keySet();
  }
}