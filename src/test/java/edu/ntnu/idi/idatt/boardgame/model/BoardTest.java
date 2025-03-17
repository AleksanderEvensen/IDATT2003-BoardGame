package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idi.idatt.boardgame.model.tiles.BoardRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

  Board board;
  @BeforeEach
  void setUp() {
    board = new Board(5,5);
  }

  @Test
  void addTile() {
    //Arrange
    Tile tile = new Tile(1, 0, 0);
    Tile tile2 = new Tile(2 , 1, 1);

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

  @Test
  void getTiles() {
    //Arrange
    Tile tile = new Tile(1, 0, 0);
    Tile tile2 = new Tile(2, 1, 1);

    //Act
    board.addTile(tile);
    board.addTile(tile2);

    //Assert
    assertEquals(2, board.getTiles().size());
    assertTrue(board.getTiles().containsKey(1));
    assertTrue(board.getTiles().containsKey(2));
    assertFalse(board.getTiles().containsKey(3));
  }

  @Test
  void tileColisionTest() {
    //Arrange
    Tile tile = new Tile(1, 0, 0);
    Tile tile2 = new Tile(2, 7, 0);

    //Act
    board.addTile(tile);


    //Assert
    assertEquals(1, board.getTiles().size());
    assertTrue(board.getTiles().containsKey(1));
    assertThrows(BoardRangeException.class, () -> board.addTile(tile2));
  }
}