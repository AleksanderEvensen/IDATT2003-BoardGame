package edu.ntnu.idi.idatt.boardgame.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.model.entities.Game;
import edu.ntnu.idi.idatt.boardgame.model.managers.GameManager;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GameManagerTest {

  private LocalFileProvider mockFileProvider;
  private GameManager gameManager;
  private String testGameJson;

  @BeforeEach
  void setUp() {
    mockFileProvider = mock(LocalFileProvider.class);

    // Sample game JSON
    testGameJson = """
        {
          "id": "simple-game",
          "name": "Simple Game",
          "description": "A simple test game",
          "minPlayers": 2,
          "maxPlayers": 4,
          "numberOfDice": 1,
          "board": {
            "tiles": {
              "0": {
                "tileId": 0,
                "row": 9,
                "col": 0,
                "nextTileId": 1,
                "previousTileId": null
              },
              "1": {
                "tileId": 1,
                "row": 9,
                "col": 1,
                "nextTileId": 2,
                "previousTileId": 0,
                "action": {
                  "type": "QUIZ",
                  "category": "RANDOM"
                }
              },
              "2": {
                "tileId": 2,
                "row": 9,
                "col": 2,
                "nextTileId": -1,
                "previousTileId": 1
              }
            }
          }
        }
        """;

    // Set up default game directory
    when(mockFileProvider.listFiles("data/games"))
        .thenReturn(List.of("game1.json", "game2.json", "README.txt"));

    when(mockFileProvider.get("data/games/game1.json"))
        .thenReturn(testGameJson.getBytes());

    when(mockFileProvider.get("data/games/game2.json"))
        .thenReturn(
            testGameJson.replace("\"id\": \"simple-game\"", "\"id\": \"other-game\"").getBytes());

    // Initialize with mock
    gameManager = new GameManager(mockFileProvider);
  }

  @Test
  void loadGame_shouldLoadGameFromJson() {
    String customGamePath = "custom/path/game3.json";
    when(mockFileProvider.get(customGamePath))
        .thenReturn(
            testGameJson.replace("\"id\": \"simple-game\"", "\"id\": \"custom-game\"").getBytes());

    gameManager.loadGame(customGamePath);

    Game loadedGame = gameManager.getGame("custom-game");
    assertNotNull(loadedGame);
    assertEquals("custom-game", loadedGame.getId());
  }

  @Test
  void getGame_withValidId_shouldReturnGame() {
    Game game = gameManager.getGame("simple-game");

    assertNotNull(game);
    assertEquals("simple-game", game.getId());
    assertEquals("Simple Game", game.getName());
    assertEquals(2, game.getMinPlayers());
    assertEquals(4, game.getMaxPlayers());
  }

  @Test
  void getGame_withInvalidId_shouldReturnNull() {
    Game game = gameManager.getGame("non-existent-game");

    assertNull(game);
  }

  @Test
  void getAvailableGameIds_shouldReturnSetOfIds() {
    Set<String> gameIds = gameManager.getAvailableGameIds();

    assertNotNull(gameIds);
    assertTrue(gameIds.contains("simple-game"));
    assertTrue(gameIds.contains("other-game"));
    assertEquals(2, gameIds.size());
  }

  @Test
  void getInstance_shouldReturnSingletonInstance() {
    try (MockedStatic<GameManager> mockedStatic = mockStatic(GameManager.class,
        invocation -> invocation.getMethod().getName().equals("getInstance")
            ? gameManager
            : invocation.callRealMethod())) {

      GameManager instance = GameManager.getInstance();
      assertSame(gameManager, instance);

      mockedStatic.verify(() -> GameManager.getInstance());
    }
  }
}
