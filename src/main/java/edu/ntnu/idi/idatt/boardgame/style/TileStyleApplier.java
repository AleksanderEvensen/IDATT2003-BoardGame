package edu.ntnu.idi.idatt.boardgame.style;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

/**
 * An abstract interface for applying styles to tiles based on their actions. This decouples the
 * styling logic from specific UI implementations.
 *
 * @version v1.0.0
 * @since v2.0.0
 */
public interface TileStyleApplier {

  /**
   * Resolves and applies the style for a specific tile and its action.
   *
   * @param tile   The tile to style
   * @param action The action associated with the tile
   * @param parent The parent container (implementation-specific)
   */
  void applyStyle(Tile tile, TileAction action, Object parent);
}