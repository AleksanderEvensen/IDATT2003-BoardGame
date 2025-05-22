package edu.ntnu.idi.idatt.boardgame.actions.immunity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.idi.idatt.boardgame.model.actions.immunity.ImmunityAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImmunityActionTest {

  private ImmunityAction immunityAction;
  private Player player;

  @BeforeEach
  void setUp() {
    immunityAction = new ImmunityAction();
    player = mock(Player.class);
  }

  @Test
  @DisplayName("Should grant immunity when action is performed")
  void testPerformSuccess() {
    // Arrange
    when(player.getImmunityTurns()).thenReturn(0);

    // Act
    boolean result = immunityAction.perform(player);

    // Assert
    assertTrue(result, "Immunity action should return true on successful execution");
    verify(player).setImmunityTurns(1);
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when player is null")
  void testPerformNullPlayer() {
    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> immunityAction.perform(null),
        "Should throw IllegalArgumentException when player is null");
  }

  @Test
  @DisplayName("Should add one turn to existing immunity turns")
  void testPerformAddToExistingImmunity() {
    // Arrange
    when(player.getImmunityTurns()).thenReturn(2);

    // Act
    boolean result = immunityAction.perform(player);

    // Assert
    assertTrue(result, "Immunity action should return true when adding to existing immunity");
    verify(player).setImmunityTurns(3);
  }
}
