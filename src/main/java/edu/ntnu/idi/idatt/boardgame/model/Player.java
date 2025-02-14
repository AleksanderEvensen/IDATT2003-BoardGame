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

    /**
     * Moves the player one til in the given direction
     * @param forward
     * @return true if the player moved, false if the player could not move
     */
    public boolean moveOneTile(boolean forward) {
        if (forward && currentTile.getNextTile().isPresent()) {
            currentTile = currentTile.getNextTile().get();
            return true;
        } else if (!forward && currentTile.getLastTile().isPresent()) {
            currentTile = currentTile.getLastTile().get();
            return true;
        }
        return false;
    }

    /**
     * Moves the player the given amount of steps
     * If there is not more tile to move forward, the player will try to move backwards
     * If backwards is not possible anymore, the rest of the steps will be returned.
     * @param steps the steps to move
     * @return the amount of steps the player actually moved
     */
    public int move(int steps) {
        boolean shouldMoveForward = true;
        int tilesMoved = steps;

        for (int i = 0; i < steps; i++) {
            boolean didMove = this.moveOneTile(shouldMoveForward);
            // If moved then continue moving forward
            if (didMove) continue;

            // Otherwise reverse the direction and try to move backwards
            shouldMoveForward = false;
            didMove = this.moveOneTile(false);
            if (!didMove) {
                tilesMoved = i;
                break;
            }
        }

        this.currentTile.getAction().ifPresent(action -> action.perform(this));

        return tilesMoved;
    }

    public void moveToTile(Tile tile) {
        this.moveToTile(tile, true);
    }

    public void moveToTile(Tile tile, boolean shouldPerformAction) {
        this.currentTile = tile;

        if (shouldPerformAction){
            this.currentTile.getAction().ifPresent(action -> action.perform(this));
        }
    }

}
