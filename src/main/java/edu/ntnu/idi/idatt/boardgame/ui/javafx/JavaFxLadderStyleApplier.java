package edu.ntnu.idi.idatt.boardgame.ui.javafx;

import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction;
import edu.ntnu.idi.idatt.boardgame.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.ui.style.LadderTileStyle;
import edu.ntnu.idi.idatt.boardgame.ui.style.TileStyleApplier;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * JavaFX-specific implementation of the ladder tile style applier.
 */
public class JavaFxLadderStyleApplier implements TileStyleApplier {

    @Override
    public void applyStyle(Tile tile, TileAction action, Object parent) {
        if (!(parent instanceof Pane) || !(action instanceof LadderAction)) {
            return;
        }

        Pane pane = (Pane) parent;
        LadderAction ladderAction = (LadderAction) action;

        List<TileComponent> tileComponents = pane.getChildren().filtered(node -> node instanceof TileComponent)
                .stream()
                .map(node -> (TileComponent) node)
                .collect(Collectors.toList());

        TileComponent startTile = tileComponents.stream()
                .filter(tileComponent -> tileComponent.getTileId() == tile.getTileId())
                .findFirst()
                .orElse(null);

        TileComponent destinationTile = tileComponents.stream()
                .filter(tileComponent -> tileComponent.getTileId() == ladderAction.getDestinationTile()
                        .getTileId())
                .findFirst().orElse(null);

        if (startTile == null || destinationTile == null) {
            return;
        }

        boolean isPositive = ladderAction.getDestinationTile().getTileId() > tile.getTileId();

        double angle = Math.toDegrees(Math.atan2(
                ladderAction.getDestinationTile().getRow() - tile.getRow(),
                ladderAction.getDestinationTile().getCol() - tile.getCol())) + 90;
        if (angle < 0) {
            angle += 360;
        }

        if (isPositive) {
            startTile.setIcon(FontAwesomeSolid.ARROW_UP, angle);
            startTile.setBackgroundColor(Color.valueOf(LadderTileStyle.START_POSITIVE_COLOR));
        } else {
            startTile.setIcon(FontAwesomeSolid.ARROW_UP, angle);
            startTile.setBackgroundColor(Color.valueOf(LadderTileStyle.START_NEGATIVE_COLOR));
        }

        if (isPositive) {
            destinationTile.setBackgroundColor(Color.valueOf(LadderTileStyle.POSITIVE_COLOR));
        } else {
            destinationTile.setBackgroundColor(Color.valueOf(LadderTileStyle.NEGATIVE_COLOR));
        }
    }
}