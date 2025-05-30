package edu.ntnu.idi.idatt.boardgame.javafx.controllers;

import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.javafx.components.enums.ToastStyle;
import edu.ntnu.idi.idatt.boardgame.javafx.providers.ToastProvider;
import edu.ntnu.idi.idatt.boardgame.model.entities.Color;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.managers.GameManager;
import edu.ntnu.idi.idatt.boardgame.model.managers.PlayerManager;
import java.io.File;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import lombok.Getter;

/**
 * Controller for the main menu view.
 *
 * <p>
 * This class handles the interactions and updates for the main menu, including player management
 * and navigation to the game view.
 * </p>
 *
 * @version v1.0.0
 * @since v3.0.0
 */
public class MainMenuController {

  private final Logger logger = Logger.getLogger(MainMenuController.class.getName());

  @Getter
  private final ObservableList<Player> players;

  @Getter
  private final ObservableMap<String, Game> games;


  /**
   * Constructs a MainMenuController and registers it as an observer to the PlayerManager.
   */
  public MainMenuController() {
    this.players = FXCollections.observableArrayList();
    this.games = FXCollections.observableHashMap();

    PlayerManager.getInstance().addListener(this.players::setAll);
    GameManager.getInstance().addListener(this.games::putAll);
  }

  /**
   * Initializes the controller by loading players from the PlayerManager.
   * <p>
   * This method is called when the main menu is loaded.
   * </p>
   */
  public void initialize() {
    this.players.setAll(PlayerManager.getInstance().getPlayers());
    this.games.putAll(GameManager.getInstance().getGames());
  }

  /**
   * Exits the application and returns to the desktop.
   * <p>
   * This method is called when the user chooses to exit the application.
   * </p>
   */
  public void exitToDesktop() {
    Application.closeApplication();
  }

  /**
   * Starts a game with the specified game ID.
   * <p>
   * This method is called when the user starts a game.
   * </p>
   *
   * @param gameId the ID of the game to start
   */
  public void startGame(String gameId) {
    try {
      Application.router.navigate(String.format("/game/%s", gameId));
    } catch (Exception e) {
      logger.severe(e.getMessage());
      ToastProvider.show("Failed to start game: " + e.getMessage(), Duration.seconds(5),
          ToastStyle.ERROR);
    }
  }

  /**
   * Loads a game from a file.
   */
  public void loadGameFromFile() {
    try {
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters()
          .add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
      File file = fileChooser.showOpenDialog(Application.getPrimaryStage());
      logger.info("File: " + file);
      if (file == null) {
        return;
      }
      logger.info("Path: " + file.getPath());
      if (!file.exists()) {
        throw new Exception("File does not exist");
      }
      logger.info("Loading game from file: " + file.getPath());

      GameManager.getInstance().loadGame(file.getPath());
    } catch (Exception e) {
      logger.severe(e.getMessage());
      ToastProvider.show(e.getMessage());
    }
  }

  /**
   * Adds a new player to the game.
   * <p>
   * This method is called when the user chooses to add a new player.
   * </p>
   *
   * @param playerName the playerName to add
   */
  public void addPlayer(String playerName) {
    logger.info("Adding player: " + playerName);
    if (playerName.trim().isBlank()) {
      ToastProvider.show("Please enter a player name", Duration.seconds(5), ToastStyle.ERROR);
    }
    try {
      PlayerManager.getInstance().addPlayer(new Player(playerName, Utils.getRandomColor()));
      ToastProvider.show("New player was added");
    } catch (Exception e) {
      ToastProvider.show(e.getMessage(), Duration.seconds(5), ToastStyle.ERROR);
    }
  }

  /**
   * Removes the specified player from the game.
   * <p>
   * This method is called when the user chooses to remove a player.
   * </p>
   *
   * @param player the player to remove
   */
  public void removePlayer(Player player) {
    PlayerManager.getInstance().removePlayer(player);
  }


  /**
   * Updates the specified player with new information.
   * <p>
   * This method is called when the user chooses to update a player's information.
   * </p>
   *
   * @param player   the player to update
   * @param newName  the new name for the player
   * @param newColor the new color for the player
   */
  public void updatePlayer(Player player, String newName, Color newColor) {
    try {
      PlayerManager.getInstance().updatePlayer(player, newName, newColor);
    } catch (Exception e) {
      ToastProvider.show(e.getMessage(), Duration.seconds(5), ToastStyle.ERROR);
    }
  }

  /**
   * Saves the current players to a file.
   * <p>
   * This method is called when the user chooses to save the players.
   * </p>
   */
  public void savePlayersToFile() {
    PlayerManager.getInstance().savePlayers("data/players.csv");
  }

  /**
   * Toggles the application theme between light and dark mode.
   */
  public void toggleTheme() {
    Application.setDarkTheme(!Application.isDarkTheme());
  }
}
