package edu.ntnu.idi.idatt.boardgame.model;

public class Player {
    private int playerId;
    private String name;

    private Tile currentTile;

    public Player(int playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void placeOnTile(Tile tile) {
        this.currentTile = tile;
    }

    public void move(int steps, boolean forward) {
        Tile newTile = currentTile;
        for (int i = 0; i < steps; i++) {
            if (forward && newTile.getNextTile().isPresent()) {
                newTile = newTile.getNextTile().get();
            } else if (!forward && newTile.getLastTile().isPresent()) {
                newTile = newTile.getLastTile().get();
            }
        }

    }

}
