package edu.ntnu.idi.idatt.boardgame.actions.goal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoalTileActionTest {

  GoalTileAction goalTileAction = new GoalTileAction();
  Player player;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
  }

  @Test
  void perform() {

    // Act
    boolean result = goalTileAction.perform(player);

    // Assert
    assertTrue(result, "GoalTileAction should return true when performed");
  }
}