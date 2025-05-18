package edu.ntnu.idi.idatt.boardgame.ui.style;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;

/**
 * Factory interface for obtaining the appropriate style applier for a specific tile action.
 * Different UI frameworks can implement this factory to provide their framework-specific stylers.
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