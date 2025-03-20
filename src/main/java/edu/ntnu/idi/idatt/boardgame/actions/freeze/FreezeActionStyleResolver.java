package edu.ntnu.idi.idatt.boardgame.actions.freeze;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;
import edu.ntnu.idi.idatt.boardgame.actions.TileActionStyleResolver;
import edu.ntnu.idi.idatt.boardgame.components.TileComponent;
import edu.ntnu.idi.idatt.boardgame.model.Tile;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class FreezeActionStyleResolver extends TileActionStyleResolver {

    @Override
    public void resolveStyle(Tile tile, TileAction action, Pane parent) {
        FreezeAction freezeAction = (FreezeAction) action;
        List<TileComponent> tileComponents =
            parent.getChildren().filtered(node -> node instanceof TileComponent)
                .stream()
                .map(node -> (TileComponent) node)
                .collect(Collectors.toList());
        TileComponent actionTile = tileComponents.stream()
            .filter(tileComponent -> tileComponent.getTileId() == tile.getTileId())
            .findFirst()
            .orElse(null);



        actionTile.setIcon(FontAwesomeSolid.SNOWFLAKE, 0);
        actionTile.setBackgroundColor(Color.BLUE);

    }




}
