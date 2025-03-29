package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.ui.TileStyleService;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.Set;

/**
 * A component representing the game board.
 */
public class GameBoard extends GridPane {
  private final Set<TileComponent> tiles;
  private final Pane overlayPane;

  private int maxCol = 0;
  private int maxRow = 0;

  /**
   * Create a new GameBoard.
   */
  GameBoard() {
    tiles = new HashSet<>();
    overlayPane = new Pane();
    overlayPane.setPickOnBounds(false);
    overlayPane.setManaged(false);

  }

  /**
   * Add a tile to the board.
   *
   * @param tile the tile to add
   */
  public void add(Tile tile) {
    TileComponent tileComponent = new TileComponent(tile.getTileId());
    add(tileComponent, tile.getCol(), tile.getRow());
    tiles.add(tileComponent);

    if (tile.getCol() > maxCol) maxCol = tile.getCol();
    if (tile.getRow() > maxRow) maxRow = tile.getRow();
  }

  /**
   * Returns the overlay pane (for drawing ladders, lines, etc.).
   */
  public Pane getOverlayPane() {
    return overlayPane;
  }

  /**
   * Span the overlay pane to cover the entire grid, and bring it to front.
   */
  private void spanOverlay() {
    getChildren().remove(overlayPane);
    add(overlayPane, 0, 0, maxCol, maxRow);
    overlayPane.toFront();
  }

  /**
   * A builder to create and configure a GameBoard.
   */
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
        tile.getAction().ifPresent(action ->
            TileStyleService.applyStyle(tile, action, gameBoard));
      });
      return this;
    }

    /**
     * Finalize the board: make sure the overlay spans everything.
     *
     * @return the fully built GameBoard
     */
    public GameBoard build() {
      gameBoard.spanOverlay();
      return gameBoard;
    }
  }
}
