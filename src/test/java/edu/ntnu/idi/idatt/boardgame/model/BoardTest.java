package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt.boardgame.model.entities.Board;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import edu.ntnu.idi.idatt.boardgame.model.exceptions.BoardRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Board class.
 */
class BoardTest {

  private Board board;

  @BeforeEach
  void setUp() {
    board = new Board(5, 5);
  }

  @Test
  void addTile() {
    Tile tile = new Tile.Builder(1).position(0, 0).build();
    Tile tile2 = new Tile.Builder(2).position(1, 1).build();

    board.addTile(tile);
    board.addTile(tile2);

    assertEquals(tile, board.getTile(1));
    assertEquals(tile2, board.getTile(2));
    assertNull(board.getTile(3));
  }

  @Test
  void getTile() {
    Tile tile = new Tile.Builder(1).build();
    Tile tile2 = new Tile.Builder(2).build();

    board.addTile(tile);
    board.addTile(tile2);

    assertEquals(tile, board.getTile(1));
    assertEquals(tile2, board.getTile(2));
    assertNull(board.getTile(3));
  }

  @Test
  void getTiles() {
    Tile tile = new Tile.Builder(1).position(0, 0).build();
    Tile tile2 = new Tile.Builder(2).position(1, 1).build();

    board.addTile(tile);
    board.addTile(tile2);

    assertEquals(2, board.getTiles().size());
    assertTrue(board.getTiles().containsKey(1));
    assertTrue(board.getTiles().containsKey(2));
    assertFalse(board.getTiles().containsKey(3));
  }

  @Test
  void tileCollisionTest() {
    Tile tile = new Tile.Builder(1).position(0, 0).build();
    Tile tile2 = new Tile.Builder(2).position(7, 0).build();

    board.addTile(tile);

    assertEquals(1, board.getTiles().size());
    assertTrue(board.getTiles().containsKey(1));
    assertThrows(BoardRangeException.class, () -> board.addTile(tile2));
  }

  @Test
  void getRowAndColCount() {
    assertEquals(5, board.getRowCount());
    assertEquals(5, board.getColCount());
  }

  @Test
  void resolveReferences() {
    Tile tile1 = new Tile.Builder(1).position(0, 0).nextTileId(2).build();
    Tile tile2 = new Tile.Builder(2).position(0, 1).build();

    board.addTile(tile1);
    board.addTile(tile2);

    board.resolveReferences(board);

    assertEquals(tile2, tile1.getNextTile().get());
  }
}