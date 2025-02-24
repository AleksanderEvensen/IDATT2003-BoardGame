package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
  protected Board board;
  @BeforeEach
  void setUp() {
    board = new Board();
  }

  @Test
  void addTile() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      board.addTile(null);
    });

    Tile tile = new Tile(1);
    board.addTile(tile);
    assertEquals(tile, board.getTile(1));

    Tile tile2 = new Tile(2);
    board.addTile(tile2);
    assertEquals(tile2, board.getTile(2));
  }

  @Test
  void getTile() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      board.getTile(-1);
    });

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      board.getTile(0);
    });

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      board.getTile(1);
    });

    Tile tile = new Tile(1);
    board.addTile(tile);
    assertEquals(tile, board.getTile(1));
  }
}