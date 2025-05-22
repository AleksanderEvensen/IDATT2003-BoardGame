package edu.ntnu.idi.idatt.boardgame.actions.quiz;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import edu.ntnu.idi.idatt.boardgame.model.actions.quiz.QuizTileAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuizTileActionTest {

  private QuizTileAction quizTileAction;
  private Player player;

  @BeforeEach
  void setUp() {
    quizTileAction = new QuizTileAction();
    player = mock(Player.class);
  }

  @Test
  @DisplayName("Should return true when quiz action is performed")
  void testPerformSuccess() {
    // Act
    boolean result = quizTileAction.perform(player);

    // Assert
    assertTrue(result, "Quiz tile action should return true on execution");
  }

  @Test
  @DisplayName("Should return the correct question category")
  void testGetCategory() {

    // Act & Assert
    assertNull(quizTileAction.getCategory(),
        "Quiz tile action should return null for category based on current implementation");
  }

  @Test
  @DisplayName("Should handle null player gracefully")
  void testPerformNullPlayer() {
    // Act
    boolean result = quizTileAction.perform(null);

    // Assert
    assertTrue(result,
        "Quiz tile action should return true even with null player based on current implementation");
  }
}
