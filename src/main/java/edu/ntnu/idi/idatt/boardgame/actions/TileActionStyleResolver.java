package edu.ntnu.idi.idatt.boardgame.actions;

import edu.ntnu.idi.idatt.boardgame.model.Tile;
import javafx.scene.layout.Pane;

public abstract class TileActionStyleResolver {
  public abstract void resolveStyle(Tile tile, TileAction action, Pane parent);
}
