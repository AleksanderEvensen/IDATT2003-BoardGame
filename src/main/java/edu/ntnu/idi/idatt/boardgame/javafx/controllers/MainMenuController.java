package edu.ntnu.idi.idatt.boardgame.javafx.controllers;

import edu.ntnu.idi.idatt.boardgame.Application;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observer;
import edu.ntnu.idi.idatt.boardgame.game.PlayerManager;
import edu.ntnu.idi.idatt.boardgame.model.Color;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
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
public class MainMenuController implements Observer<PlayerManager, List<Player>> {

  private final Logger logger = Logger.getLogger(MainMenuController.class.getName());

  @Getter
  private final ObservableList<Player> players;

  @Getter
  private final ObjectProperty<Pair<String, String>> errorDialog = new SimpleObjectProperty<>(null);

  /**
   * Constructs a MainMenuController and registers it as an observer to the PlayerManager.
   */
  public MainMenuController() {
    PlayerManager.getInstance().addListener(this);
    this.players = FXCollections.observableArrayList();
  }

  /**
   * Initializes the controller by loading players from the PlayerManager.
   * <p>
   * This method is called when the main menu is loaded.
   * </p>
   */
  public void initialize() {
    this.players.setAll(PlayerManager.getInstance().getPlayers());
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
      errorDialog.set(new Pair<>("Failed to start game", e.getMessage()));
    }
  }

  /**
   * Clears the current error state.
   */
  public void clearError() {
    errorDialog.set(null);
  }

  /**
   * Adds a new player to the game.
   * <p>
   * This method is called when the user chooses to add a new player.
   * </p>
   *
   * @param player the player to add
   */
  public void addPlayer(Player player) {
    logger.info("ADDing player: " + player.getName());
    PlayerManager.getInstance().addPlayer(player);
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
    PlayerManager.getInstance().updatePlayer(player, newName, newColor);
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

  // Observers
  @Override
  public void update(List<Player> value) {
    logger.info("Updating players from observer: " + value);
    this.players.setAll(value);
  }
}
