package edu.ntnu.idi.idatt.boardgame.model.actions;

import edu.ntnu.idi.idatt.boardgame.model.entities.Board;

/**
 * Interface for resolving references to other tiles on the board.
 * <p>
 * Implementing classes should provide logic to resolve references to other tiles.
 * </p>
 *
 * @version v1.0.0
 * @see Board
 * @since v2.0.0
 */
public interface HasTileReferenceResolver {

  /**
   * Resolves references to other tiles on the board.
   *
   * @param board the board containing the tiles
   * @see Board
   */
  void resolveReferences(Board board);
}