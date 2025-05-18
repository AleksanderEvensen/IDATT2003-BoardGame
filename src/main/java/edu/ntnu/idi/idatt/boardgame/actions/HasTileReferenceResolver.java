package edu.ntnu.idi.idatt.boardgame.actions;

import edu.ntnu.idi.idatt.boardgame.model.Board;

/**
 * Interface for resolving references to other tiles on the board.
 * <p>
 * Implementing classes should provide logic to resolve references to other tiles.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.model.Board
 * @since v1.0.0
 */
public interface HasTileReferenceResolver {

  /**
   * Resolves references to other tiles on the board.
   *
   * @param board the board containing the tiles
   * @see edu.ntnu.idi.idatt.boardgame.model.Board
   */
  void resolveReferences(Board board);
}