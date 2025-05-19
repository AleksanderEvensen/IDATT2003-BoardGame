package edu.ntnu.idi.idatt.boardgame.javafx.style;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.immunity.ImmunityAction;
import edu.ntnu.idi.idatt.boardgame.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.style.ImmunityStyle;
import edu.ntnu.idi.idatt.boardgame.style.TileStyleApplier;
import java.util.List;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/**
 * JavaFX-specific implementation of the immunity tile style applier.
 *
 * @version v1.0.0
 * @see TileStyleApplier
 * @since v2.0.0
 */
public class JavaFxImmunityStyleApplier implements TileStyleApplier {

  @Override
  public void applyStyle(Tile tile, TileAction action, Object parent) {
    if (!(parent instanceof Pane pane) || !(action instanceof ImmunityAction)) {
      return;
    }

    List<TileComponent> tileComponents = pane.getChildren()
        .filtered(node -> node instanceof TileComponent)
        .stream()
        .map(node -> (TileComponent) node)
        .toList();

    TileComponent actionTile = tileComponents.stream()
        .filter(tileComponent -> tileComponent.getTileId() == tile.getTileId())
        .findFirst()
        .orElse(null);

    Tooltip.install(actionTile, new Tooltip("This tile grants immunity to the player."));

    if (actionTile == null) {
      return;
    }

    actionTile.setIcon(FontAwesomeSolid.SHIELD_ALT, ImmunityStyle.ICON_ROTATION, Color.WHITE);
    actionTile.setBackgroundColor(Color.valueOf(ImmunityStyle.BACKGROUND_COLOR));
  }
}