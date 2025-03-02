package edu.ntnu.idi.idatt.boardgame.model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final Map<Integer, Tile> tiles = new HashMap<>();

    public void addTile(Tile tile) {
        tiles.put(tile.getTileId(), tile);
    }

    public Tile getTile(int tileId) {
        return tiles.get(tileId);
    }

    public Map<Integer, Tile> getTiles() {
        return tiles;
    }

}
