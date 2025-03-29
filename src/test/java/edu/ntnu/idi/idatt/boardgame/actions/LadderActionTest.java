package edu.ntnu.idi.idatt.boardgame.actions;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LadderActionTest {

  LadderAction ladderAction;
  LadderAction ladderAction2;
  Tile tile;
  Player player;
  @BeforeEach
  void setUp() {
    tile = new Tile(1);
    ladderAction = new LadderAction(tile);
    player = new Player(0, "Test");
  }

  @Test
  void perform() {
    // Arrange
    final Tile startTile = player.getCurrentTile();

    // Act
    ladderAction.perform(player);

    // Assert
    assertNull(startTile);
    assertThrows(IllegalArgumentException.class, () -> ladderAction.perform(null));
    assertEquals(tile, player.getCurrentTile());
  }

  @Test
  void testConstructorFailsWhenDestinationIsNull() {
    // Arrange
    // Act
    // Assert
    assertThrows(IllegalArgumentException.class, () ->     ladderAction2 = new LadderAction(null));
  }
}