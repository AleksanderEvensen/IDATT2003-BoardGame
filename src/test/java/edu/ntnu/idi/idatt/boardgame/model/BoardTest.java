package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

  Board board;
  @BeforeEach
  void setUp() {
    board = new Board(1,1);
  }

  @Test
  void addTile() {
    //Arrange
    Tile tile = new Tile(1);
    Tile tile2 = new Tile(2);

    //Act
    board.addTile(tile);
    board.addTile(tile2);

    //Assert
    assertEquals(tile, board.getTile(1));
    assertEquals(tile2, board.getTile(2));
    assertNull(board.getTile(3));
  }

  @Test
  void getTile() {
    //Arrange
    Tile tile = new Tile(1);
    Tile tile2 = new Tile(2);

    //Act
    board.addTile(tile);
    board.addTile(tile2);

    //Assert
    assertEquals(tile, board.getTile(1));
    assertEquals(tile2, board.getTile(2));
    assertNull(board.getTile(3));
  }
}