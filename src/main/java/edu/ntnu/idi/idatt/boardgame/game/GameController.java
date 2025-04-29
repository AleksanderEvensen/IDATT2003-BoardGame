package edu.ntnu.idi.idatt.boardgame.game;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.game.events.TileActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable;
import edu.ntnu.idi.idatt.boardgame.game.events.DiceRolledEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEndedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameStartedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerMovedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerSkippedTurnEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerTurnChangedEvent;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import lombok.Getter;

/**
 * Controller for managing game state, player turns, and game actions.
 * <p>
 * This controller manages all aspects of game flow, including:
 * <ul>
 * <li>Starting and ending games</li>
 * <li>Managing player turns</li>
 * <li>Rolling dice and moving players</li>
 * <li>Handling special tile actions like ladders</li>
 * </ul>
 * </p>
 * <p>
 * The controller extends the Observable class to allow UI components
 * to observe and react to game events without tight coupling.
 * </p>
 * 
 * @see edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable
 * @see edu.ntnu.idi.idatt.boardgame.game.events.GameEvent
 * @see edu.ntnu.idi.idatt.boardgame.model.Game
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @see edu.ntnu.idi.idatt.boardgame.model.Tile
 * @since v2.0.0
 */
public class GameController extends Observable<GameController, GameEvent> {


  @Getter
  private Game game;
  private List<Player> players;
  private int currentPlayerIndex;

  @Getter
  private boolean gameStarted;

  @Getter
  private boolean gameEnded;
  private Tile lastTile;
  private List<Integer> lastDiceRolls;
  private final Logger logger = Logger.getLogger(GameController.class.getName());

  /**
   * Creates a new GameController instance.
   */
  public GameController() {
    super();
    this.game = null;
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    this.gameStarted = false;
    this.gameEnded = false;
    this.lastDiceRolls = new ArrayList<>();
  }

  /**
   * Starts the game with the specified game and players.
   *
   * @param game    the game to start
   * @param players the list of players
   * @throws IllegalArgumentException if the game or players list is null
   * @throws IllegalStateException    if the number of players doesn't match game
   *                                  requirements
   */
  public void startGame(Game game, List<Player> players) {
    if (game == null) {
      throw new IllegalArgumentException("Game cannot be null");
    }
    if (players == null) {
      throw new IllegalArgumentException("Players list cannot be null");
    }
    if (players.size() < game.getMinPlayers() || players.size() > game.getMaxPlayers()) {
      throw new IllegalStateException("Invalid number of players. Required: "
          + game.getMinPlayers() + "-" + game.getMaxPlayers() + ", got: " + players.size());
    }

    this.game = game;
    this.players = new ArrayList<>(players);
    this.currentPlayerIndex = 0;
    this.gameStarted = true;
    this.gameEnded = false;

    findLastTile();
    Tile startTile = game.getBoard().getTile(0);
    if (startTile == null) {
      throw new IllegalStateException("Game board doesn't have a start tile (ID: 0)");
    }

    for (Player player : players) {
      player.placeOnTile(startTile);
    }

    notifyObservers(new GameStartedEvent(game, players));
  }

  /**
   * Finds the tile with the highest ID, which is considered the last tile.
   */
  private void findLastTile() {
    Board board = game.getBoard();
    int highestId = -1;
    Tile highestTile = null;

    for (Integer tileId : board.getTiles().keySet()) {
      if (tileId > highestId) {
        highestId = tileId;
        highestTile = board.getTile(tileId);
      }
    }

    this.lastTile = highestTile;
  }

