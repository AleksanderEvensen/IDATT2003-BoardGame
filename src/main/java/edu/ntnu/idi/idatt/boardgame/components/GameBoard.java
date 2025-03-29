package edu.ntnu.idi.idatt.boardgame.components;

import edu.ntnu.idi.idatt.boardgame.model.Tile;

import java.util.HashSet;
import java.util.Set;

import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.ui.TileStyleService;
import javafx.scene.layout.GridPane;

public class GameBoard extends GridPane {
  Set<TileComponent> tiles;

  GameBoard() {
    tiles = new HashSet<TileComponent>();
  }

  public void add(Tile tile) {
    TileComponent tileComponent = new TileComponent(tile.getTileId());
    add(tileComponent, tile.getCol(), tile.getRow());
    tiles.add(tileComponent);
  }

  public static class Builder {
    private final GameBoard gameBoard;
    private final Game game;

    public Builder(Game game) {
      this.game = game;
      gameBoard = new GameBoard();
    }

    public Builder addTiles() {
      game.getBoard().getTiles().forEach((id, tile) -> {
        gameBoard.add(tile);
      });
      return this;
    }

    /**
     * Resolves and applies styles for all tiles with actions.
     * 
     * @return this builder for chaining
     */
    public Builder resolveActionStyles() {
      game.getBoard().getTiles().forEach((id, tile) -> {
        if (tile.getAction().isPresent()) {
          TileStyleService.applyStyle(tile, tile.getAction().get(), gameBoard);
        }
      });
      return this;
    }

    public GameBoard build() {
      return gameBoard;
    }
  }
}
