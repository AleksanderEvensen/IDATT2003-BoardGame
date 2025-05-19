package edu.ntnu.idi.idatt.boardgame.javafx.style;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.goal.GoalTileAction;
import edu.ntnu.idi.idatt.boardgame.javafx.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import edu.ntnu.idi.idatt.boardgame.style.TileStyleApplier;
import java.util.List;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.boxicons.BoxiconsSolid;

/**
 * JavaFX-specific implementation of the goal tile style applier.
 *
 * <p>
 * This class applies a specific style to goal tiles in the JavaFX UI.
 * </p>
 *
 * @version v1.0.0
 * @see TileStyleApplier
 * @since v2.0.0
 */
public class JavaFxGoalStyleApplier implements TileStyleApplier {

  @Override
  public void applyStyle(Tile tile, TileAction action, Object parent) {

    if (!(parent instanceof Pane pane) || !(action instanceof GoalTileAction)) {
      return;
    }

    List<TileComponent> tileComponents =
        pane.getChildren().filtered(node -> node instanceof TileComponent).stream()
            .map(node -> (TileComponent) node).toList();

    TileComponent actionTile = tileComponents.stream()
        .filter(tileComponent -> tileComponent.getTileId() == tile.getTileId()).findFirst()
        .orElse(null);

    if (actionTile == null) {
      return;
    }

    Tooltip.install(actionTile,
        new Tooltip("When a player comes to this tile it will trigger the win condition"));

    actionTile.setIcon(BoxiconsSolid.FLAG_CHECKERED, 0, Color.BLACK);
    actionTile.setBackgroundColor(Color.valueOf("#EEF20A"));
  }

}
