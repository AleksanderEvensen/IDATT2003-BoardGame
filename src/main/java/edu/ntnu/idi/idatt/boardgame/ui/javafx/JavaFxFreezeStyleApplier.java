package edu.ntnu.idi.idatt.boardgame.ui.javafx;

import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.freeze.FreezeAction;
import edu.ntnu.idi.idatt.boardgame.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.style.FreezeStyle;
import edu.ntnu.idi.idatt.boardgame.ui.style.TileStyleApplier;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * JavaFX-specific implementation of the freeze tile style applier.
 */
public class JavaFxFreezeStyleApplier implements TileStyleApplier {

    @Override
    public void applyStyle(Tile tile, TileAction action, Object parent) {
        if (!(parent instanceof Pane) || !(action instanceof FreezeAction)) {
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

        actionTile.setIcon(FontAwesomeSolid.SNOWFLAKE, FreezeStyle.ICON_ROTATION);
        actionTile.setBackgroundColor(Color.valueOf(FreezeStyle.BACKGROUND_COLOR));
    }
}