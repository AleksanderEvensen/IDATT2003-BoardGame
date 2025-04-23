package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;

public class NewRoundEvent implements GameEvent {

  private final int roundNumber;
  private final Player currentPlayer;

  public NewRoundEvent(int roundNumber, Player currentPlayer) {
    this.roundNumber = roundNumber;
    this.currentPlayer = currentPlayer;
  }

  public int getRoundNumber() {
    return roundNumber;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

}
