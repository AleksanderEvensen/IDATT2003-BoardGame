package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;
import edu.ntnu.idi.idatt.boardgame.model.Tile;

/**
 * Event fired when a player uses a ladder to move between tiles.
 * <p>
 * This event contains information about the ladder traversal, including the
 * player who used the ladder and the source and destination tiles. UI
 * components
 * can observe this event to animate the ladder movement.
 * </p>
 * 
 * @see edu.ntnu.idi.idatt.boardgame.game.GameController
 * @see edu.ntnu.idi.idatt.boardgame.actions.ladder.LadderAction
 * @see edu.ntnu.idi.idatt.boardgame.model.Player
 * @see edu.ntnu.idi.idatt.boardgame.model.Tile
 * @since v2.0.0
 */
public class LadderActionEvent implements GameEvent {
    private final Player player;
    private final Tile fromTile;
    private final Tile toTile;

    /**
     * Creates a new LadderActionEvent.
     *
     * @param player   the player who used the ladder
     * @param fromTile the tile where the ladder starts
     * @param toTile   the tile where the ladder ends
     */
    public LadderActionEvent(Player player, Tile fromTile, Tile toTile) {
        this.player = player;
        this.fromTile = fromTile;
        this.toTile = toTile;
    }

    /**
     * Gets the player who used the ladder.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the tile where the ladder starts.
     *
     * @return the source tile
     */
    public Tile getFromTile() {
        return fromTile;
    }

    /**
     * Gets the tile where the ladder ends.
     *
     * @return the destination tile
     */
    public Tile getToTile() {
        return toTile;
    }
}
