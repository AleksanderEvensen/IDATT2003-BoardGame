package edu.ntnu.idi.idatt.boardgame.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

  protected Player player;
  protected Tile tile;
  protected Tile tile2;

  @BeforeEach
  void setUp() {
    player = new Player(1, "Player 1");
    tile = new Tile(1);
    tile2 = new Tile(2);
    tile.setNextTile(tile2);
  }

  @Test
  void getPlayerId() {
    assertEquals(1, player.getPlayerId());
  }

  @Test
  void getName() {
    assertEquals("Player 1", player.getName());
  }

  @Test
  void getCurrentTile() {
    assertNull(player.getCurrentTile());
  }

  @Test
  void placeOnTile() {
    player.placeOnTile(tile);
    assertNotNull(player.getCurrentTile());
  }

  @Test
  void moveOneTile() {
    player.placeOnTile(tile);
    player.move(1);
    assertNotNull(player.getCurrentTile());
    assertEquals(tile2, player.getCurrentTile());

  }

  @Test
  void move() {
    player.placeOnTile(tile);
    player.move(2);
    assertNotNull(player.getCurrentTile());
    assertEquals(tile2, player.getCurrentTile());
  }

  @Test
  void moveToTile() {
    player.moveToTile(tile2);
    assertNotNull(player.getCurrentTile());
    assertEquals(tile2, player.getCurrentTile());
  }

}