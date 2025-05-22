package edu.ntnu.idi.idatt.boardgame.model.events;

import edu.ntnu.idi.idatt.boardgame.model.entities.Player;

public record NewRoundEvent(
    int roundNumber,
    Player currentPlayer
) implements GameEvent {

}
