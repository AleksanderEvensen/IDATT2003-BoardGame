package edu.ntnu.idi.idatt.boardgame.model;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable;
import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.goal.GoalTileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.quiz.QuizTileAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.Question;
import edu.ntnu.idi.idatt.boardgame.model.entities.QuestionCategory;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import edu.ntnu.idi.idatt.boardgame.model.events.DiceRolledEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.GameEndedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.GameEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.GameStartedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.PlayerMovedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.PlayerSkippedTurnEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.PlayerTurnChangedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.QuestionAskedEvent;
import edu.ntnu.idi.idatt.boardgame.model.events.TileActionEvent;
import edu.ntnu.idi.idatt.boardgame.model.managers.QuizManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NonNull;

/**
 * engine for managing game state, player turns, and game actions.
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
 * The engine extends the Observable class to allow UI components to observe
 * and react to game
 * events without tight coupling.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.core.reactivity.Observable
 * @see GameEvent
 * @see Game
 * @see Player
 * @see Tile
 * @since v2.0.0
 */
public class GameEngine extends Observable<GameEngine, GameEvent> {

  private final Logger logger = Logger.getLogger(GameEngine.class.getName());
  private final QuizManager quizManager;
  @Getter
  private final Game game;
  private final List<Player> players;
  private int currentPlayerIndex;
  @Getter
  private boolean gameStarted;
  @Getter
  private boolean gameEnded;
  private Question currentQuestion;
  private Tile checkpointTile;

  @Getter
  private int roundCount = 0;

  /**
   * Constructs a GameEngine with the specified game, quiz manager, and players.
   *
   * @param game        the game to be played
   * @param quizManager the quiz manager for handling quiz questions
   * @param players     the list of players participating in the game
   * @throws IllegalArgumentException if game or players are null
   * @throws IllegalStateException    if the number of players is not within the valid range
   */
  public GameEngine(@NonNull Game game, @NonNull QuizManager quizManager,
      @NonNull List<Player> players) {
    super();
    this.game = game;
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    this.gameStarted = false;
    this.gameEnded = false;
    this.quizManager = quizManager;

    if (players.size() < game.getMinPlayers() || players.size() > game.getMaxPlayers()) {
      throw new IllegalStateException(
          "Invalid number of players. Required: " + game.getMinPlayers() + "-"
              + game.getMaxPlayers() + ", got: " + players.size());
    }
    /// this will prevent updating the global player list
    /// and makes sure the player state is internal within the game
    addPlayers(players);
  }

  /**
   * Starts the game with the specified game and players.
   */
  public void startGame() {
    this.currentPlayerIndex = 0;
    this.gameStarted = true;
    this.gameEnded = false;
    this.roundCount = 1;

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

    notifyObservers(new DiceRolledEvent(currentPlayer, diceValue, diceRolls));

    Tile startTile = currentPlayer.getCurrentTile();

    int actualStepsMoved = currentPlayer.move(diceValue);
    Tile endTile = currentPlayer.getCurrentTile();

    notifyObservers(
        new PlayerMovedEvent(currentPlayer, startTile, endTile, diceValue, actualStepsMoved));

    if (endTile.getAction().isPresent()) {
      TileAction action = endTile.getAction().get();
      switch (action) {
        case GoalTileAction goalAction -> {
          gameEnded = true;
          notifyObservers(new GameEndedEvent(game, currentPlayer));
        }
        case QuizTileAction quizAction -> {
          initiateQuizQuestion(quizAction, startTile);
          return;
        }
        default -> {
          boolean triggered = action.perform(currentPlayer);
          if (triggered) {
            notifyObservers(new TileActionEvent(currentPlayer, endTile, action));
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
   */
  public Player getCurrentPlayer() {
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
