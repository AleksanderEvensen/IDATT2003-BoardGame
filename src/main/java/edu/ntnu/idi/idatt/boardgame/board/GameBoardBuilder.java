package edu.ntnu.idi.idatt.boardgame.board;

import edu.ntnu.idi.idatt.boardgame.components.GameBoard;
import edu.ntnu.idi.idatt.boardgame.model.Game;
import edu.ntnu.idi.idatt.boardgame.ui.TileStyleService;

public class GameBoardBuilder {
  private final GameBoard gameBoard;
  private Game game;

  public GameBoardBuilder(Game game) {
    this.game = game;
    gameBoard = new GameBoard();
  }

  public GameBoardBuilder addTiles() {
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
  public GameBoardBuilder resolveActionStyles() {
    game.getBoard().getTiles().forEach((id, tile) -> {
      if (tile.getAction().isPresent()) {
        // Use the new styling system directly
        TileStyleService.applyStyle(tile, tile.getAction().get(), gameBoard);
      }
    });
    return this;
  }

  public GameBoard build() {
    return gameBoard;
  }
}
