package edu.ntnu.idi.idatt.boardgame.core.filesystem;

/**
 * An exception that is thrown when a file cannot be saved.
 */
public class FileSaveException extends RuntimeException {

  /**
   * Constructs a new FileSaveException with the specified detail message.
   *
   * @param message the detail message
   */
  public FileSaveException(String message) {
    super(message);
  }
}
