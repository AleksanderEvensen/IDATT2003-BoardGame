package edu.ntnu.idi.idatt.boardgame.components;

import edu.ntnu.idi.idatt.boardgame.model.Tile;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.layout.GridPane;

public class GameBoard extends GridPane {
  Set<TileComponent> tiles;

  public GameBoard() {
    tiles = new HashSet<TileComponent>();
  }

  public void add(Tile tile) {
    TileComponent tileComponent = new TileComponent(tile.getTileId());
    add(tileComponent, tile.getCol(), tile.getRow());
    tiles.add(tileComponent);
  }
}
