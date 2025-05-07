package edu.ntnu.idi.idatt.boardgame.game;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.goal.GoalTileAction;
import edu.ntnu.idi.idatt.boardgame.actions.quiz.QuizTileAction;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable;
import edu.ntnu.idi.idatt.boardgame.game.events.DiceRolledEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEndedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.GameStartedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerMovedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerSkippedTurnEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.PlayerTurnChangedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.QuestionAskedEvent;
import edu.ntnu.idi.idatt.boardgame.game.events.TileActionEvent;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.model.quiz.Question;
import edu.ntnu.idi.idatt.boardgame.model.quiz.QuestionCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
 * The controller extends the Observable class to allow UI components to observe and react to game
 * events without tight coupling.
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


  private final Logger logger = Logger.getLogger(GameController.class.getName());
  private final QuizManager quizManager;
  @Getter
  private final Game game;
  private final List<Player> players;
  private int currentPlayerIndex;
  @Getter
  private boolean gameStarted;
  @Getter
  private boolean gameEnded;
  private List<Integer> lastDiceRolls;
  private Question currentQuestion;
  private Tile checkpointTile;

  @Getter
  private Integer roundCount = 0;

  /**
   * Creates a new GameController instance.
   */
  public GameController(Game game, QuizManager quizManager) {
    super();
    this.game = game;
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    this.gameStarted = false;
    this.gameEnded = false;
    this.lastDiceRolls = new ArrayList<>();
    this.quizManager = quizManager;
  }

  /**
   * Starts the game with the specified game and players.
   *
   * @param players the list of players
   * @throws IllegalArgumentException if the game or players list is null
   * @throws IllegalStateException    if the number of players doesn't match game requirements
   */
  public void startGame(List<Player> players) {
    if (game == null) {
      throw new IllegalArgumentException("Game cannot be null");
    }
    if (players == null) {
      throw new IllegalArgumentException("Players list cannot be null");
    }
    if (players.size() < game.getMinPlayers() || players.size() > game.getMaxPlayers()) {
      throw new IllegalStateException(
          "Invalid number of players. Required: " + game.getMinPlayers() + "-"
              + game.getMaxPlayers() + ", got: " + players.size());
    }

    this.currentPlayerIndex = 0;
    this.gameStarted = true;
    this.gameEnded = false;
    this.roundCount = 1;

    /// this will prevent updating the global player list
    /// and makes sure the player state is internal within the game
    addPlayers(players);
    Tile startTile = game.getBoard().getTile(0);
    if (startTile == null) {
      throw new IllegalStateException("Game board doesn't have a start tile (ID: 0)");
    }

    for (Player player : this.players) {
      player.placeOnTile(startTile);
    }

    notifyObservers(new GameStartedEvent(game, this.players));
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

    List<Integer> diceRolls = rollDice();
    int diceValue = diceRolls.stream().mapToInt(Integer::intValue).sum();
    this.lastDiceRolls = diceRolls;

    notifyObservers(new DiceRolledEvent(currentPlayer, diceValue, diceRolls));

    Tile startTile = currentPlayer.getCurrentTile();

    int actualStepsMoved = currentPlayer.move(diceValue);
    Tile endTile = currentPlayer.getCurrentTile();

    notifyObservers(
        new PlayerMovedEvent(currentPlayer, startTile, endTile, diceValue, actualStepsMoved));

    if (endTile.getAction().isPresent()) {
      TileAction tileAction = endTile.getAction().get();
      switch (tileAction) {
        case GoalTileAction goalTileAction -> {
          gameEnded = true;
          notifyObservers(new GameEndedEvent(game, currentPlayer));
        }
        case QuizTileAction quizTileAction -> {
          initiateQuizQuestion(quizTileAction, startTile);
          return;
        }
        default -> {
          boolean triggered = tileAction.perform(currentPlayer);
          if (triggered) {
            notifyObservers(new TileActionEvent(currentPlayer, endTile, tileAction));
          }
        }
      }
    }
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
  }

  /**
   * answers the current quiz question.
   *
   * @param answer the answer to the question
   *               <p>
   *               returns true if the answer is correct, false otherwise
   */
  public boolean answerQuestion(String answer) {
    if (!gameStarted) {
      throw new IllegalStateException("Game is not started");
    }
    if (currentQuestion == null) {
      throw new IllegalStateException("No question is being asked");
    }

    boolean isCorrect = currentQuestion.getCorrectAnswer().equals(answer);
    Player currentPlayer = getCurrentPlayer();
    currentQuestion = null;

    if (!isCorrect && !currentPlayer.isImmune()) {
      placePlayerOnTile(currentPlayer, checkpointTile.getTileId());
    }

    checkpointTile = null;
    if (!gameEnded) {
      advanceToNextPlayer();
    }

    return isCorrect;
  }

  /**
   * Initiates a quiz question for the current player.
   *
   * @param tileAction the tile action that triggered the quiz
   */
  private void initiateQuizQuestion(QuizTileAction tileAction, Tile checkpointTile) {
    QuestionCategory category = tileAction.getCategory();
    Question question;
    if (category == QuestionCategory.RANDOM) {
      question = quizManager.getRandomQuestion();
    } else {
      question = quizManager.getRandomQuestionFromCategory(category);
    }
    if (question == null) {
      throw new IllegalStateException("No questions available in the selected category");
    }
    this.currentQuestion = question;
    this.checkpointTile = checkpointTile;
    notifyObservers(new QuestionAskedEvent(question, getCurrentPlayer()));

  }

  private void addPlayers(List<Player> players) {
    // create new player instances to avoid modifiying the original list
    for (Player player : players) {
      Player newPlayer = new Player(player.getName(), player.getColor());
      this.players.add(newPlayer);
    }
  }

  /**
   * Advances to the next player in turn order.
   */
  private void advanceToNextPlayer() {
    int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
    // If its the same player or a index lower than the current
    if (nextPlayerIndex <= currentPlayerIndex) {
      // THen increment the round
      roundCount++;
    }
    currentPlayerIndex = nextPlayerIndex;
    Player nextPlayer = players.get(currentPlayerIndex);
    notifyObservers(new PlayerTurnChangedEvent(nextPlayer));

    Player currentPlayer = getCurrentPlayer();

    if (currentPlayer.isFrozen()) {
      currentPlayer.setFrozenTurns(currentPlayer.getFrozenTurns() - 1);
      notifyObservers(new PlayerSkippedTurnEvent(currentPlayer, "Player is frozen"));
      advanceToNextPlayer();
    }
  }

  /**
   * Checks if a question is currently being asked.
   *
   * @return true if a question is being asked, false otherwise
   */
  public boolean isQuestionBeingAsked() {
    return currentQuestion != null;
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
