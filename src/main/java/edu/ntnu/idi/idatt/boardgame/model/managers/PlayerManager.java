package edu.ntnu.idi.idatt.boardgame.model.managers;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.FileProvider;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable;
import edu.ntnu.idi.idatt.boardgame.model.entities.Color;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.exceptions.PlayerExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.NonNull;

/**
 * Manages players for the game, including loading and saving player data.
 */
public class PlayerManager extends Observable<PlayerManager, List<Player>> {

  private static PlayerManager instance;
  private static volatile Supplier<FileProvider> fileProviderSupplier;
  private final FileProvider fileProvider;
  private final Logger logger = Logger.getLogger(PlayerManager.class.getName());
  private final List<Player> players = new ArrayList<>();

  /**
   * Constructs a PlayerManager with the specified file provider.
   *
   * @param fileProvider the file provider for loading and saving player data
   */
  private PlayerManager(FileProvider fileProvider) {
    this.fileProvider = fileProvider;
  }


  /**
   * Constructs a PlayerManager with a specified file provider.
   *
   * @param fileProvider the file provider to use for loading game files
   */
  public static synchronized void init(Supplier<FileProvider> fileProvider) {
    if (instance != null) {
      throw new IllegalStateException("PlayerManager already initialised");
    }
    if (fileProvider == null) {
      throw new NullPointerException("fileProvider must not be null");
    }
    fileProviderSupplier = fileProvider;
  }

  /**
   * Returns the singleton instance of PlayerManager.
   *
   * @return the singleton instance of PlayerManager
   */
  public static synchronized PlayerManager getInstance() {
    if (fileProviderSupplier == null) {
      throw new IllegalStateException("PlayerManager not initialised");
    }
    if (instance != null) {
      return instance;
    }

    return instance = new PlayerManager(fileProviderSupplier.get());
  }

  /**
   * Loads JavaFX players from a file.
   *
   * @param path the path to the player data file
   */
  public void loadPlayers(String path) {
    try {
      var playersData = new String(fileProvider.get(path));

      players.clear();
      playersData.lines().forEach(line -> {
        String[] data = line.split(";");
        if (data.length != 2) {
          logger.warning(String.format("Invalid player data: '%s'", line));
          return;
        }

        String name = data[0];
        Color color = Color.fromHex(data[1]);

        players.add(new Player(name, color));
      });

      logger.info(String.format("Loaded %d players from '%s'", players.size(), path));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Failed to load players from path: " + path, e);
    } finally {
      notifyObservers(Collections.unmodifiableList(players));
    }
  }

  /**
   * Saves players to a file.
   *
   * @param path the path to save to
   */
  public void savePlayers(String path) {
    StringBuilder sb = new StringBuilder();
    for (Player player : this.players) {
      sb.append(String.format("%s;%s\n", player.getName(), player.getColor().toHex()));
    }

    try {
      fileProvider.save(path, sb.toString().getBytes());
      logger.info(String.format("Saved %d players to '%s'", players.size(), path));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Failed to save players to path: " + path, e);
    }
  }

  /**
   * Adds a new player.
   *
   * @param player the player to add
   * @return true if the player was added, false otherwise
   */
  public boolean addPlayer(Player player) {
    if (player == null) {
      logger.warning("Attempted to add null player");
      return false;
    }

    if (players.contains(player)) {
      logger.warning("Player already exists: " + player.getName());
      throw new PlayerExistsException(
          String.format("Player with same name '%s' and color already exists", player.getName()));
    }

    players.add(player);
    notifyObservers(Collections.unmodifiableList(players));
    logger.info("Added player: " + player.getName());
    return true;
  }

  /**
   * Removes a player by ID.
   *
   * @param playerId the ID of the player to remove
   * @return true if the player was removed, false if not found
   */
  public boolean removePlayer(int playerId) {
    if (playerId < 0 || playerId >= players.size()) {
      logger.warning("Invalid player ID: " + playerId);
      return false;
    }
    players.remove(playerId);
    notifyObservers(Collections.unmodifiableList(players));
    return false;
  }

  /**
   * Updates player information.
   *
   * @param playerId      the ID of the player to update
   * @param updatedPlayer the player with updated information
   * @return true if the player was updated, false if not found
   */
  public boolean updatePlayer(int playerId, @NonNull Player updatedPlayer) {
    if (playerId < 0 || playerId >= players.size()) {
      logger.warning("Invalid player ID: " + playerId);
      return false;
    }
    players.set(playerId, updatedPlayer);
    notifyObservers(Collections.unmodifiableList(players));
    logger.info("Updated player information for: " + updatedPlayer.getName());
    return true;
  }

  /**
   * Removes a specific player.
   *
   * @param player the player to remove
   * @return true if the player was removed, false if not found
   */
  public boolean removePlayer(@NonNull Player player) {
    int index = players.indexOf(player);
    if (index == -1) {
      logger.warning("Player not found for removal: " + player.getName());
      return false;
    }
    return this.removePlayer(index);
  }

  /**
   * Updates player information by finding the player in the list.
   *
   * @param player   the player to update
   * @param newName  the new name for the player
   * @param newColor the new color for the player
   * @return true if the player was updated, false if not found
   */
  public boolean updatePlayer(@NonNull Player player, String newName, Color newColor) {
    int index = players.indexOf(player);
    if (index == -1) {
      logger.warning("Player not found for update: " + player.getName());
      return false;
    }
    logger.info("Player index: " + index);
    Player updatedPlayer = new Player(newName, newColor);
    if (players.contains(updatedPlayer)) {
      throw new PlayerExistsException(
          String.format("Player with same name '%s' and color already exists", newName));
    }
    return this.updatePlayer(index, updatedPlayer);
  }

  public List<Player> getPlayers() {
    return Collections.unmodifiableList(this.players);
  }
}
