package edu.ntnu.idi.idatt.boardgame.style;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;

/**
 * Factory interface for obtaining the appropriate style applier for a specific tile action.
 * Different UI frameworks can implement this factory to provide their framework-specific stylers.
 *
 * @version v1.0.0
 * @since v2.0.0
 */
public interface TileStyleApplierFactory {

  /**
   * Get a style applier appropriate for the given action type.
   *
   * @param action The tile action
   * @return A TileStyleApplier for the specific action type
   */
  TileStyleApplier getStyleApplier(TileAction action);
}