package edu.ntnu.idi.idatt.boardgame.actions;

import edu.ntnu.idi.idatt.boardgame.model.Player;

public class LadderAction implements TileAction {

    private int destinationTileId;

    public LadderAction(int destinationTileId) {
        this.destinationTileId = destinationTileId;
    }

    @Override
    public void perform(Player player) {
        player.move(Math.abs(destinationTileId - player.getCurrentTile().getTileId()), destinationTileId > player.getCurrentTile().getTileId());
    }
}
