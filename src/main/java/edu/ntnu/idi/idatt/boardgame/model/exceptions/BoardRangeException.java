package edu.ntnu.idi.idatt.boardgame.model.exceptions;

/**
 * Exception thrown when a board range is exceeded.
 */
public class BoardRangeException extends RuntimeException {

  /**
   * Constructs a new BoardRangeException with the specified detail message.
   *
   * @param message the detail message
   */
  public BoardRangeException(String message) {
    super(message);
  }
}