  /**
   * Rolls the dice for the current player and moves them.
   *
   * @throws IllegalStateException if the game hasn't started or has ended
   */
  public void rollDiceAndMoveCurrentPlayer() {
    if (!gameStarted || gameEnded) {
      throw new IllegalStateException("Game is not in progress");
    }

    Player currentPlayer = getCurrentPlayer();

    if (currentPlayer.isFrozen()) {
      currentPlayer.setFrozenTurns(currentPlayer.getFrozenTurns() - 1);
      notifyObservers(new PlayerSkippedTurnEvent(currentPlayer, "Player is frozen"));
      advanceToNextPlayer();
      return;
    }

    List<Integer> diceRolls = rollDice();
    int diceValue = diceRolls.stream().mapToInt(Integer::intValue).sum();
    this.lastDiceRolls = diceRolls;

    notifyObservers(new DiceRolledEvent(currentPlayer, diceValue, diceRolls));

    Tile startTile = currentPlayer.getCurrentTile();

    int actualStepsMoved = currentPlayer.move(diceValue);
    Tile endTile = currentPlayer.getCurrentTile();

    notifyObservers(new PlayerMovedEvent(currentPlayer, startTile, endTile, diceValue, actualStepsMoved));

    if (endTile.getAction().isPresent()) {
      TileAction tileAction = endTile.getAction().get();
      tileAction.perform(currentPlayer);
      notifyObservers(new TileActionEvent(currentPlayer, endTile, tileAction));
    }

    checkGameEnd(currentPlayer);

    if (!gameEnded) {
      advanceToNextPlayer();
    }

  }

  /**
   * Rolls the dice based on the game's number of dice.
   *
   * @return a list of individual dice values
   */
  private List<Integer> rollDice() {
    int diceCount = game.getNumberOfDice();
    return Utils.throwDice(diceCount);
  }

  /**
   * Gets the results of the last dice roll.
   *
   * @return a list of the individual dice values from the last roll
   */
  public List<Integer> getLastDiceRolls() {
    return List.copyOf(lastDiceRolls);
  }

  /**
   * Moves a specified player by a given number of steps.
   *
   * @param player the player to move
   * @param steps  the number of steps to move
   * @return the number of steps the player actually moved
   * @throws IllegalStateException    if the game hasn't started
   * @throws IllegalArgumentException if the player is not in the game
   */
  public int movePlayer(Player player, int steps) {
    if (!gameStarted) {
      throw new IllegalStateException("Game is not started");
    }
    if (!players.contains(player)) {
      throw new IllegalArgumentException("Player is not in this game");
    }

    Tile startTile = player.getCurrentTile();
    int actualStepsMoved = player.move(steps);
    Tile endTile = player.getCurrentTile();

    notifyObservers(new PlayerMovedEvent(player, startTile, endTile, steps, actualStepsMoved));

    if (endTile.getAction().isPresent()) {
      TileAction tileAction = endTile.getAction().get();
      tileAction.perform(player);
      notifyObservers(new TileActionEvent(player, endTile, tileAction));
    }

    checkGameEnd(player);

    return actualStepsMoved;
  }

  /**
   * Places a player on a specific tile.
   *
   * @param player the player to place
   * @param tileId the ID of the tile to place the player on
   * @throws IllegalStateException    if the game hasn't started
   * @throws IllegalArgumentException if the player is not in the game
   * @throws IllegalArgumentException if the tile doesn't exist
   */
  public void placePlayerOnTile(Player player, int tileId) {
    if (!gameStarted) {
      throw new IllegalStateException("Game is not started");
    }
    if (!players.contains(player)) {
      throw new IllegalArgumentException("Player is not in this game");
    }

    Tile tile = game.getBoard().getTile(tileId);
    if (tile == null) {
      throw new IllegalArgumentException("Tile with ID " + tileId + " doesn't exist");
    }

    Tile oldTile = player.getCurrentTile();
    player.placeOnTile(tile);

    notifyObservers(new PlayerMovedEvent(player, oldTile, tile, 0, 0));
    checkGameEnd(player);
  }

  /**
   * Checks if the game has ended (a player has reached the last tile).
   *
   * @param player the player to check
   */
  private void checkGameEnd(Player player) {
    if (lastTile != null && player.getCurrentTile() == lastTile) {
      gameEnded = true;
      notifyObservers(new GameEndedEvent(game, player));
    }
  }

  /**
   * Advances to the next player in turn order.
   */
  private void advanceToNextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    Player nextPlayer = players.get(currentPlayerIndex);
    notifyObservers(new PlayerTurnChangedEvent(nextPlayer));
  }

  /**
   * Gets the current player whose turn it is.
   *
   * @return the current player
   * @throws IllegalStateException if the game hasn't started
   */
  public Player getCurrentPlayer() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }

    return players.get(currentPlayerIndex);
  }

  /**
   * Gets the list of players.
   *
   * @return an unmodifiable list of players
   */
  public List<Player> getPlayers() {
    return List.copyOf(players);
  }

}
