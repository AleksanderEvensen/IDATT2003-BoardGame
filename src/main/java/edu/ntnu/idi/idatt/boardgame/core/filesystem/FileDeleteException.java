package edu.ntnu.idi.idatt.boardgame.core.filesystem;

/**
 * An exception that is thrown when a file cannot be deleted.
 */
public class FileDeleteException extends RuntimeException {

  /**
   * Constructs a new FileDeleteException with the specified detail message.
   *
   * @param message the detail message
   */
  public FileDeleteException(String message) {
    super(message);
  }
}
