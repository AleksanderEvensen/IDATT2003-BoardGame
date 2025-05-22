package edu.ntnu.idi.idatt.boardgame.model.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception thrown when a player already exists in the game.
 * <p>
 * This exception is used to indicate that an attempt was made to add a player to the game who is
 * already present.
 * </p>
 */
@StandardException
public class PlayerExistsException extends RuntimeException {

}
