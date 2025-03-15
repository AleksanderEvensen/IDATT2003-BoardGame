package edu.ntnu.idi.idatt.boardgame.actions;

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
        ladderAction.getDestinationTile().getCol() - tile.getCol()
    )) + 90;
    if (angle < 0) {
      angle += 360;
    }

      if (isPositive) {
        startTile.setIcon(FontAwesomeSolid.ARROW_UP, angle);
        startTile.setBackgroundColor(Color.GREEN);
      } else {
        startTile.setIcon(FontAwesomeSolid.ARROW_UP, angle);
        startTile.setBackgroundColor(Color.RED);
      }


      if (isPositive) {
        destinationTile.setBackgroundColor(Color.valueOf("#6ee7b7"));
      } else {
        destinationTile.setBackgroundColor(Color.valueOf("#ff6f72"));
      }

  }

}