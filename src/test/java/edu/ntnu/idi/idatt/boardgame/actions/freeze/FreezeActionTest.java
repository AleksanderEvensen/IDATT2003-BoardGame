package edu.ntnu.idi.idatt.boardgame.actions.freeze;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class FreezeActionTest {

    private FreezeAction freezeAction;
    private Player player;

    @BeforeEach
    void setUp() {
        freezeAction = new FreezeAction();
        player = mock(Player.class);
    }

    @Test
    @DisplayName("Should freeze player when action is performed")
    void testPerformSuccess() {
        // Arrange
        when(player.isImmune()).thenReturn(false);
        when(player.isFrozen()).thenReturn(false);
        when(player.getFrozenTurns()).thenReturn(0);

        // Act
        boolean result = freezeAction.perform(player);

        // Assert
        assertTrue(result, "Freeze action should return true on successful execution");
        verify(player).setFrozenTurns(1);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when player is null")
    void testPerformNullPlayer() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> freezeAction.perform(null),
                "Should throw IllegalArgumentException when player is null");
    }

    @Test
    @DisplayName("Should not freeze immune player and decrease immunity turns")
    void testPerformImmunityPreventsFreeze() {
        // Arrange
        when(player.isImmune()).thenReturn(true);
        when(player.getImmunityTurns()).thenReturn(1);

        // Act
        boolean result = freezeAction.perform(player);

        // Assert
        assertFalse(result, "Freeze action should return false when player is immune");
        verify(player, never()).setFrozenTurns(anyInt());
        verify(player).setImmunityTurns(0);
    }

    @Test
    @DisplayName("Should not freeze already frozen player")
    void testPerformAlreadyFrozen() {
        // Arrange
        when(player.isImmune()).thenReturn(false);
        when(player.isFrozen()).thenReturn(true);

        // Act
        boolean result = freezeAction.perform(player);

        // Assert
        assertFalse(result, "Freeze action should return false when player is already frozen");
        verify(player, never()).setFrozenTurns(anyInt());
    }
}
