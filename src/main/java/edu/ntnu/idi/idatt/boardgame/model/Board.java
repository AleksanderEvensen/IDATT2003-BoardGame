package edu.ntnu.idi.idatt.boardgame.model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Tile> tiles = new HashMap<>();

    public void addTile(Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null");
        }
        tiles.put(tile.getTileId(), tile);
    }

    public Tile getTile(int tileId) {
        if (tileId <= 0) {
            throw new IllegalArgumentException("TileId must be greater than 0");
        }

        if (!tiles.containsKey(tileId)) {
            throw new IllegalArgumentException("Tile with id " + tileId + " does not exist");
        }

        return tiles.get(tileId);
    }

}
