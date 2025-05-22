package edu.ntnu.idi.idatt.boardgame.javafx.style;

import edu.ntnu.idi.idatt.boardgame.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.model.actions.freeze.FreezeAction;
import edu.ntnu.idi.idatt.boardgame.model.entities.Tile;
import edu.ntnu.idi.idatt.boardgame.model.style.FreezeStyle;
import edu.ntnu.idi.idatt.boardgame.model.style.TileStyleApplier;
import java.util.List;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * JavaFX-specific implementation of the freeze tile style applier.
 *
 * @version v1.0.0
 * @see TileStyleApplier
 * @since v2.0.0
 */
public class JavaFxFreezeStyleApplier implements TileStyleApplier {

  @Override
  public void applyStyle(Tile tile, TileAction action, Object parent) {
    if (!(parent instanceof Pane pane) || !(action instanceof FreezeAction)) {
      return;
    }

    List<TileComponent> tileComponents =
        pane.getChildren().filtered(node -> node instanceof TileComponent)
            .stream()
            .map(node -> (TileComponent) node)
            .toList();

    TileComponent actionTile = tileComponents.stream()
        .filter(tileComponent -> tileComponent.getTileId() == tile.getTileId())
        .findFirst()
        .orElse(null);

    Tooltip.install(actionTile, new Tooltip("This tile freezes the player."));

    if (actionTile == null) {
      return;
    }

    actionTile.setIcon(FontAwesomeSolid.SNOWFLAKE, FreezeStyle.ICON_ROTATION, Color.WHITE);
    actionTile.setBackgroundColor(Color.valueOf(FreezeStyle.BACKGROUND_COLOR));
  }
}