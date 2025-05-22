package edu.ntnu.idi.idatt.boardgame.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.idi.idatt.boardgame.model.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Color;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LadderActionTest {

  LadderAction ladderAction;
  LadderAction ladderAction2;
  Tile tile;
  Player player;

  @BeforeEach
  void setUp() {
    tile = new Tile.Builder(1)
        .position(2, 3)
        .build();
    ladderAction = new LadderAction(tile);
    player = new Player("Test", Color.RED);
  }

  @Test
  void perform() {
    // Arrange
    final Tile startTile = player.getCurrentTile();
    player.placeOnTile(tile);

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
    assertThrows(IllegalArgumentException.class, () -> ladderAction2 = new LadderAction(null));
  }
}