package edu.ntnu.idi.idatt.boardgame.actions;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

public class LadderAction implements TileAction {

    private final Tile destinationTile;
    public LadderAction(Tile destinationTile) {
        if (destinationTile == null) {
            throw new IllegalArgumentException("Destination tile cannot be null");
        }
        this.destinationTile = destinationTile;
    }

    public Tile getDestinationTile() {
        return destinationTile;
    }

    @Override
    public void perform(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        player.moveToTile(destinationTile, false);
        System.out.printf("Player %s triggered a LadderAction and move to tile %d\n", player.getName(), destinationTile.getTileId());
    }

    @Override
    public String toString() {
        return "LadderAction{" +
                "destinationTile=" + destinationTile +
                '}';
    }
}
