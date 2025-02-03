package edu.ntnu.idi.idatt.boardgame.model;

import edu.ntnu.idi.idatt.boardgame.actions.TileAction;

import java.util.Optional;

public class Tile {

    private int tileId;
    private TileAction action;

    private Optional<Tile> nextTile = Optional.empty();
    private Optional<Tile> lastTile = Optional.empty();


    public Tile(int tileId, TileAction action) {
        this.tileId = tileId;
        this.action = action;
    }
    public Tile(int tileId, TileAction action, Tile lastTile) {
        this(tileId, action);
        this.lastTile = Optional.of(lastTile);
    }

    public Tile(int tileId, TileAction action, Tile lastTile, Tile nextTile) {
        this(tileId, action, lastTile);
        this.nextTile = Optional.of(nextTile);
    }

    public Optional<Tile> getNextTile() {
        return nextTile;
    }

    public Optional<Tile> getLastTile() {
        return lastTile;
    }

    public int getTileId() {
        return tileId;
    }

    public TileAction getAction() {
        return action;
    }

    public void setNextTile(Tile nextTile) {
        if (nextTile == null) {
            this.nextTile = Optional.empty();
            return;
        }
        this.nextTile = Optional.of(nextTile);
    }

    public void setLastTile(Tile lastTile) {
        if (lastTile == null) {
            this.lastTile = Optional.empty();
            return;
        }
        this.lastTile = Optional.of(lastTile);
    }
}
