package edu.ntnu.idi.idatt.boardgame.game.events;

import edu.ntnu.idi.idatt.boardgame.model.Player;

public record NewRoundEvent(
    int roundNumber,
    Player currentPlayer
) implements GameEvent {

}
