package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GameTest {

    private Board mockBoard;
    private String validName;
    private String validDesc;
    private String validId;
    private int validMinPlayers;
    private int validMaxPlayers;
    private int validDice;
    private String validImagePath;

    @BeforeEach
    void setUp() {
        mockBoard = mock(Board.class);
        validName = "Test Game";
        validDesc = "A test game for unit testing";
        validId = "test-game";
        validMinPlayers = 2;
        validMaxPlayers = 4;
        validDice = 2;
        validImagePath = "images/test-game.png";
    }

    @Test
    void constructor_withValidParameters_shouldCreateGame() {
        Game game = new Game(mockBoard, validName, validDesc, validId, validMinPlayers,
                validMaxPlayers, validDice, validImagePath);

        assertEquals(mockBoard, game.getBoard());
        assertEquals(validName, game.getName());
        assertEquals(validDesc, game.getDescription());
        assertEquals(validId, game.getId());
        assertEquals(validMinPlayers, game.getMinPlayers());
        assertEquals(validMaxPlayers, game.getMaxPlayers());
        assertEquals(validDice, game.getNumberOfDice());
        assertTrue(game.getImagePath().isPresent());
        assertEquals(validImagePath, game.getImagePath().get());
    }

    @Test
    void constructor_withNullImagePath_shouldCreateGameWithEmptyImagePath() {
        Game game = new Game(mockBoard, validName, validDesc, validId, validMinPlayers,
                validMaxPlayers, validDice, null);

        assertFalse(game.getImagePath().isPresent());
    }

    @Test
    void constructor_withMinPlayersLessThanOne_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Game(mockBoard, validName, validDesc, validId, 0,
                        validMaxPlayers, validDice, validImagePath));

        assertTrue(exception.getMessage().contains("Minimum players must be greater than 0"));
    }

    @Test
    void constructor_withMaxPlayersLessThanMinPlayers_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Game(mockBoard, validName, validDesc, validId, validMinPlayers,
                        1, validDice, validImagePath));

        assertTrue(exception.getMessage().contains("Maximum players must be greater than or equal to minimum players"));
    }

    @Test
    void constructor_withMaxPlayersGreaterThanTen_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Game(mockBoard, validName, validDesc, validId, validMinPlayers,
                        11, validDice, validImagePath));

        assertTrue(exception.getMessage().contains(
                "Maximum players must be greater than or equal to minimum players and less than or equal to 10"));
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 11 })
    void constructor_withInvalidNumberOfDice_shouldThrowException(int dice) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Game(mockBoard, validName, validDesc, validId, validMinPlayers,
                        validMaxPlayers, dice, validImagePath));

        assertTrue(exception.getMessage().contains("Number of dice cannot be negative or greater than 10"));
    }

    @Test
    void resolveReferences_shouldCallResolveOnBoard() {
        Game game = new Game(mockBoard, validName, validDesc, validId, validMinPlayers,
                validMaxPlayers, validDice, validImagePath);

        Board anotherMockBoard = mock(Board.class);
        game.resolveReferences(anotherMockBoard);

        verify(mockBoard).resolveReferences(anotherMockBoard);
    }
}
