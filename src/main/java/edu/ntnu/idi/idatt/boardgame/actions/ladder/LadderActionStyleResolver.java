package edu.ntnu.idi.idatt.boardgame.actions.ladder;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.TileActionStyleResolver;
import edu.ntnu.idi.idatt.boardgame.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class LadderActionStyleResolver extends TileActionStyleResolver {


  @Override
  public void resolveStyle(Tile tile, TileAction action, Pane parent) {
    LadderAction ladderAction = (LadderAction) action;
    List<TileComponent> tileComponents =
        parent.getChildren().filtered(node -> node instanceof TileComponent)
            .stream()
            .map(node -> (TileComponent) node)
            .collect(Collectors.toList());

    TileComponent startTile = tileComponents.stream()
        .filter(tileComponent -> tileComponent.getTileId() == tile.getTileId())
        .findFirst()
        .orElse(null);

    if (startTile != null) {
      startTile.setBackgroundColor(Color.valueOf("#ff0000"));
      startTile.setIcon(FontAwesomeSolid.ARROW_UP);
    }

    TileComponent destinationTile = tileComponents.stream()
        .filter(tileComponent -> tileComponent.getTileId() == ladderAction.getDestinationTile()
            .getTileId())
        .findFirst().orElse(null);

    if (destinationTile != null) {
      destinationTile.setBackgroundColor(Color.valueOf("#ff6f72"));
    }


  }
}