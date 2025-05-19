package edu.ntnu.idi.idatt.boardgame.actions.ladder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ntnu.idi.idatt.boardgame.model.Board;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class LadderActionTest {

    private LadderAction ladderAction;
    private Tile sourceTile;
    private Tile destinationTile;
    private Player player;
    private Board board;

    @BeforeEach
    void setUp() {
        // Create mocks
        sourceTile = mock(Tile.class);
        destinationTile = mock(Tile.class);
        player = mock(Player.class);
        board = mock(Board.class);

        // Set up common behavior
        when(sourceTile.getTileId()).thenReturn(5);
        when(destinationTile.getTileId()).thenReturn(10);
        when(player.getCurrentTile()).thenReturn(sourceTile);
        when(board.getTile(10)).thenReturn(destinationTile);

        // Create the action
        ladderAction = new LadderAction(destinationTile);
    }

    @Test
    @DisplayName("Should move player when ladder action is performed")
    void testPerformSuccess() {
        // Act
        boolean result = ladderAction.perform(player);

        // Assert
        assertTrue(result, "Ladder action should return true on successful execution");
        verify(player).moveToTile(destinationTile, false);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when player is null")
    void testPerformNullPlayer() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> ladderAction.perform(null),
                "Should throw IllegalArgumentException when player is null");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when player is not on a tile")
    void testPerformPlayerNotOnTile() {
        // Arrange
        when(player.getCurrentTile()).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> ladderAction.perform(player),
                "Should throw IllegalArgumentException when player is not on a tile");
    }

    @Test
    @DisplayName("Should not move immune player when destination tile is lower than current tile")
    void testPerformImmunityPreventsDownwardMove() {
        // Arrange
        when(sourceTile.getTileId()).thenReturn(15);
        when(destinationTile.getTileId()).thenReturn(10);
        when(player.isImmune()).thenReturn(true);
        when(player.getImmunityTurns()).thenReturn(1);

        // Act
        boolean result = ladderAction.perform(player);

        // Assert
        assertFalse(result, "Ladder action should return false when immune player would move downward");
        verify(player, never()).moveToTile(any(Tile.class), anyBoolean());
        verify(player).setImmunityTurns(0);
    }

    @Test
    @DisplayName("Should move immune player when destination tile is higher than current tile")
    void testPerformImmunityAllowsUpwardMove() {
        // Arrange
        when(player.isImmune()).thenReturn(true);

        // Act
        boolean result = ladderAction.perform(player);

        // Assert
        assertTrue(result, "Ladder action should return true when immune player moves upward");
        verify(player).moveToTile(destinationTile, false);
    }

    @Test
    @DisplayName("Should resolve references from board correctly")
    void testResolveReferences() {
        // Arrange
        LadderAction ladderActionWithId = new LadderAction(10);

        // Act
        ladderActionWithId.resolveReferences(board);

        // Assert
        assertEquals(destinationTile, ladderActionWithId.getDestinationTile(),
                "LadderAction should resolve the correct destination tile from the board");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when destination tile is null in constructor")
    void testConstructorNullDestinationTile() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LadderAction((Tile) null),
                "Constructor should throw IllegalArgumentException when destination tile is null");
    }
}
