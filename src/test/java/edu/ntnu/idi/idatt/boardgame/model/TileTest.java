package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.boardgame.actions.LadderAction;
import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileTest {

  protected Tile tile;
  protected Tile nextTile;
  protected TileAction tileAction;

  @BeforeEach
  void setUp() {
    tile = new Tile(1);
    nextTile = new Tile(2);
    tileAction = new LadderAction(nextTile);
    tile.setAction(tileAction);

  }

  @Test
  void getNextTile() {
    assertEquals(Optional.empty(), tile.getNextTile());
    tile.setNextTile(nextTile);
    assertNotNull(tile.getNextTile());
    assertEquals(nextTile, tile.getNextTile().get());
  }

  @Test
  void getLastTile() {
    assertEquals(Optional.empty(), tile.getLastTile());
    tile.setLastTile(nextTile);
    assertNotNull(tile.getLastTile());
    assertEquals(nextTile, tile.getLastTile().get());
  }

  @Test
  void getTileId() {
    assertEquals(1, tile.getTileId());

  }

  @Test
  void getAction() {
    assertNotNull(tile.getAction());
    assertEquals(tileAction, tile.getAction().get());
  }

  @Test
  void setNextTile() {
    tile.setNextTile(nextTile);
    assertEquals(nextTile, tile.getNextTile().get());
  }

  @Test
  void setLastTile() {
    tile.setLastTile(nextTile);
    assertEquals(nextTile, tile.getLastTile().get());
  }

  @Test
  void setAction() {
    TileAction newAction = new LadderAction(tile);
    nextTile.setAction(newAction);
    assertEquals(newAction, nextTile.getAction().get());
  }
}