package edu.ntnu.idi.idatt.boardgame.model;

/**
 * Represents a player in the board game.
 * <p>
 * Each player has an ID, a name, and a current tile they are on.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.model.Tile
 */
public class Player {
    private int playerId;
    private String name;
    private Tile currentTile;

    /**
     * Constructs a player with the specified ID and name.
     *
     * @param playerId the player's ID
     * @param name the player's name
     */
    public Player(int playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    /**
     * Returns the player's ID.
     *
     * @return the player ID
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current tile the player is on.
     *
     * @return the current tile
     * @see edu.ntnu.idi.idatt.boardgame.model.Tile
     */
    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Places the player on the specified tile.
     *
     * @param tile the tile to place the player on
     * @see edu.ntnu.idi.idatt.boardgame.model.Tile
     */
    public void placeOnTile(Tile tile) {
        this.currentTile = tile;
    }

    /**
     * Moves the player one tile in the given direction.
     *
     * @param forward true to move forward, false to move backward
     * @return true if the player moved, false if the player could not move
     * @see edu.ntnu.idi.idatt.boardgame.model.Tile
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
     * Moves the player the given number of steps.
     * <p>
     * If there are no more tiles to move forward, the player will try to move backward.
     * If moving backward is not possible anymore, the rest of the steps will be returned.
     * </p>
     *
     * @param steps the number of steps to move
     * @return the number of steps the player actually moved
     * @see edu.ntnu.idi.idatt.boardgame.model.Tile
     */
    public int move(int steps) {
        boolean shouldMoveForward = true;
        int tilesMoved = steps;

        for (int i = 0; i < steps; i++) {
            boolean didMove = this.moveOneTile(shouldMoveForward);
            // If moved then continue moving forward
            if (didMove) continue;

            // Otherwise reverse the direction and try to move backward
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

    /**
     * Moves the player to the specified tile.
     *
     * @param tile the tile to move to
     * @see edu.ntnu.idi.idatt.boardgame.model.Tile
     */
    public void moveToTile(Tile tile) {
        this.moveToTile(tile, true);
    }

    /**
     * Moves the player to the specified tile and optionally performs the tile's action.
     *
     * @param tile the tile to move to
     * @param shouldPerformAction true to perform the tile's action, false otherwise
     * @see edu.ntnu.idi.idatt.boardgame.model.Tile
     */
    public void moveToTile(Tile tile, boolean shouldPerformAction) {
        this.currentTile = tile;

        if (shouldPerformAction) {
            this.currentTile.getAction().ifPresent(action -> action.perform(this));
        }
    }
}