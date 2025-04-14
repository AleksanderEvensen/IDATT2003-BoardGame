package edu.ntnu.idi.idatt.boardgame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable;
import edu.ntnu.idi.idatt.boardgame.game.events.DiceRolledEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEndedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameStartedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.LadderActionEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerMovedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerSkippedTurnEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerTurnChangedEvent;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

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

  private Game game;
  private List<Player> players;
  private int currentPlayerIndex;
  private boolean gameStarted;
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
    logger.info("GameController initialized");
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
      logger.severe("Attempted to start game with null game");
      throw new IllegalArgumentException("Game cannot be null");
    }
    if (players == null) {
      logger.severe("Attempted to start game with null players list");
      throw new IllegalArgumentException("Players list cannot be null");
    }
    if (players.size() < game.getMinPlayers() || players.size() > game.getMaxPlayers()) {
      logger.severe(String.format(
          "Invalid number of players. Required: %d-%d, got: %d",
          game.getMinPlayers(), game.getMaxPlayers(), players.size()));
      throw new IllegalStateException("Invalid number of players. Required: "
          + game.getMinPlayers() + "-" + game.getMaxPlayers() + ", got: " + players.size());
    }

    this.game = game;
    this.players = new ArrayList<>(players);
    this.currentPlayerIndex = 0;
    this.gameStarted = true;
    this.gameEnded = false;

    logger.info("Starting game: " + game.getName() + " (ID: " + game.getId() + ")");
    logger.info("Players: " + formatPlayerList(players));

    findLastTile();
    Tile startTile = game.getBoard().getTile(0);
    if (startTile == null) {
      logger.severe("Game board doesn't have a start tile (ID: 0)");
      throw new IllegalStateException("Game board doesn't have a start tile (ID: 0)");
    }

    for (Player player : players) {
      player.placeOnTile(startTile);
      logger.fine("Placed player " + player.getName() + " (ID: " + player.getPlayerId() + ") on start tile");
    }

    notifyObservers(new GameStartedEvent(game, players));
    logger.info("Game started successfully");
  }

  /**
   * Formats a list of players for logging purposes.
   * 
   * @param players the list of players
   * @return a string representation of the players
   */
  private String formatPlayerList(List<Player> players) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      sb.append(player.getName()).append(" (ID: ").append(player.getPlayerId()).append(")");
      if (i < players.size() - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
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
    logger.info("Last tile identified as tile ID: " + highestId);
  }

  /**
   * Rolls the dice for the current player and moves them.
   *
   * @return the total value rolled on all dice
   * @throws IllegalStateException if the game hasn't started or has ended
   */
  public int rollDiceAndMoveCurrentPlayer() {
    if (!gameStarted || gameEnded) {
      logger.warning("Attempted to roll dice when game is not in progress");
      throw new IllegalStateException("Game is not in progress");
    }

    Player currentPlayer = getCurrentPlayer();
    logger.info("Rolling dice for player: " + currentPlayer.getName() + " (ID: " + currentPlayer.getPlayerId() + ")");

    if (currentPlayer.isFrozen()) {
      currentPlayer.setFrozenTurns(currentPlayer.getFrozenTurns() - 1);
      logger.info("Player " + currentPlayer.getName() + " is frozen for " +
          (currentPlayer.getFrozenTurns() == 0 ? "this turn" : currentPlayer.getFrozenTurns() + " more turns"));
      notifyObservers(new PlayerSkippedTurnEvent(currentPlayer, "Player is frozen"));
      advanceToNextPlayer();
      return 0;
    }

    List<Integer> diceRolls = rollDice();
    int diceValue = diceRolls.stream().mapToInt(Integer::intValue).sum();
    this.lastDiceRolls = diceRolls;

    logger.info("Player " + currentPlayer.getName() + " rolled: " + diceRolls + " (total: " + diceValue + ")");
    notifyObservers(new DiceRolledEvent(currentPlayer, diceValue, diceRolls));

    Tile startTile = currentPlayer.getCurrentTile();
    logger.fine("Player starting position: Tile ID " + startTile.getTileId());

    int actualStepsMoved = currentPlayer.move(diceValue);
    Tile endTile = currentPlayer.getCurrentTile();

    logger.info("Player " + currentPlayer.getName() + " moved from tile " +
        startTile.getTileId() + " to tile " + endTile.getTileId() +
        " (" + actualStepsMoved + " steps moved)");

    notifyObservers(new PlayerMovedEvent(currentPlayer, startTile, endTile, diceValue, actualStepsMoved));

    checkGameEnd(currentPlayer);

    if (!gameEnded) {
      advanceToNextPlayer();
    }

    return diceValue;
  }

  /**
   * Rolls the dice based on the game's number of dice.
   *
   * @return a list of individual dice values
   */
  private List<Integer> rollDice() {
    int diceCount = game.getNumberOfDice();
    logger.fine("Rolling " + diceCount + " dice");
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
      logger.warning("Attempted to move player when game is not started");
      throw new IllegalStateException("Game is not started");
    }
    if (!players.contains(player)) {
      logger.warning("Attempted to move player not in the game: " + player.getName());
      throw new IllegalArgumentException("Player is not in this game");
    }

    logger.info("Moving player " + player.getName() + " by " + steps + " steps");
    Tile startTile = player.getCurrentTile();
    int actualStepsMoved = player.move(steps);
    Tile endTile = player.getCurrentTile();

    logger.info("Player " + player.getName() + " moved from tile " +
        startTile.getTileId() + " to tile " + endTile.getTileId() +
        " (" + actualStepsMoved + " steps moved)");

    notifyObservers(new PlayerMovedEvent(player, startTile, endTile, steps, actualStepsMoved));

    // Handle ladder actions
    if (startTile != endTile && endTile.getAction().isPresent() &&
        endTile.getAction().get() instanceof LadderAction) {
      Tile beforeLadderTile = endTile;
      Tile afterLadderTile = player.getCurrentTile();

      if (beforeLadderTile != afterLadderTile) {
        logger.info("Player " + player.getName() + " used ladder from tile " +
            beforeLadderTile.getTileId() + " to tile " + afterLadderTile.getTileId());
        notifyObservers(new LadderActionEvent(player, beforeLadderTile, afterLadderTile));
      }
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
      logger.warning("Attempted to place player when game is not started");
      throw new IllegalStateException("Game is not started");
    }
    if (!players.contains(player)) {
      logger.warning("Attempted to place player not in the game: " + player.getName());
      throw new IllegalArgumentException("Player is not in this game");
    }

    Tile tile = game.getBoard().getTile(tileId);
    if (tile == null) {
      logger.warning("Attempted to place player on non-existent tile ID: " + tileId);
      throw new IllegalArgumentException("Tile with ID " + tileId + " doesn't exist");
    }

    logger.info("Placing player " + player.getName() + " on tile " + tileId);
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
      logger.info("Game ended - Player " + player.getName() + " reached the final tile");
      notifyObservers(new GameEndedEvent(game, player));
    }
  }

  /**
   * Advances to the next player in turn order.
   */
  private void advanceToNextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    Player nextPlayer = players.get(currentPlayerIndex);
    logger.info("Turn advanced to player: " + nextPlayer.getName());
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
      logger.warning("Attempted to get current player when game has not started");
      throw new IllegalStateException("Game has not started");
    }

    return players.get(currentPlayerIndex);
  }

  /**
   * Gets the current game.
   *
   * @return the current game
   */
  public Game getGame() {
    return game;
  }

  /**
   * Gets the list of players.
   *
   * @return an unmodifiable list of players
   */
  public List<Player> getPlayers() {
    return List.copyOf(players);
  }

  /**
   * Checks if the game has started.
   *
   * @return true if the game has started
   */
  public boolean isGameStarted() {
    return gameStarted;
  }

  /**
   * Checks if the game has ended.
   *
   * @return true if the game has ended
   */
  public boolean isGameEnded() {
    return gameEnded;
  }
}
