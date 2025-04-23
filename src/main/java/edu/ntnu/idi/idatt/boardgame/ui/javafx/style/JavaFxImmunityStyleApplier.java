package edu.ntnu.idi.idatt.boardgame.ui.javafx.style;

import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.immunity.ImmunityAction;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.style.ImmunityStyle;
import edu.ntnu.idi.idatt.boardgame.ui.style.TileStyleApplier;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * JavaFX-specific implementation of the immunity tile style applier.
 *
 * @see TileStyleApplier
 * @since v2.0.0
 */
public class JavaFxImmunityStyleApplier implements TileStyleApplier {

    @Override
    public void applyStyle(Tile tile, TileAction action, Object parent) {
        if (!(parent instanceof Pane) || !(action instanceof ImmunityAction)) {
            return;
        }

        Pane pane = (Pane) parent;

        List<TileComponent> tileComponents = pane.getChildren().filtered(node -> node instanceof TileComponent)
                .stream()
                .map(node -> (TileComponent) node)
                .collect(Collectors.toList());

        TileComponent actionTile = tileComponents.stream()
                .filter(tileComponent -> tileComponent.getTileId() == tile.getTileId())
                .findFirst()
                .orElse(null);

        if (actionTile == null) {
            return;
        }

        actionTile.setIcon(FontAwesomeSolid.SHIELD_ALT, ImmunityStyle.ICON_ROTATION, Color.WHITE);
        actionTile.setBackgroundColor(Color.valueOf(ImmunityStyle.BACKGROUND_COLOR));
    }
}