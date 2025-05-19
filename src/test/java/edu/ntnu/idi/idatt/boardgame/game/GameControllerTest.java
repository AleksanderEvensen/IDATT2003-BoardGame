package edu.ntnu.idi.idatt.boardgame.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.actions.goal.GoalTileAction;
import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Color;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.model.quiz.Question;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GameControllerTest {

  private Game game;
  private Board board;
  private Tile startTile;
  private Tile tile1;
  private QuizManager quizManager;
  private GameController controller;

  @BeforeEach
  void setUp() {
    game = mock(Game.class);
    board = mock(Board.class);
    startTile = mock(Tile.class);
    tile1 = mock(Tile.class);
    quizManager = mock(QuizManager.class);

    when(game.getMinPlayers()).thenReturn(2);
    when(game.getMaxPlayers()).thenReturn(4);
    when(game.getNumberOfDice()).thenReturn(2);
    when(game.getBoard()).thenReturn(board);

    when(startTile.getTileId()).thenReturn(0);
    when(startTile.getAction()).thenReturn(Optional.empty());
    when(tile1.getTileId()).thenReturn(1);
    when(tile1.getAction()).thenReturn(Optional.empty());

    when(board.getTile(0)).thenReturn(startTile);
    when(board.getTile(anyInt())).thenAnswer(i -> {
      int id = i.getArgument(0);
      if (id == 0) {
        return startTile;
      }
      return tile1;
    });

    controller = new GameController(game, quizManager);
  }

  private Player newPlayer(String name) {
    Player p = mock(Player.class);
    when(p.getName()).thenReturn(name);
    when(p.getColor()).thenReturn(Color.RED);
    return p;
  }

  @Test
  @DisplayName("startGame happy path")
  void startGameOk() {
    controller.startGame(List.of(newPlayer("a"), newPlayer("b")));
    assertTrue(controller.isGameStarted());
    assertEquals(1, controller.getRoundCount());
    assertEquals(2, controller.getPlayers().size());
  }

  @Test
  @DisplayName("startGame with null player list throws")
  void startGameNullPlayers() {
    assertThrows(IllegalArgumentException.class, () -> controller.startGame(null));
  }

  @Test
  @DisplayName("rollDice before start throws")
  void rollDiceBeforeStart() {
    assertThrows(IllegalStateException.class, controller::rollDiceAndMoveCurrentPlayer);
  }

  @Test
  @DisplayName("rollDice normal move records dice")
  void rollDiceRecordsDice() {
    controller.startGame(List.of(newPlayer("a"), newPlayer("b")));
    try (MockedStatic<Utils> st = mockStatic(Utils.class)) {
      st.when(() -> Utils.throwDice(2)).thenReturn(List.of(1, 1));
      controller.rollDiceAndMoveCurrentPlayer();
      assertEquals(List.of(1, 1), controller.getLastDiceRolls());
    }
  }

  @Test
  @DisplayName("round count increments after a full cycle")
  void roundCountIncrements() {
    controller.startGame(List.of(newPlayer("a"), newPlayer("b")));
    try (MockedStatic<Utils> st = mockStatic(Utils.class)) {
      st.when(() -> Utils.throwDice(2)).thenReturn(List.of(1, 1));
      controller.rollDiceAndMoveCurrentPlayer();
      controller.rollDiceAndMoveCurrentPlayer();
      assertEquals(2, controller.getRoundCount());
    }
  }


  @Test
  @DisplayName("goal tile ends game")
  void goalTileEndsGame() {
    Tile goalTile = mock(Tile.class);
    when(goalTile.getTileId()).thenReturn(2);
    when(goalTile.getAction()).thenReturn(Optional.of(new GoalTileAction()));
    when(board.getTile(anyInt())).thenReturn(goalTile);

    controller.startGame(List.of(newPlayer("a"), newPlayer("b")));
    try (MockedStatic<Utils> st = mockStatic(Utils.class)) {
      st.when(() -> Utils.throwDice(2)).thenReturn(List.of(2, 0));
      controller.rollDiceAndMoveCurrentPlayer();
      assertTrue(controller.isGameEnded());
    }
  }

  @Test
  @DisplayName("answerQuestion returns true on correct answer")
  void answerQuestionCorrect() throws Exception {
    controller.startGame(List.of(newPlayer("a"), newPlayer("b")));
    Question q = mock(Question.class);
    when(q.getCorrectAnswer()).thenReturn("X");
    Field cq = GameController.class.getDeclaredField("currentQuestion");
    cq.setAccessible(true);
    cq.set(controller, q);
    Field cp = GameController.class.getDeclaredField("checkpointTile");
    cp.setAccessible(true);
    cp.set(controller, startTile);
    boolean res = controller.answerQuestion("X");
    assertTrue(res);
    assertFalse(controller.isQuestionBeingAsked());
  }

  @Test
  @DisplayName("answerQuestion places player on checkpoint when wrong")
  void answerQuestionWrong() throws Exception {
    Tile otherTile = mock(Tile.class);
    when(otherTile.getTileId()).thenReturn(5);
    when(otherTile.getAction()).thenReturn(Optional.empty());
    when(board.getTile(5)).thenReturn(otherTile);

    controller.startGame(List.of(newPlayer("a"), newPlayer("b")));
    Player internal = controller.getCurrentPlayer();
    controller.placePlayerOnTile(internal, 5);

    Question q = mock(Question.class);
    when(q.getCorrectAnswer()).thenReturn("X");
    Field cq = GameController.class.getDeclaredField("currentQuestion");
    cq.setAccessible(true);
    cq.set(controller, q);
    Field cp = GameController.class.getDeclaredField("checkpointTile");
    cp.setAccessible(true);
    cp.set(controller, startTile);

    boolean res = controller.answerQuestion("Y");
    assertFalse(res);
    assertEquals(startTile, internal.getCurrentTile());
  }
}
