package edu.ntnu.idi.idatt.boardgame;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.boardgame.game.ladder.LadderBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LadderGameTest {

  LadderGame ladderGame;

  @BeforeEach
  void setUp() {
    ladderGame = new LadderGame();
  }

  @Test
  void getBoard() {
    // Arrange

    // Act
    LadderBoard board = ladderGame.getBoard();

    // Assert
    assertNotNull(board);
    assertEquals(91, board.getTiles().size());
  }
}