package edu.ntnu.idi.idatt.boardgame.model;

import java.util.Map;

public class Board {
    private Map<Integer, Tile> tiles;

    public void addTile(Tile tile) {
        tiles.put(tile.getTileId(), tile);
    }

    public Tile getTile(int tileId) {
        return tiles.get(tileId);
    }

}
