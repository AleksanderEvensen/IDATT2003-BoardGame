package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt.boardgame.model.entities.Color;
import edu.ntnu.idi.idatt.boardgame.model.entities.Player;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

  Player player;
  Tile startTile;
  Tile nextTile;
  Tile lastTile;

  @BeforeEach
  void setUp() {
    player = new Player("Player1", Color.RED);
    startTile = new Tile.Builder(1).build();
    nextTile = new Tile.Builder(2).build();
    lastTile = new Tile.Builder(3).build();

    startTile.setNextTile(nextTile);
    nextTile.setPreviousTile(startTile);
    nextTile.setNextTile(lastTile);
    lastTile.setPreviousTile(nextTile);

    player.placeOnTile(startTile);
  }

  @Test
  void getName() {
    assertEquals("Player1", player.getName());
  }

  @Test
  void getColor() {
    assertEquals(Color.RED, player.getColor());
  }

  @Test
  void getCurrentTile() {
    assertEquals(startTile, player.getCurrentTile());
  }

  @Test
  void placeOnTile() {
    Tile newTile = new Tile.Builder(4).build();
    player.placeOnTile(newTile);
    assertEquals(newTile, player.getCurrentTile());
  }

  @Test
  void moveOneTileForward() {
    boolean moved = player.moveOneTile(true);
    assertTrue(moved);
    assertEquals(nextTile, player.getCurrentTile());
  }

  @Test
  void moveOneTileBackward() {
    player.placeOnTile(nextTile);
    boolean moved = player.moveOneTile(false);
    assertTrue(moved);
    assertEquals(startTile, player.getCurrentTile());
  }

  @Test
  void move() {
    int stepsMoved = player.move(2);
    assertEquals(2, stepsMoved);
    assertEquals(lastTile, player.getCurrentTile());
  }

  @Test
  void moveToTile() {
    Tile newTile = new Tile.Builder(5).build();
    player.moveToTile(newTile);
    assertEquals(newTile, player.getCurrentTile());
  }

  @Test
  void moveToTileWithAction() {
    Tile newTile = new Tile.Builder(6).build();
    player.moveToTile(newTile, false);
    assertEquals(newTile, player.getCurrentTile());
  }

  @Test
  void isImmune() {
    player.setImmunityTurns(2);
    assertTrue(player.isImmune());

    player.setImmunityTurns(0);
    assertFalse(player.isImmune());
  }

  @Test
  void testEquals() {
    Player samePlayer = new Player("Player1", Color.RED);
    Player differentPlayer = new Player("Player2", Color.BLUE);

    assertTrue(player.equals(samePlayer));
    assertFalse(player.equals(differentPlayer));
    assertFalse(player.equals(null));
    assertFalse(player.equals(new Object()));
  }

  @Test
  void setImmunityTurns() {
    player.setImmunityTurns(3);
    assertTrue(player.isImmune());
    assertEquals(3, player.getImmunityTurns());

    player.setImmunityTurns(0);
    assertFalse(player.isImmune());
    assertEquals(0, player.getImmunityTurns());
  }

  @Test
  void setFrozenTurns_shouldThrowExceptionForNegativeValue() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> player.setFrozenTurns(-1));
    assertEquals("Frozen turns cannot be negative", exception.getMessage());
  }

  @Test
  void setImmunityTurns_shouldThrowExceptionForNegativeValue() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> player.setImmunityTurns(-1));
    assertEquals("Immunity turns cannot be negative", exception.getMessage());
  }


  @Test
  void constructor_shouldThrowExceptionWhenNameIsNull() {
    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Player(null, Color.RED));
    assertEquals("Name cannot be null or empty and must be less than 20 characters",
        exception.getMessage());
  }

  @Test
  void constructor_shouldThrowExceptionWhenNameIsEmpty() {
    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Player("", Color.RED));
    assertEquals("Name cannot be null or empty and must be less than 20 characters",
        exception.getMessage());
  }

  @Test
  void constructor_shouldThrowExceptionWhenNameExceedsMaxLength() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Player("ThisNameIsWayTooLongToBeValid", Color.RED));
    assertEquals("Name cannot be null or empty and must be less than 20 characters",
        exception.getMessage());
  }

  @Test
  void constructor_shouldThrowExceptionWhenColorIsNull() {
    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Player("Player1", null));
    assertEquals("Color cannot be null", exception.getMessage());
  }

}