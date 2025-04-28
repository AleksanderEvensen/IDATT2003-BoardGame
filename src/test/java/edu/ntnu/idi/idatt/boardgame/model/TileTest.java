package edu.ntnu.idi.idatt.boardgame.model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;

/**
 * Test class for the Tile class.
 */
class TileTest {

  private Tile tile;
  private Tile nextTile;
  private Tile previousTile;
  private TileAction action;

  @BeforeEach
  void setUp() {
    action = player -> {
    };
    nextTile = new Tile.Builder(2).build();
    previousTile = new Tile.Builder(3).build();
    tile = new Tile.Builder(1).build();
  }

  @Test
  void testBuilderWithMinimalParameters() {
    Tile simpleTile = new Tile.Builder(1).build();

    assertEquals(1, simpleTile.getTileId());
    assertEquals(0, simpleTile.getRow());
    assertEquals(0, simpleTile.getCol());
    assertTrue(simpleTile.getAction().isEmpty());
    assertTrue(simpleTile.getNextTile().isEmpty());
    assertTrue(simpleTile.getPreviousTile().isEmpty());
  }


  @Test
  void testPositionMethods() {
    tile.setRow(5);
    tile.setCol(7);

    assertEquals(5, tile.getRow());
    assertEquals(7, tile.getCol());
  }

  @Test
  void testGetNextTile() {
    tile.setNextTile(nextTile);

    Optional<Tile> result = tile.getNextTile();

    assertTrue(result.isPresent());
    assertEquals(nextTile, result.get());
  }

  @Test
  void testGetPreviousTile() {
    tile.setPreviousTile(previousTile);

    Optional<Tile> result = tile.getPreviousTile();

    assertTrue(result.isPresent());
    assertEquals(previousTile, result.get());
  }


  @Test
  void testGetAction() {
    tile.setAction(action);

    Optional<TileAction> result = tile.getAction();

    assertTrue(result.isPresent());
    assertEquals(action, result.get());
  }

  @Test
  void testEmptyOptionalWhenReferencesAreNull() {
    assertTrue(tile.getNextTile().isEmpty());
    assertTrue(tile.getPreviousTile().isEmpty());
    assertTrue(tile.getAction().isEmpty());
  }

  @Test
  void testResolveReferences() {
    Board board = new Board(10, 10);
    Tile boardTile1 = new Tile.Builder(1).build();
    Tile boardTile2 = new Tile.Builder(2).build();
    Tile boardTile3 = new Tile.Builder(3).build();

    board.addTile(boardTile1);
    board.addTile(boardTile2);
    board.addTile(boardTile3);

    Tile testTile = new Tile.Builder(4)
        .nextTileId(boardTile1.getTileId())
        .previousTileId(boardTile2.getTileId())
        .build();

    testTile.resolveReferences(board);

    assertEquals(boardTile1, testTile.getNextTile().get());
    assertEquals(boardTile2, testTile.getPreviousTile().get());
  }

  @Test
  void testBuilderPosition() {
    Tile positionTile = new Tile.Builder(5)
        .position(3, 4)
        .build();

    assertEquals(3, positionTile.getRow());
    assertEquals(4, positionTile.getCol());
  }

  @Test
  void testBuilderAction() {
    Tile actionTile = new Tile.Builder(5)
        .action(action)
        .build();

    assertEquals(action, actionTile.getAction().get());
  }


}