package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

  Player player;
  Tile startTile;
  Tile nextTile;
  Tile lastTile;

  @BeforeEach
  void setUp() {
    player = new Player(1, "Player1");
    startTile = new Tile(1);
    nextTile = new Tile(2);
    lastTile = new Tile(0);
    startTile.setNextTile(nextTile);
    startTile.setLastTile(lastTile);
    player.placeOnTile(startTile);
  }

  @Test
  void getPlayerId() {
    assertEquals(1, player.getPlayerId());
  }

  @Test
  void getName() {
    assertEquals("Player1", player.getName());
  }

  @Test
  void getCurrentTile() {
    assertEquals(startTile, player.getCurrentTile());
  }

  @Test
  void placeOnTile() {
    Tile newTile = new Tile(3);
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
    boolean moved = player.moveOneTile(false);
    assertTrue(moved);
    assertEquals(lastTile, player.getCurrentTile());
  }

  @Test
  void move() {
    int stepsMoved = player.move(2);
    assertEquals(1, stepsMoved);
    assertEquals(nextTile, player.getCurrentTile());
  }

  @Test
  void moveToTile() {
    Tile newTile = new Tile(3);
    player.moveToTile(newTile);
    assertEquals(newTile, player.getCurrentTile());
  }

  @Test
  void moveToTileWithAction() {
    Tile newTile = new Tile(3);
    player.moveToTile(newTile, false);
    assertEquals(newTile, player.getCurrentTile());
  }
}