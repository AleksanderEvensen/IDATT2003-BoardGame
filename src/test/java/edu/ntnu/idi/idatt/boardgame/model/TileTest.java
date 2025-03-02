package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileTest {

  Tile tile;
  Tile nextTile;
  Tile lastTile;
  TileAction action;

  @BeforeEach
  void setUp() {
    tile = new Tile(1);
    nextTile = new Tile(2);
    lastTile = new Tile(0);
    action = player -> {};
  }

  @Test
  void testConstructor() {
    // Act
    Tile newTile = new Tile(1);
    Tile newTileWithAction = new Tile(1, action);
    Tile newTileWithActionAndNextTile = new Tile(1, action, nextTile);
    Tile newTileWithActionNextTileAndLastTile = new Tile(1, action, nextTile, lastTile);

    // Assert
    assertEquals(1, newTile.getTileId());
    assertEquals(1, newTileWithAction.getTileId());
    assertEquals(1, newTileWithActionAndNextTile.getTileId());
    assertEquals(1, newTileWithActionNextTileAndLastTile.getTileId());
  }

  @Test
  void getNextTile() {
    // Arrange
    tile.setNextTile(nextTile);

    // Act
    Optional<Tile> result = tile.getNextTile();

    // Assert
    assertTrue(result.isPresent());
    assertEquals(nextTile, result.get());
  }

  @Test
  void getLastTile() {
    // Arrange
    tile.setLastTile(lastTile);

    // Act
    Optional<Tile> result = tile.getLastTile();

    // Assert
    assertTrue(result.isPresent());
    assertEquals(lastTile, result.get());
  }

  @Test
  void getTileId() {
    // Act
    int result = tile.getTileId();

    // Assert
    assertEquals(1, result);
  }

  @Test
  void getAction() {
    // Arrange
    tile.setAction(action);

    // Act
    Optional<TileAction> result = tile.getAction();

    // Assert
    assertTrue(result.isPresent());
    assertEquals(action, result.get());
  }

  @Test
  void setNextTile() {
    // Act
    tile.setNextTile(nextTile);

    // Assert
    assertEquals(nextTile, tile.getNextTile().get());
  }

  @Test
  void setLastTile() {
    // Act
    tile.setLastTile(lastTile);

    // Assert
    assertEquals(lastTile, tile.getLastTile().get());
  }

  @Test
  void setAction() {
    // Act
    tile.setAction(action);

    // Assert
    assertEquals(action, tile.getAction().get());
  }
}