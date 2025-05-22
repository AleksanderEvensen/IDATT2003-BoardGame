package edu.ntnu.idi.idatt.boardgame.model.managers;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import edu.ntnu.idi.idatt.boardgame.model.exceptions.GameLoadException;
import edu.ntnu.idi.idatt.boardgame.model.factories.GameFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;
import lombok.NonNull;

/**
 * Manages the loading and retrieval of games.
 * <p>
 * This class is responsible for loading games from JSON files and providing access to loaded
 * games.
 * </p>
 *
 * @see Game
 * @since v1.0.0
 */
public class GameManager extends Observable<GameManager, Map<String, Game>> {

  private static final String DEFAULT_GAME_PATH = "data/games";
  private static volatile Supplier<FileProvider> fileProviderSupplier;
  private static GameManager instance;
  private final Map<String, Game> games = new HashMap<>();
  private final Logger logger = Logger.getLogger(GameManager.class.getName());
  private final FileProvider fileProvider;

  /**
   * Constructs a GameManager.
   *
   * @param fileProvider the file provider to use for loading game files
   */
  private GameManager(@NonNull FileProvider fileProvider) {
    this.fileProvider = fileProvider;
  }

  /**
   * Constructs a GameManager with a specified file provider.
   *
   * @param fileProvider the file provider to use for loading game files
   */
  public static synchronized void init(Supplier<FileProvider> fileProvider) {
    if (instance != null) {
      throw new IllegalStateException("GameManager already initialised");
    }
    if (fileProvider == null) {
      throw new NullPointerException("fileProvider must not be null");
    }
    fileProviderSupplier = fileProvider;
  }

  /**
   * Returns the singleton instance of GameManager.
   *
   * @return the singleton instance of GameManager
   */
  public static synchronized GameManager getInstance() {
    if (fileProviderSupplier == null) {
      throw new IllegalStateException("GameManager not initialised");
    }
    if (instance == null) {
      return instance = new GameManager(fileProviderSupplier.get());
    }

    return instance;
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
      String json = new String(fileProvider.get(path));
      Game game = GameFactory.createGame(json);
      games.put(game.getId(), game);
      this.notifyObservers(Collections.unmodifiableMap(games));
    } catch (Exception e) {
      logger.severe("Failed to load game from path: " + path);
      logger.severe("Error: " + e.getMessage());
      throw new GameLoadException("Failed to load game from path: " + path, e);
    }
  }

  /**
   * Retrieves a game by its ID.
   *
   * @param id the game ID
   * @return the game with the specified ID, or null if not found
   * @see Game
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

  /**
   * Returns an unmodifiable view of the games map.
   *
   * @return a map of game IDs to games
   */
  public Map<String, Game> getGames() {
    return Collections.unmodifiableMap(games);
  }


  /**
   * Loads games from the default path.
   * <p>
   * This method loads all game files from the default path and adds them to the games map.
   * </p>
   */
  public void loadGamesFromDefaultPath() {
    List<String> directoryFiles = fileProvider.listFiles(DEFAULT_GAME_PATH).stream()
        .filter(fileName -> fileName.endsWith(".json")).toList();

    for (String fileName : directoryFiles) {
      String filePath = DEFAULT_GAME_PATH + "/" + fileName;
      loadGame(filePath);
    }
  }


}