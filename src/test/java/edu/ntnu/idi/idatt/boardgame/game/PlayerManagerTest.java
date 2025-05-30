package edu.ntnu.idi.idatt.boardgame.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.idi.idatt.boardgame.core.filesystem.LocalFileProvider;
import edu.ntnu.idi.idatt.boardgame.model.entities.Color;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.managers.PlayerManager;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class PlayerManagerTest {

  private LocalFileProvider mockFileProvider;
  private PlayerManager playerManager;

  @BeforeEach
  void setUp() {
    mockFileProvider = mock(LocalFileProvider.class);
    String testPlayerData = "John;#FF0000\nJane;#00FF00";
    when(mockFileProvider.get("data/players.csv")).thenReturn(testPlayerData.getBytes());

    PlayerManager.init(() -> mockFileProvider);
    playerManager = PlayerManager.getInstance();
    playerManager.loadPlayers("data/players.csv");
  }

  @Test
  void loadPlayers_shouldLoadPlayersFromCSV() {
    // Since loadPlayers is called in constructor, just verify players are loaded
    // correctly
    List<Player> players = playerManager.getPlayers();

    assertEquals(2, players.size());

    Player player1 = players.getFirst();
    assertEquals("John", player1.getName());
    assertEquals(Color.fromHex("#FF0000"), player1.getColor());

    Player player2 = players.get(1);
    assertEquals("Jane", player2.getName());
    assertEquals(Color.fromHex("#00FF00"), player2.getColor());
  }

  @Test
  void savePlayers_shouldWritePlayersToCSV() {
    ArgumentCaptor<byte[]> dataCaptor = ArgumentCaptor.forClass(byte[].class);

    playerManager.savePlayers("data/players.csv");

    verify(mockFileProvider).save(eq("data/players.csv"), dataCaptor.capture());
    String savedData = new String(dataCaptor.getValue());

    // Check that each player is in the saved data
    for (Player player : playerManager.getPlayers()) {
      String expectedLine = player.getName() + ";" + player.getColor().toHex();
      assertTrue(savedData.contains(expectedLine));
    }
  }

  @Test
  void addPlayer_shouldAddPlayerToList() {
    int initialCount = playerManager.getPlayers().size();
    Player newPlayer = new Player("Alice", Color.fromHex("#0000FF"));

    boolean result = playerManager.addPlayer(newPlayer);

    assertTrue(result);
    assertEquals(initialCount + 1, playerManager.getPlayers().size());
    assertTrue(playerManager.getPlayers().stream()
        .anyMatch(
            p -> p.getName().equals("Alice") && p.getColor().equals(Color.fromHex("#0000FF"))));
  }

  @Test
  void addPlayer_withNullPlayer_shouldReturnFalse() {
    int initialCount = playerManager.getPlayers().size();

    boolean result = playerManager.addPlayer(null);

    assertFalse(result);
    assertEquals(initialCount, playerManager.getPlayers().size());
  }

  @Test
  void removePlayer_byId_shouldRemovePlayerAtIndex() {
    int initialCount = playerManager.getPlayers().size();
    playerManager.removePlayer(0);

    List<Player> remainingPlayers = playerManager.getPlayers();
    assertEquals(initialCount - 1, remainingPlayers.size());
    assertEquals("Jane", remainingPlayers.getFirst().getName());
  }

  @Test
  void updatePlayer_shouldUpdateExistingPlayer() {
    Player updatedPlayer = new Player("John Updated", Color.fromHex("#0000FF"));

    playerManager.updatePlayer(0, updatedPlayer);

    Player resultPlayer = playerManager.getPlayers().getFirst();
    assertEquals("John Updated", resultPlayer.getName());
    assertEquals(Color.fromHex("#0000FF"), resultPlayer.getColor());
  }

  @Test
  void getInstance_shouldReturnSingletonInstance() {
    PlayerManager instance1 = PlayerManager.getInstance();
    PlayerManager instance2 = PlayerManager.getInstance();

    assertNotNull(instance1);
    assertSame(instance1, instance2);
  }

  @AfterEach
  void tearDown() throws Exception {
    Field instanceField = PlayerManager.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, null);
  }
}
