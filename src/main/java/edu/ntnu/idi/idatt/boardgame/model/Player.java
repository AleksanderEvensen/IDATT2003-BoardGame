package edu.ntnu.idi.idatt.boardgame.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a player in the board game.
 * <p>
 * Each player has an ID, a name, and a current tile they are on.
 * </p>
 *
 * @see edu.ntnu.idi.idatt.boardgame.model.Tile
 */
public class Player {

  private @Getter
  @Setter String name;
  private @Getter
  @Setter Color color;
  private Tile currentTile;
  private transient int frozenTurns = 0;
  private transient int immunityTurns = 0;

  /**
   * Constructs a player with the specified ID and name.
   *
   * @param name  the player's name
   * @param color the player's color
   */
  public Player(String name, Color color) {
    this.name = name;
    this.color = color;
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
    } else if (!forward && currentTile.getPreviousTile().isPresent()) {
      currentTile = currentTile.getPreviousTile().get();
      return true;
    }
    return false;
  }

  /**
   * Moves the player the given number of steps.
   * <p>
   * If there are no more tiles to move forward, the player will stop moving.
   * </p>
   *
   * @param steps the number of steps to move
   * @return the number of steps the player actually moved
   * @see edu.ntnu.idi.idatt.boardgame.model.Tile
   */
  public int move(int steps) {
    int tilesMoved = 0;
    if (steps < 0) {
      throw new IllegalArgumentException("Steps cannot be negative");
    }

    while (tilesMoved < steps && this.moveOneTile(true)) {
      tilesMoved++;
    }
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
   * @param tile                the tile to move to
   * @param shouldPerformAction true to perform the tile's action, false otherwise
   * @see edu.ntnu.idi.idatt.boardgame.model.Tile
   */
  public void moveToTile(Tile tile, boolean shouldPerformAction) {
    this.currentTile = tile;

    if (shouldPerformAction) {
      this.currentTile.getAction().ifPresent(action -> action.perform(this));
    }
  }

  /**
   * Returns whether the player is frozen.
   *
   * @return true if the player is frozen, false otherwise
   */
  public boolean isFrozen() {
    return frozenTurns > 0;
  }

  /**
   * Returns the number of turns the player is frozen.
   *
   * @return the number of turns the player is frozen
   */
  public int getFrozenTurns() {
    return frozenTurns;
  }

  /**
   * Sets the number of turns the player is frozen.
   *
   * @param frozenTurns the number of turns the player is frozen
   */
  public void setFrozenTurns(int frozenTurns) {
    this.frozenTurns = frozenTurns;
  }

  /**
   * Returns the number of turns the player is immune.
   *
   * @return the number of turns the player is immune
   */
  public int getImmunityTurns() {
    return immunityTurns;
  }

  /**
   * Sets the number of turns the player is immune.
   *
   * @param immunityTurns the number of turns the player is immune
   */
  public void setImmunityTurns(int immunityTurns) {
    if (immunityTurns < 0) {
      throw new IllegalArgumentException("Immunity turns cannot be negative");
    }
    this.immunityTurns = immunityTurns;
  }

  /**
   * Returns whether the player is immune.
   *
   * @return true if the player is immune, false otherwise
   */
  public boolean isImmune() {
    return immunityTurns > 0;
  }

  @Override
  public String toString() {
    return String.format(
        "Player{name='%s', color=%s, currentTile=%s, frozenTurns=%d, immunityTurns=%d}",
        name, color, currentTile, frozenTurns, immunityTurns);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Player other)) {
      return false;
    }
    return name.equals(other.name) && color.equals(other.color);
  }
}