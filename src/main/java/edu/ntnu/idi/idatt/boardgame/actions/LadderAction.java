package edu.ntnu.idi.idatt.boardgame.actions;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

public class LadderAction implements TileAction {

    private Tile destinationTile;

    public LadderAction(Tile destinationTile) {
        if (destinationTile == null) {
            throw new IllegalArgumentException("Destination tile cannot be null");
        }
        this.destinationTile = destinationTile;
    }

    @Override
    public void perform(Player player) {
        // Ladder shouldn't start chain reaction
        player.moveToTile(destinationTile, false);
        System.out.printf("Player %s triggered a LadderAction and move to tile %d\n", player.getName(), destinationTile.getTileId());
    }
}
