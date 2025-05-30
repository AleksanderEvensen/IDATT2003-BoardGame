package edu.ntnu.idi.idatt.boardgame.model.style;

import edu.ntnu.idi.idatt.boardgame.javafx.style.JavaFxTileStyleApplierFactory;
import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;

/**
 * Service class to provide easy access to the tile styling system. This class abstracts away the
 * details of the styling system and provides a simple API for applying styles to tiles.
 *
 * @version v1.0.0
 * @since v2.0.0
 */
public class TileStyleService {

  /// The default factory to use for creating style appliers. This can be changed to support
  /// different
  private static TileStyleApplierFactory factory = JavaFxTileStyleApplierFactory.getInstance();

  /**
   * Set a different style applier factory to be used by the service. This allows for switching
   * between different UI frameworks.
   *
   * @param newFactory The new factory to use
   */
  public static void setStyleApplierFactory(TileStyleApplierFactory newFactory) {
    factory = newFactory;
  }

  /**
   * Apply the appropriate style to a tile based on its action.
   *
   * @param tile   The tile to style
   * @param action The action associated with the tile
   * @param parent The parent container (implementation-specific)
   */
  public static void applyStyle(Tile tile, TileAction action, Object parent) {
    TileStyleApplier applier = factory.getStyleApplier(action);
    if (applier != null) {
      applier.applyStyle(tile, action, parent);
    }
  }
}